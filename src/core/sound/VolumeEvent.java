package core.sound;

/** Event used to change one of the volume settings.
 * @author Bryan Bettis
 */
class VolumeEvent extends BaseSoundEvent
{
	/** What volume setting is being changed. */
	private Volume.VolumeSetting setting;
	/** The new value for the volume level. */
	private int newVolume = 100;
	
	/** Basic constructor.
	 * @param setting the setting to change
	 * @param newVolume the new value of that setting
	 */
	public VolumeEvent(Volume.VolumeSetting setting, int newVolume)
	{
		super(Type.CHANGE_VOLUME);
		this.setting = setting;
		this.newVolume = newVolume;
	}
	
	/** Gets the setting to change.
	 * @return the Volume.VolumeSetting to change
	 */
	public Volume.VolumeSetting getSetting()
	{
		return setting;
	}
	
	/** Gets the new volume level.
	 * @return the new volume value
	 */
	public int getNewVolume()
	{
		return newVolume;
	}
}
