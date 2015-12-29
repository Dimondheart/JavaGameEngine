package core.sound;

import javax.sound.sampled.Clip;

/** Handles play-back of a background music track.
 * @author Bryan Bettis
 */
class BGM extends Sound
{
	/** Basic constructor.
	 * @param event the event object for this track
	 */
	public BGM(BGMEvent event)
	{
		super("bgm/" + event.getBGM());
		// Resume the track with the proper loop settings
		resume();
	}
	
	/** Stops this track from playing. */
	public void stop()
	{
		clip.stop();
	}
	
	/** Resumes the track. */
	public void resume()
	{
		// Set the track to loop
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		// Reset the loop segment back to the whole track
		clip.setLoopPoints(0, -1);
	}
}
