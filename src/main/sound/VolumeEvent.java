package main.sound;

/** Event used to change one of the volume settings. */
public class VolumeEvent implements SoundEvent
{
	/** What type of sound event this is. */
	private Type type;
	/** What volume setting is being changed. */
	private Volume.Setting setting;
	/** The new value for the volume level. */
	private int newVolume = 100;
	
	public VolumeEvent(Volume.Setting setting, int newVolume)
	{
		type = Type.CHANGE_VOLUME;
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
	
	@Override
	public Type getType()
	{
		return type;
	}
}
