package main.sound;

import static main.sound.SoundManager.BGMTransition;

public class BGMEvent implements SoundEvent
{
	/** What type of sound event this is. */
	private Type type;
	/** The background music to play. */
	private String bgm;
	/** The transition effect into this track. */
	private BGMTransition effect;
	
	public BGMEvent(String bgm, BGMTransition effect)
	{
		type = Type.PLAY_BGM;
		this.bgm = bgm;
		this.effect = effect;
	}

	@Override
	public Type getType()
	{
		return type;
	}
	
	public String getBGM()
	{
		return bgm;
	}
	
	public BGMTransition getEffect()
	{
		return effect;
	}
}
