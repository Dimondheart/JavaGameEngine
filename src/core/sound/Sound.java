package core.sound;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/** A base class used to control the play-back of a sound.
 * @author Bryan Bettis
 */
abstract class Sound implements LineListener
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
	/** The name of this sound. */
	public String sound;
	/** Indicates when this sound is done playing. */
	private boolean isDone = true;
	
	/** Basic constructor.
	 * @param sound the relative path to the sound file
	 */
	public Sound(String sound)
	{
		// The sound to be played
		this.sound = sound;
		// Set done as true to start, and change it after ready to play
		isDone  = true;
		// Get an input stream for the sound effect
		InputStream is = SoundManager.getResManager().getRes(sound);
		// Get the audio input stream
		try
		{
			ais = AudioSystem.getAudioInputStream(is);
		}
		catch (UnsupportedAudioFileException e)
		{
			System.out.println("Unsupported audio file: " + sound);
			return;
		}
		catch (IOException e)
		{
			System.out.println("Error reading sound file: " + sound);
			return;
		}
		finally
		{
			try
			{
				if(ais == null)
				{
					is.close();
				}
			}
			catch (IOException e)
			{
			}
		}
		// Get an audio clip
		try
		{
			clip = AudioSystem.getClip();
		}
		catch (LineUnavailableException e)
		{
			System.out.println("Error acquiring audio line for: " + sound);
			return;
		}
		finally
		{
			try
			{
				if (clip == null)
				{
					ais.close();
				}
			}
			catch (IOException e)
			{
			}
		}
		// Add this Sound as a line listener for this clip
		clip.addLineListener(this);
		// Open the audio clip
		try
		{
			clip.open(ais);
		}
		catch (LineUnavailableException e)
		{
			System.out.println("Error opening audio line for: " + sound);
			return;
		}
		catch (IOException e)
		{
			System.out.println("Error reading audio data for: " + sound);
			return;
		}
		finally
		{
			if (!clip.isOpen())
			{
				cleanup();
			}
		}
		// Play the sound
		isDone = false;
		play();
	}
	
	/** Start playing this sound. */
	private synchronized void play()
	{
		// Start the clip
		clip.start();
	}
	
	/**  If this sound has finished playing.
	 * @return true if done playing
	 */
	public synchronized boolean isDone()
	{
		return isDone;
	}
	
	/** Adjust the volume of this sound. */
	public synchronized void adjustVolume()
	{
	}
	
	@Override
	public synchronized void update(LineEvent e)
	{
		if (e.getType() == LineEvent.Type.STOP)
		{
			cleanup();
		}
	}
	
	/** Releases system resources used by this sound. */
	private synchronized void cleanup()
	{
		clip.close();
		try
		{
			ais.close();
		}
		catch (IOException e)
		{
		}
		isDone = true;
	}
}
