package main.sound;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.sound.Volume.Setting;

/** A base class used to control the play-back of a sound.
 * @author Bryan Bettis
 */
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
	/** The time at which this sound started playing. */
	private long start;
	/** The length of this sound's clip in milliseconds. */
	private long clipLength;
	
	public Sound(String sound, Volume volume)
	{
		this.volume = volume;
		// Get an input stream for the sound effect
		InputStream is = this.getClass().getResourceAsStream(
				"/game/resources/" + sound + ".wav"
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
		start = main.ProgramTimer.getTime();
		try
		{
			clip.start();
			clipLength = clip.getMicrosecondLength() / 1000;
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
		long elapsed = main.ProgramTimer.getTime() - start;
		if ( elapsed >= clipLength*1.5)
		{
			cleanup();
			return true;
		}
		return false;
	}
	
	/** Adjust the volume of this sound. */
	public void adjustVolume()
	{
		// Convert the units of the volume
		float vol = (float)(100 - volume.getFinalVolume(Setting.SFX))/100.0f * -80.0f;
		volumeCtrl.setValue(vol);
	}
	
	private void cleanup()
	{
		clip.stop();
		clip.flush();
		clip.close();
	}
}
