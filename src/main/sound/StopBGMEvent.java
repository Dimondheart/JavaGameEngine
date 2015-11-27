package main.sound;

import main.sound.SoundManager.BGMTransition;

public class StopBGMEvent extends BaseSoundEvent
{
	private BGMTransition effect;
	
	public StopBGMEvent(BGMTransition effect)
	{
		super(Type.STOP_BGM);
		this.effect = effect;
	}
	
	public BGMTransition getEffect()
	{
		return effect;
	}
}
