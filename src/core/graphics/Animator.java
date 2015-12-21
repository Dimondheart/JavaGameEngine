package core.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

/** Handles cyclic/looped animations using frames.
 * @author Bryan Bettis
 */
public abstract class Animator implements Renderer
{
	/** The directory all of an instances images are located in. */
	private String location;
	/** The current animation path used to draw to (directly mapped to
	 * sub-directories).
	 */
	private String currPath;
	/** The interval at which the animation frame changes. */
	private long interval;
	/** The current frame of the animation. */
	private int currFrame = 0;
	/** The start time of the current frame. */
	private long frameStart;
	
	/** Constructor, uses the 'default' animation path. */
	public Animator(int layer, String location)
	{
		this(layer, location, "default");
	}
	
	/** Constructor which takes a non-'default' animation path. */
	public Animator(int layer, String location, String startPath)
	{
		this.show(layer);
		this.location = location;
		setAnimationPath(startPath);
		setInterval(100);
	}
	
	/** Selects and sets what the next rendered frame is. */
	private void autoSelectFrame()
	{
		// Increment the animation frame
		if (core.ProgramTimer.getTime() - frameStart >= interval)
		{
			setFrame(currFrame+1);
		}
		// End of animation, reset to beginning
		String framePath = location + currPath + "frame"
				+ Integer.toString(currFrame) + ".png";
//		if (main.graphics.GfxManager.getResManager().resExists(framePath))
		if (currFrame >= 4)
		{
			setFrame(0);
		}
	}
	
	/** Render the current frame of this animation at the specified
	 * coordinates.
	 * @param g the surface to draw to
	 * @param x the centered x coordinate to draw at
	 * @param y the centered y coordinate to draw at
	 */
	protected void renderAnimation(Graphics2D g, int x, int y)
	{
		autoSelectFrame();
		// Test animation
		switch (currFrame)
		{
			case 0:
				g.setColor(Color.blue);
				break;
			case 1:
				g.setColor(Color.cyan);
				break;
			case 2:
				g.setColor(Color.lightGray);
				break;
			case 3:
				g.setColor(Color.gray);
				break;
			default:
				break;
		}
		g.fillRect(x-25, y-25, 50, 50);
	}
	
	/** Changes the animation path to use for this animator.
	 * An "animator path" is a set of frames of an animation in the same
	 * sub-folder of the root folder of a set of animation frames. For example
	 * you might have separate animation paths for when a character is walking
	 * and when it is jumping.
	 */
	protected void setAnimationPath(String pathName)
	{
		currPath = pathName;
	}
	
	/** Set how many milliseconds should elapse between each animation
	 * frame.
	 * @param interval the number of milliseconds between frame changes
	 */
	protected void setInterval(int interval)
	{
		this.interval = (long) interval;
	}
	
	/** Change the current frame to render of this animation.
	 * @param frame the integer portion of the frame name (0,1,2,...)
	 */
	protected void setFrame(int frame)
	{
		currFrame = frame;
		frameStart = core.ProgramTimer.getTime();
	}
}
