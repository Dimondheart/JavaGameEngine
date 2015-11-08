package main.sound;

public class Volume
{
	private int master = 100;
	private int sfx = 100;
	private int bgm = 100;
	
	public enum Setting
	{
		MASTER,
		SFX,
		BGM
	}
	
	public void setVolume(Setting setting, int value)
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
	
	public int getVolume(Setting setting)
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
				return 100;
		}
	}
	public int getFinalVolume(Setting setting)
	{
		int base;
		switch (setting)
		{
			case MASTER:
				return master;
			case SFX:
				base = sfx;
				break;
			case BGM:
				base = bgm;
				break;
			default:
				return 100;
		}
		
		base *= ((double)master / 100.0);
		return base;
	}
}
