package main.sound;

import main.sound.SoundManager.BGMTransition;

/** A simple event used to stop or fade out the current background music
 * track.
 * @author Bryan Bettis
 */
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
