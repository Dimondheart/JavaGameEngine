package core.sound;

/** Event used to change one of the volume settings.
 * @author Bryan Bettis
 */
public class VolumeEvent extends BaseSoundEvent
{
	/** What volume setting is being changed. */
	private Volume.Setting setting;
	/** The new value for the volume level. */
	private int newVolume = 100;
	
	public VolumeEvent(Volume.Setting setting, int newVolume)
	{
		super(Type.CHANGE_VOLUME);
		this.setting = setting;
		this.newVolume = newVolume;
	}
	
	public Volume.Setting getSetting()
	{
		return setting;
	}
	
	public int getNewVolume()
	{
		return newVolume;
	}
}
