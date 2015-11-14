package main.sound;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SoundManager implements main.CustomRunnable
{
	/** Thread for this object. */
	private Thread thread;
	/** Thread controller for this object. */
	private main.ThreadClock clock;
	/** Queue used only for sound effects. */
	private static volatile ConcurrentLinkedDeque<SFXEvent> sfxQueue;
	/** Queue used for general events. */
	private static volatile ConcurrentLinkedDeque<SoundEvent> genQueue;
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
		// Setup queues
		sfxQueue = new ConcurrentLinkedDeque<SFXEvent>();
		genQueue = new ConcurrentLinkedDeque<SoundEvent>();
		playingSFX = new ConcurrentLinkedQueue<SFX>();
		volume = new Volume();
		// Setup thread controller
		clock = new main.ThreadClock(10);
	}
	
	@Override
	public void start()
	{
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		while (true)
		{
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
			SoundEvent nextGen = genQueue.poll();
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
			clock.nextCycle();
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
	private static synchronized void queueGenEvent(SoundEvent e)
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
}
