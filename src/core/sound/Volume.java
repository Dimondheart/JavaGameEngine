package core.sound;

/** Handles volume settings.
* @author Bryan Bettis
*/
public class Volume
{
	private int master = 100;
	private int sfx = 100;
	private int bgm = 100;
	
	public enum VolumeSetting
	{
		MASTER,
		SFX,
		BGM
	}
	
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
