package main.sound;

public class BGM extends Sound
{
	public BGM(String track, Volume volume)
	{
		super("bgm/" + track, volume);
	}
	
	/** Stops this track from playing. */
	public void stop()
	{
		this.clip.stop();
	}
}
