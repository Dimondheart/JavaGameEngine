package main.sound;

import javax.sound.sampled.Clip;

public class BGM extends Sound
{
	public BGM(String track, Volume volume)
	{
		super("bgm/" + track, volume);
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/** Stops this track from playing. */
	public void stop()
	{
		this.clip.stop();
	}
}
