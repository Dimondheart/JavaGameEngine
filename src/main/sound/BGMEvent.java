package main.sound;

import static main.sound.SoundManager.BGMTransition;

public class BGMEvent extends BaseSoundEvent
{
	/** The background music to play. */
	private String bgm;
	/** The transition effect into this track. */
	private BGMTransition effect;
	
	/** Basic constructor. */
	public BGMEvent(String bgm, BGMTransition effect)
	{
		super(Type.PLAY_BGM);
		this.bgm = bgm;
		this.effect = effect;
	}
	
	/** Get the track to play. */
	public String getBGM()
	{
		return bgm;
	}
	
	/** Get the effect used to transition in this track. */
	public BGMTransition getEffect()
	{
		return effect;
	}
}
