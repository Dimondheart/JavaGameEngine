package core.sound;

/** An event used to play a sound effect.
* @author Bryan Bettis
*/
class SFXEvent extends BaseSoundEvent
{
	/** The sound effect to play. */
	private String sfx;
	
	/** Basic constructor.
	 * @param sfx the name of the file
	 */
	public SFXEvent(String sfx)
	{
		super(Type.PLAY_SFX);
		this.sfx = sfx;
	}
	
	/** Get the sound effect to play.
	 * @return the name of the file
	 */
	public String getSFX()
	{
		return sfx;
	}
}
