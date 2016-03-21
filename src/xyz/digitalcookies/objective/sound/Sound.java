/** Copyright 2016 Bryan Charles Bettis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.digitalcookies.objective.sound;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/** A base class used to control the play-back of a sound.
 * @author Bryan Charles Bettis
 */
class Sound implements Runnable
{
	/** The audio stream for this sound. */
	protected AudioInputStream ais;
	/** The audio line this sound is playing to. */
	protected SourceDataLine line;
	/** The resource name of this sound. */
	public String sound;
	/** Indicates when this sound is done playing. */
	private boolean isDone;
	private boolean paused;
	private boolean looping;
	private boolean allDataBuffered;
	private Thread thread;
	
	/** Basic constructor.
	 * @param sound the relative path to the sound file
	 */
	public Sound(String sound)
	{
		// The sound to be played
		this.sound = sound;
		isDone  = false;
		allDataBuffered = false;
		paused = false;
		looping = false;
		// Get an input stream for the sound effect
		InputStream is = SoundManager.getResManager().getRes(sound);
		// Get the audio input stream
		setAIS(is);
		if (ais == null)
		{
			return;
		}
		// Get a source data line to play to
		try
		{

			line = AudioSystem.getSourceDataLine(ais.getFormat());
		}
		catch (LineUnavailableException e)
		{
			System.out.println("Error acquiring audio line for: " + sound);
			isDone  = true;
			return;
		}
		finally
		{
			try
			{
				if (line == null)
				{
					ais.close();
				}
			}
			catch (IOException e)
			{
			}
		}
		// Open the line
		try
		{
			line.open();
		}
		catch (LineUnavailableException e)
		{
			System.out.println("Error opening audio line for: " + sound);
			isDone  = true;
			return;
		}
		finally
		{
			if (!line.isOpen())
			{
				cleanup();
			}
		}
		// Setup the dameon thread
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.setName("Playing Sound Thread");
		thread.start();
	}
	
	@Override
	public void run()
	{
		byte[] toBuffer = new byte[ais.getFormat().getFrameSize()*800];
		line.start();
		while (true)
		{
			if (allDataBuffered)
			{
				if (!line.isActive())
				{
					cleanup();
					break;
				}
			}
			// Otherwise try to buffer more data
			else if (!paused)
			{
				try
				{
					int bRead = ais.read(toBuffer, 0, toBuffer.length);
					if (bRead < toBuffer.length)
					{
						if (looping)
						{
							setAIS(SoundManager.getResManager().getRes(sound));
						}
						else
						{
							allDataBuffered = true;
						}
					}
					if (bRead > 0)
					{
						line.write(toBuffer, 0, bRead);
					}
				}
				catch (IOException e)
				{
					System.out.println("Error reading audio data for: " + sound);
					cleanup();
					break;
				}
			}
			// buffer data here
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	/**  If this sound has finished playing.
	 * @return true if done playing
	 */
	public boolean isDone()
	{
		return isDone;
	}
	
	/** Completely stops this sound effect. After calling this method, the
	 * current sound effect cannot be restarted.
	 */
	public void stop()
	{
		line.flush();
		cleanup();
	}
	
	public void pause()
	{
		paused = true;
	}
	
	public void resume()
	{
		paused = false;
	}
	
	public void setLooping(boolean doLoop)
	{
		looping = doLoop;
	}
	
	public void setVolume(int percent)
	{
		FloatControl vc = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
		float tnr = (float)(100-percent)/100.0f*vc.getMinimum();
		vc.setValue(tnr);
	}
	
	/** Releases system resources used by this sound. */
	private void cleanup()
	{
		line.close();
		try
		{
			ais.close();
		}
		catch (IOException e)
		{
		}
		isDone = true;
	}
	
	private void setAIS(InputStream is)
	{
		if (ais != null)
		{
			try
			{
				ais.close();
			}
			catch (IOException e)
			{
			}
		}
		try
		{
			ais = AudioSystem.getAudioInputStream(is);
		}
		catch (UnsupportedAudioFileException e)
		{
			System.out.println("Unsupported audio file: " + sound);
			isDone  = true;
			return;
		}
		catch (IOException e)
		{
			System.out.println("Error reading sound file: " + sound);
			isDone  = true;
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
	}
}
