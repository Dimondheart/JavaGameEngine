package core.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import core.graphics.GfxManager;

/** Handles animations using images as frames.
 * @author Bryan Bettis
 */
public abstract class Animator implements PrimaryRenderer
{
	/** The directory all animation sets are located in. */
	private String location;
	/** The current set of animation frames being drawn. */
	private String currentSet;
	/** The interval at which the animation frame changes. */
	private long interval;
	/** The current frame of the animation. */
	private int currFrame;
	/** The path to the current frame image. */
	private String currFramePath;
	/** The start time of the current frame. */
	private long frameStart;
	
	/** Defaults to the "basic" animation set.
	 * @param location the root folder for all the animation sets
	 */
	public Animator(String location)
	{
		this(location, "basic");
	}
	
	/** Set an initial animation set other than the default "basic".
	 * @param location the root folder for all the animation sets
	 * @param startingSet the frame set to start with
	 */
	public Animator(String location, String startingSet)
	{
		this.location = location;
		setAnimationSet(startingSet);
		setInterval(50);
		setFrame(1);
	}
	
	/** Render the current frame of this animation at the specified
	 * coordinates.
	 * @param g the surface to draw to
	 * @param x the x coordinate to draw at
	 * @param y the y coordinate to draw at
	 */
	protected void renderAnimation(Graphics2D g, int x, int y)
	{
		// Update the current frame
		selectFrame();
		// Get the actual image
		BufferedImage img =
				GfxManager.getResManager().getRes(currFramePath);
		// Draw the current frame
		GfxManager.drawGraphic(g, img, x, y, img.getWidth(), img.getHeight());
	}
	
	/** Changes the animation set to use for this animator.
	 * An "animator set" is a set of frames of an animation in a
	 * sub-folder of where all the animation's frames are stored. For example
	 * you might have separate animation sets for when a character is walking
	 * verses when it is jumping.
	 * @param setName the name of the new animation set
	 */
	protected void setAnimationSet(String setName)
	{
		currentSet = setName;
	}
	
	/** Set how many milliseconds should elapse between each animation
	 * frame.
	 * @param interval the number of milliseconds between frame changes
	 */
	protected void setInterval(int interval)
	{
		this.interval = (long) interval;
	}
	
	/** Set what frame number should render next. This updates on its own,
	 * so no need to worry about it unless you want to adjust the current
	 * animation (e.g. when you change animation sets).
	 * @param frame the integer portion of the frame name
	 */
	protected void setFrame(int frame)
	{
		currFrame = frame;
		frameStart = core.ProgramTimer.getTime();
	}
	
	/** Selects and updates what the next rendered frame is. */
	private void selectFrame()
	{
		// Increment the animation frame
		if (core.ProgramTimer.getTime() - frameStart >= interval)
		{
			setFrame(currFrame+1);
		}
		// Get the path to the current frame's image
		setFramePath();
		// If the image frame doesn't exist, reset to the beginning
		if (!GfxManager.getResManager().resExists(currFramePath))
		{
			setFrame(1);
			setFramePath();
		}
	}
	
	/** Sets the string representing the relative path to the
	 * image file of the current frame.
	 */
	private void setFramePath()
	{
		currFramePath = location + "/" + currentSet + "/" + "frame"
				+ Integer.toString(currFrame) + ".png";
	}
}
