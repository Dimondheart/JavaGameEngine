package core.sound;

/** Event used to change one of the volume settings.
 * @author Bryan Bettis
 */
class VolumeEvent extends BaseSoundEvent
{
	/** What volume setting is being changed. */
	private SoundManager.VolumeSetting setting;
	/** The new value for the volume level. */
	private int newVolume = 100;
	
	/** Basic constructor.
	 * @param setting the setting to change
	 * @param newVolume the new value of that setting
	 */
	public VolumeEvent(SoundManager.VolumeSetting setting, int newVolume)
	{
		this.setting = setting;
		this.newVolume = newVolume;
	}
	
	/** Gets the setting to change.
	 * @return the Volume.VolumeSetting to change
	 */
	public SoundManager.VolumeSetting getSetting()
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
