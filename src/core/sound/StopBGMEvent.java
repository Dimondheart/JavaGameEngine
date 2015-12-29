package core.sound;

import core.sound.SoundManager.BGMTransition;

/** A simple event used to stop or fade out the current background music
 * track.
 * @author Bryan Bettis
 */
class StopBGMEvent extends BaseSoundEvent
{
	/** The effect used to fade out the track. */
	private BGMTransition effect;
	
	/** Basic constructor.
	 * @param effect the effect used to fade out the track
	 */
	public StopBGMEvent(BGMTransition effect)
	{
		super(Type.STOP_BGM);
		this.effect = effect;
	}
	
	/** Get the fade out effect.
	 * @return the transition effect for fading out the track
	 */
	public BGMTransition getEffect()
	{
		return effect;
	}
}
