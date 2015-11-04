package main.sound;

import java.util.concurrent.ConcurrentLinkedDeque;

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
	
	/** The different volume settings. */
	public enum VolumeSetting
	{
		MASTER,
		SFX,
		BGM
	}
	
	/** Different ways to transition BGM. */
	public enum BGMTransition
	{
		IMMEDIATE,
		SMOOTH
	}
	
	/** Normal sound manager setup. */
	public SoundManager()
	{
		// Setup queues
		sfxQueue = new ConcurrentLinkedDeque<SFXEvent>();
		genQueue = new ConcurrentLinkedDeque<SoundEvent>();
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
	public static void changeVolume(VolumeSetting setting, int newVolume)
	{
		queueGenEvent(new VolumeEvent(setting, newVolume));
	}
	
	/** Change/play a background music track. */
	public static void playBGM(String sfx, BGMTransition effect)
	{
		queueGenEvent(new BGMEvent(sfx, effect));
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
		// TODO Implement
		System.out.println("Playing SFX...");
	}
	
	/** Actually change the volume setting. */
	private void doChangeVolume(VolumeEvent ve)
	{
		// TODO Implement
		System.out.println("Changing Volume Setting...");
	}
	
	/** Actually play/change the BGM track. */
	private void doPlayBGM(BGMEvent bgme)
	{
		// TODO Implement
		System.out.println("Changing BGM...");
	}
}
