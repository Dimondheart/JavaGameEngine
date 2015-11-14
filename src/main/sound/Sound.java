package main.sound;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.sound.Volume.Setting;

public abstract class Sound
{
	/** The audio stream for this sound. */
	protected AudioInputStream ais;
	/** The clip for this sound. */
	protected Clip clip;
	/** The volume settings this sound will use. */
	protected volatile Volume volume;
	/** If this sound has started playing yet. */
	protected boolean started = false;
	/** The volume control for this sound. */
	protected FloatControl volumeCtrl;
	
	public Sound(String sound, Volume volume)
	{
		this.volume = volume;
		// Get an input stream for the sound effect
		InputStream is = this.getClass().getResourceAsStream(
				"/main/sound/" + sound + ".wav"
				);
		// Must be converted to a buffered input stream to work is a JAR
		is = new BufferedInputStream(is);
		try
		{
			// Setup the sound objects to be used for playing
			ais = AudioSystem.getAudioInputStream(is);
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
