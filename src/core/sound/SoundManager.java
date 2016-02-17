/** Copyright 2016 Bryan Charles Bettis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.sound;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.userinput.InputManager;

/** Manages the sound system.
* @author Bryan Charles Bettis
*/
public class SoundManager extends core.Subsystem
{
	/** Maximum number of sound effects to play at once. */
	private static final int MAX_PLAYING_SFX = 50;
	/** Manages sound data loaded from files. */
	private static SoundResources srm;
	/** Queue used only for sound effects. */
	private static volatile ConcurrentLinkedDeque<SFXEvent> sfxQueue;
	/** Queue used for general events. */
	private static volatile ConcurrentLinkedDeque<BaseSoundEvent> genQueue;
	/** Currently playing sound effects. */
	private static ConcurrentLinkedQueue<SFX> playingSFX;
	/** Current BGM track. */
	private static BGM currTrack;
	/** The last volume settings used to update the volume of playing
	 * sounds.
	 */
	private int[] currVolLvls;
	
	/** Different ways to transition BGM. Currently only one is implemented.
	 * @author Bryan Charles Bettis
	 */
	public enum BGMTransition
	{
		/** Immediately stop the previous track, then immediately start the
		 * new track.
		 */
		IMMEDIATE
//		/** Fades out the previous track, then fades in the new track. */
//		SMOOTH,
//		/** Fade out the old track, then immediately start the
//		 * new track.
//		 */
//		FADE_OUT,
//		/** Immediately stop the old track, then fade in the
//		 * new track.
//		 */
//		FADE_IN,
//		/** Fade out the old track and fade in the new
//		 * track at the same time.
//		 */
//		CROSSFADE
	}
	
	/** Normal sound manager setup. */
	public SoundManager()
	{
		super(10, "Sound Manager Event Queue");
		System.out.println("Setting Up Sound System...");
		srm = new SoundResources();
		// Setup queues
		sfxQueue = new ConcurrentLinkedDeque<SFXEvent>();
		genQueue = new ConcurrentLinkedDeque<BaseSoundEvent>();
		playingSFX = new ConcurrentLinkedQueue<SFX>();
		currVolLvls = new int[3];
		currVolLvls[0] = 
				(int) core.DynamicSettings.getSetting("MASTER_VOLUME");
		currVolLvls[1] = 
				(int) core.DynamicSettings.getSetting("BGM_VOLUME");
		currVolLvls[2] = 
				(int) core.DynamicSettings.getSetting("SFX_VOLUME");
	}
	
	@Override
	public void startSystem()
	{
		System.out.println("Starting Sound System...");
	}

	@Override
	public boolean runCycle()
	{
		// Pause/resume the current BGM based on program state
		if (currTrack != null)
		{
			if (InputManager.getState().equals(InputManager.InputManagerState.PAUSED))
			{
				currTrack.stop();
				return true;
			}
			else
			{
				currTrack.resume();
			}
		}
		// Clear any finished sound effects
		for (SFX sfx : playingSFX)
		{
			if (sfx.isDone())
			{
				playingSFX.remove(sfx);
			}
		}
		// Clear any "stale" sound effects
		for (SFXEvent e : sfxQueue)
		{
			if (e.isStale())
			{
				sfxQueue.remove(e);
			}
		}
		// Start a few new sound effects
		for (int i = 0; i < 7 && playingSFX.size() < MAX_PLAYING_SFX; ++i)
		{
			// Get the next sound effect
			SFXEvent nextSFX = sfxQueue.poll();
			// No more sfx events
			if (nextSFX == null)
			{
				break;
			}
			// Play the next sound effect
			doPlaySFX(nextSFX);
		}
		// Do a few general events
		for (int i = 0; i < 5; ++i)
		{
			// Get the next general event
			BaseSoundEvent nextGen = genQueue.poll();
			// Stop if no gen events left
			if (nextGen == null)
			{
				break;
			}
			// Get the class of the next event
			Class<? extends BaseSoundEvent> c = nextGen.getClass();
			// Change BGM
			if (c == BGMEvent.class)
			{
				doPlayBGM((BGMEvent) nextGen);
			}
			// Stop current BGM
			else if (c == StopBGMEvent.class)
			{
				doStopBGM((StopBGMEvent) nextGen);
			}
			// Stop all sfx
			else if (c == StopAllSFXEvent.class)
			{
				doStopAllSFX((StopAllSFXEvent) nextGen);
			}
			// Unknown/unused general event
			else
			{
				System.out.println(
						"WARNING: Non-General/Unused event on the "
						+ "general sound event queue: "
								+ c
						);
			}
		}
		return true;
	}
	
	/** Gets the object that manages sound files.
	 * @return the manager of sound resources
	 */
	public static SoundResources getResManager()
	{
		return srm;
	}
	
	/** Play a sound effect.
	 * @param sfx the sound effect to play
	 */
	public static void playSFX(String sfx)
	{
		queueSFXEvent(new SFXEvent(sfx));
	}
	
	/** Play a sound effect, using the given sound effect event.
	 * @param sfx the event to use to play a sound effect
	 */
	public static void playSFX(SFXEvent sfx)
	{
		queueSFXEvent(sfx);
	}
	
	/** Change or set the background music track.
	 * @param track the track to play
	 * @param effect the transition effect for fading in the new track
	 */
	public static void playBGM(String track, BGMTransition effect)
	{
		queueGenEvent(new BGMEvent(track, effect));
	}
	
	/** Change or set the background music track, using the given background
	 * music event.
	 * @param bgm the event to use to change/set the background music track
	 */
	public static void playBGM(BGMEvent bgm)
	{
		queueGenEvent(bgm);
	}
	
	/** Stop the current BGM and remove any queued tracks, fades out the
	 * current track using the specified transition effect.
	 * @param effect the effect used to fade out the track
	 */
	public static void stopBGM(BGMTransition effect)
	{
		queueGenEvent(new StopBGMEvent(effect));
	}
	
	/** Immediately stop the current BGM and remove any queued tracks. */
	public static void stopBGM()
	{
		stopBGM(BGMTransition.IMMEDIATE);
	}
	
	/** Stop all sound effects and clear any queued ones. */
	public static void stopAllSFX()
	{
		queueGenEventUnlessPrev(new StopAllSFXEvent());
	}
	
	/** Add the specified event to the sound effect queue.
	 * @param e the main.sound.SFXEvent object
	 */
	private static synchronized void queueSFXEvent(SFXEvent e)
	{
		sfxQueue.add(e);
	}
	
	/** Add the specified event to the general queue.
	 * @param e the main.sound.BaseSoundEvent object
	 */
	private static synchronized void queueGenEvent(BaseSoundEvent e)
	{
		genQueue.add(e);
	}
	
	/** Add the specified event to the general queue, unless it is the same
	 * type as the previous event.
	 * @param e the main.sound.BaseSoundEvent object
	 */
	private static synchronized void queueGenEventUnlessPrev(BaseSoundEvent e)
	{
		try
		{
			// Check if the previous queued event is the same type
			if (genQueue.getLast().getClass().equals(e.getClass()))
			{
				return;
			}
		}
		// Queue is empty, so yes this event should still be queued
		catch (java.util.NoSuchElementException ex)
		{
		}
		// Queue the event
		queueGenEvent(e);
	}
	
	/** Play the specified sound effect.
	 * @param event the sound effect event
	 */
	private void doPlaySFX(SFXEvent event)
	{
		SFX newSFX = new SFX(event);
		newSFX.play();
		playingSFX.add(newSFX);
	}
	
	/** Actually play/change the BGM track.
	 * @param event the background music play event
	 */
	private void doPlayBGM(BGMEvent event)
	{
		if (currTrack != null)
		{
			currTrack.stop();
		}
		currTrack = new BGM(event);
	}
	
	/** Stops the current BGM and clears any queued ones.
	 * @param event the stop background music event
	 */
	private void doStopBGM(StopBGMEvent event)
	{
		currTrack.stop();
		currTrack = null;
	}
	
	/** Stops all currently playing sound effects and clears any queued
	 * ones.
	 * @param event the stop all sound effects event object
	 */
	private void doStopAllSFX(StopAllSFXEvent event)
	{
		sfxQueue.clear();
		for (SFX sfx : playingSFX)
		{
			sfx.stop();
			
		}
		playingSFX.clear();
	}
	
	/** Checks if the volume settings have been changed since they were last
	 * used to update the volumes of playing sounds.
	 * @return true if the dynamic volume settings have changed since the
	 * 		the last time they were used to update playing sounds' volumes
	 */
	@SuppressWarnings("unused")
	private boolean hasVolumeChanged()
	{
		int[] settings = {0,0,0};
		settings[0] = (int) core.DynamicSettings.getSetting(
				"MASTER_VOLUME"
				);
		settings[1] = (int) core.DynamicSettings.getSetting(
				"BGM_VOLUME"
				);
		settings[2] = (int) core.DynamicSettings.getSetting(
				"SFX_VOLUME"
				);
		if (
				settings[0] != currVolLvls[0]
				|| settings[1] != currVolLvls[1]
				|| settings[2] != currVolLvls[2]
			)
		{
			return true;
		}
		return false;
	}
}
