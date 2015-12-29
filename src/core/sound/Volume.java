package core.sound;

/** Stores volume settings.
* @author Bryan Bettis
*/
public class Volume
{
	/** The master volume for all sounds. */
	private int master = 100;
	/** The volume of sound effects. */
	private int sfx = 100;
	/** The volume of background music. */
	private int bgm = 100;
	
	/** The different volume settings.
	 * @author Bryan Bettis
	 */
	public enum VolumeSetting
	{
		MASTER,
		SFX,
		BGM
	}
	
	/** Adjust a volume setting.
	 * @param setting the volume setting to adjust
	 * @param value the new volume value
	 */
	public void setVolume(VolumeSetting setting, int value)
	{
		switch (setting)
		{
			case MASTER:
				master = value;
				break;
			case SFX:
				sfx = value;
				break;
			case BGM:
				bgm = value;
				break;
			default:
				break;
		}
	}
	
	/** Gets a current volume setting.
	 * @param setting the volume setting to get
	 * @return the value of the specified volume setting
	 */
	public int getVolume(VolumeSetting setting)
	{
		switch (setting)
		{
			case MASTER:
				return master;
			case SFX:
				return sfx;
			case BGM:
				return bgm;
			default:
				return 0;
		}
	}
}
