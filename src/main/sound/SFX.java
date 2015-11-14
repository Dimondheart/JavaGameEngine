package main.sound;

public class SFX extends Sound
{
	public SFX(String sfx, Volume volume)
	{
		super("sfx/" + sfx, volume);
	}
}
