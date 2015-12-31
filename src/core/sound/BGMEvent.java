package core.sound;

import static core.sound.SoundManager.BGMTransition;

/** An event used to change/transition the playing background music track.
* @author Bryan Bettis
*/
class BGMEvent extends BaseSoundEvent
{
	/** The background music to play. */
	private String bgm;
	/** The transition effect into this track. */
	private BGMTransition effect;
	
	/** Basic constructor.
	 * @param bgm the name of the file
	 * @param effect the effect used to transition in the new track
	 */
	public BGMEvent(String bgm, BGMTransition effect)
	{
		this.bgm = bgm;
		this.effect = effect;
	}
	
	/** Get the track to play.
	 * @return the name of the file
	 */
	public String getBGM()
	{
		return bgm;
	}
	
	/** Get the effect used to transition in this track.
	 * @return the transition effect
	 */
	public BGMTransition getEffect()
	{
		return effect;
	}
}
