package core.sound;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.userinput.InputManager;

/** Manages the sound system.
* @author Bryan Bettis
*/
public class SoundManager implements core.CustomRunnable
{
	/** Maximum number of sound effects to play at once. */
	private static final int MAX_PLAYING_SFX = 25;
	/** How long a sound effect can be queued before it is canceled. */
	private static final long MAX_SFX_QUEUE_TIME = 250;
	/** Thread for this object. */
	private Thread thread;
	/** Thread controller for this object. */
	private core.ThreadClock clock;
	/** Manages sound data loaded from files. */
	private static SoundResources srm;
	/** Queue used only for sound effects. */
	private static volatile ConcurrentLinkedDeque<SFXEvent> sfxQueue;
	/** Queue used for general events. */
	private static volatile ConcurrentLinkedDeque<BaseSoundEvent> genQueue;
	/** Currently playing sound effects. */
	private static ConcurrentLinkedQueue<SFX> playingSFX;
	/** Queued BGM tracks. */
	private static BGM currTrack;
	/** The sound volume settings. */
	public static Volume volume;
	
	/** Different ways to transition BGM.  Currently only IMMEDIATE is
	 * implemented.
	 * @author Bryan Bettis
	 */
	public enum BGMTransition
	{
		IMMEDIATE,  // Stop the previous track and start the new track
		SMOOTH,  // Fade out old track then start new track (WIP)
		CROSSOVER  // Fade out old track as the new one is faded in (WIP)
	}
	
	/** Normal sound manager setup. */
	public SoundManager()
	{
		System.out.println("Setting Up Sound System...");
		srm = new SoundResources();
		// Setup queues
		sfxQueue = new ConcurrentLinkedDeque<SFXEvent>();
		genQueue = new ConcurrentLinkedDeque<BaseSoundEvent>();
		playingSFX = new ConcurrentLinkedQueue<SFX>();
		volume = new Volume();
		// Setup thread controller
		clock = new core.ThreadClock(10);
	}
	
	@Override
	public void start()
	{
		System.out.println("Starting Sound System...");
		thread = new Thread(this);
		thread.setName("Sound Manager Event Queue");
		thread.start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			clock.nextCycle();
			// Pause/resume the current BGM based on program state
			if (currTrack != null)
			{
				if (InputManager.getState() == InputManager.State.PAUSED)
				{
					currTrack.stop();
					continue;
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
			// Get the next sound effect
			SFXEvent nextSFX = sfxQueue.poll();
			// Play the next sound effect
			if (nextSFX != null)
			{
				doPlaySFX(nextSFX);
			}
			// Get the next general event
			BaseSoundEvent nextGen = genQueue.poll();
			// Do the next general event
			if (nextGen != null)
			{
				// Get the class of the next event
				Class<? extends BaseSoundEvent> c = nextGen.getClass();
				// Change BGM
				if (c == BGMEvent.class)
				{
					doPlayBGM((BGMEvent) nextGen);
				}
				// Change volume settings
				else if (c == VolumeEvent.class)
				{
					doChangeVolume((VolumeEvent) nextGen);
				}
				// Stop current BGM
				else if (c == StopBGMEvent.class)
				{
					doStopBGM((StopBGMEvent) nextGen);
				}
				// Unknown/unused general event
				else
				{
					System.out.println(
							"Non-General event on the general sound event queue: "
									+ c
							);
				}
			}
		}
	}
	
	/** TODO fill this out
	 * @return TODO fill this
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
	
	/** Change a volume setting.
	 * @param setting the volume setting to change
	 * @param newVolume the new volume setting
	 */
	public static void changeVolume(Volume.VolumeSetting setting, int newVolume)
	{
		queueGenEvent(new VolumeEvent(setting, newVolume));
	}
	
	/** Change/play a background music track.
	 * @param track the track to play
	 * @param effect the transition effect for fading in the new track
	 */
	public static void playBGM(String track, BGMTransition effect)
	{
		queueGenEvent(new BGMEvent(track, effect));
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
	
	/** Add the specified event to the sound effect queue.
	 * @param e the main.sound.SFXEvent object
	 */
	private static synchronized void queueSFXEvent(SFXEvent e)
	{
		sfxQueue.add(e);
	}
	
	/** Add the specified event to the general queue.
	 * @param e the main.sound.SoundEvent object
	 */
	private static synchronized void queueGenEvent(BaseSoundEvent e)
	{
		genQueue.add(e);
	}
	
	/** Play the specified sound effect.
	 * @param sfx the sound effect event
	 */
	private void doPlaySFX(SFXEvent sfx)
	{
		playingSFX.add(new SFX(sfx));
	}
	
	/** Actually change the volume setting.
	 * @param ve the volume change event
	 */
	private void doChangeVolume(VolumeEvent ve)
	{
		if (volume.getVolume(ve.getSetting()) != ve.getNewVolume())
		{
			volume.setVolume(ve.getSetting(), ve.getNewVolume());
			for (SFX sfx : playingSFX)
			{
				sfx.adjustVolume();
			}
		}
	}
	
	/** Actually play/change the BGM track.
	 * @param bgme the background music play event
	 */
	private void doPlayBGM(BGMEvent bgme)
	{
		if (currTrack != null)
		{
			currTrack.stop();
		}
		currTrack = new BGM(bgme);
	}
	
	/** Stops the current BGM and clears any queued ones.
	 * @param sbgme the stop background music event
	 */
	private void doStopBGM(StopBGMEvent sbgme)
	{
		currTrack.stop();
		currTrack = null;
	}
}
