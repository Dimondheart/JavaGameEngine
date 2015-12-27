package core.sound;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.userinput.InputManager;

/** Manages the sound system.
* @author Bryan Bettis
*/
public class SoundManager implements core.CustomRunnable
{
	/** Thread for this object. */
	private Thread thread;
	/** Thread controller for this object. */
	private core.ThreadClock clock;
	/** Queue used only for sound effects. */
	private static volatile ConcurrentLinkedDeque<SFXEvent> sfxQueue;
	/** Queue used for general events. */
	private static volatile ConcurrentLinkedDeque<BaseSoundEvent> genQueue;
	/** Currently playing sound effects. */
	private static ConcurrentLinkedQueue<SFX> playingSFX;
	/** Queued BGM tracks. */
	private static BGM currTrack;
	/** The volume settings. */
	public Volume volume;
	
	/** Different ways to transition BGM. */
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
			// Get the next general event
			BaseSoundEvent nextGen = genQueue.poll();
			// Play the next sound effect
			if (nextSFX != null)
			{
				doPlaySFX(nextSFX);
			}
			// Do the next general event
			if (nextGen != null)
			{
				switch (nextGen.getType())
				{
					case CHANGE_VOLUME:
						doChangeVolume((VolumeEvent) nextGen);
						break;
					case PLAY_BGM:
						doPlayBGM((BGMEvent) nextGen);
						break;
					case STOP_BGM:
						doStopBGM((StopBGMEvent) nextGen);
						break;
					case PLAY_SFX:
						System.out.println(
								"WARNING: SFXEvent added to the general queue."
								);
						doPlaySFX((SFXEvent) nextGen);
						break;
					default:
						System.out.println(
								"Unrecognized SoundEvent Type: " +
										nextGen.getType()
								);
						break;
				
				}
			}
		}
	}
	
	/** Play a sound effect. */
	public static void playSFX(String sfx)
	{
		queueSFXEvent(new SFXEvent(sfx));
	}
	
	/** Change a volume setting. */
	public static void changeVolume(Volume.Setting setting, int newVolume)
	{
		queueGenEvent(new VolumeEvent(setting, newVolume));
	}
	
	/** Change/play a background music track. */
	public static void playBGM(String track, BGMTransition effect)
	{
		queueGenEvent(new BGMEvent(track, effect));
	}
	
	/** Stop the current BGM and remove any queued tracks, fades out the
	 * current track using the specified transition effect.
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
	
	/** Play the specified sound effect. */
	private void doPlaySFX(SFXEvent sfx)
	{
		playingSFX.add(new SFX(sfx.getSFX(), volume));
	}
	
	/** Actually change the volume setting. */
	private void doChangeVolume(VolumeEvent ve)
	{
		if (volume.getFinalVolume(ve.getSetting()) != ve.getNewVolume())
		{
			volume.setVolume(ve.getSetting(), ve.getNewVolume());
			for (SFX sfx : playingSFX)
			{
				sfx.adjustVolume();
			}
		}
	}
	
	/** Actually play/change the BGM track. */
	private void doPlayBGM(BGMEvent bgme)
	{
		if (currTrack != null)
		{
			currTrack.stop();
		}
		currTrack = new BGM(bgme.getBGM(), volume);
	}
	
	/** Stops the current BGM and clears any queued ones. */
	private void doStopBGM(StopBGMEvent sbgme)
	{
		currTrack.stop();
		currTrack = null;
	}
}
