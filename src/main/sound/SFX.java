package main.sound;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.sound.Volume.Setting;

public class SFX
{
	private AudioInputStream ais;
	private Clip clip;
	private File sfx;
	private volatile Volume volume;
	private boolean started = false;
	private FloatControl volumeCtrl;
	
	public SFX(String sfx, Volume volume)
	{
		this.sfx = (new File("C:/Users/Bryan/Documents/GitHub/GameFrameworkRevamp/src/main/sound/sfx/" + sfx + ".wav")).getAbsoluteFile();
		this.volume = volume;
		try
		{
			ais = AudioSystem.getAudioInputStream(this.sfx);
			clip = AudioSystem.getClip();
			clip.open(ais);
			// Get the volume controller
			volumeCtrl = 
					(FloatControl) clip.getControl(
							FloatControl.Type.MASTER_GAIN
							);
		}
		catch(Exception e)
		{
			System.out.println("Unable to play sound");
			e.printStackTrace();
		}
		adjustVolume();
		play();
	}
	
	/** Start playing this sound. */
	private void play()
	{
		try
		{
			clip.start();
		}
		catch(Exception e)
		{
			System.out.println("Unable to play sound");
			e.printStackTrace();
		}
	}
	
	/**  If this sound has finished playing. */
	public boolean isDone()
	{
		if (!started)
		{
			if (clip.isRunning())
			{
				started = true;
			}
			return false;
		}
		return !clip.isRunning();
	}
	
	/** Adjust the volume of this sound. */
	public void adjustVolume()
	{
		// Convert the units of the volume
		float vol = (float)(100 - volume.getFinalVolume(Setting.SFX))/100.0f * -80.0f;
		volumeCtrl.setValue(vol);
	}
}
