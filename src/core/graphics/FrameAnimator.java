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

package core.graphics;

import java.awt.image.BufferedImage;

import core.graphics.GfxManager;

/** Handles animations using images as frames.
 * @author Bryan Charles Bettis
 */
public class FrameAnimator extends Animator
{
	/** Indicates that a dimension of an animation image should be scaled
	 * to maintain the original aspect ratio of the image.
	 */
	public static final int SCALE_DIMENSION = -1;
	/** Indicates that a dimension of an animation image should not be
	 * altered, instead using the actual respective dimension of the original
	 * image.
	 */
	public static final int ORIGINAL_DIMENSION = -2;
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
	/** If rendered images should be centered over the specified coordinates
	 * when rendering the animation.
	 */
	private boolean centerOverCoords;
	/** The width to draw the animation images. */
	private int imgWidth;
	/** The height to draw the animation images. */
	private int imgHeight;
	
	/** Defaults to the "basic" animation set.
	 * @param location the root folder containing animation sets
	 */
	public FrameAnimator(String location)
	{
		this(location, "basic");
	}
	
	/** Takes an argument for the location of the animation sets and the
	 * interval for changing frames.
	 * @param location the root folder containing animation sets
	 * @param interval how often to switch to the next frame
	 */
	public FrameAnimator(String location, int interval)
	{
		this(location, "basic", interval);
	}
	
	/** Set an initial animation set other than the default "basic".
	 * @param location the root folder containing animation sets
	 * @param startingSet the animation set to start with
	 */
	public FrameAnimator(String location, String startingSet)
	{
		this(location, startingSet, 50);
	}
	
	/** Set an initial animation set other than the default "basic".
	 * @param location the root folder for all the animation sets
	 * @param startingSet the animation set to start with
	 * @param interval how often to switch to the next frame
	 */
	public FrameAnimator(String location, String startingSet, int interval)
	{
		this.location = location;
		setAnimationSet(startingSet);
		setInterval(interval);
		setFrame(1);
		setCenterOverCoords(true);
		setImageSize(SCALE_DIMENSION, SCALE_DIMENSION);
	}
	
	/** Render the current frame of this animation at the specified
	 * coordinates.
	 * @param event the RenderEvent containing the graphics context to draw to
	 * @param x the x coordinate to draw at
	 * @param y the y coordinate to draw at
	 */
	@Override
	public void renderAnimation(RenderEvent event, int x, int y)
	{
		// Update the current frame
		selectFrame();
		if (isFinished)
		{
			return;
		}
		// Get the actual image
		BufferedImage img =
				GfxManager.getResManager().getRes(currFramePath);
//		String theCase = "1";
		int imgWidth = this.imgWidth;
		int imgHeight = this.imgHeight;
		// Width needs to be scaled
		if (imgWidth == SCALE_DIMENSION)
		{
			// Use both original image dimensions
			if (imgHeight == SCALE_DIMENSION || imgHeight == ORIGINAL_DIMENSION)
			{
//				theCase = "8,9";
				imgWidth = img.getWidth();
				imgHeight = img.getHeight();
			}
			// Scale only the width
			else
			{
//				theCase = "7";
				// TODO scale dimension
				imgWidth = 0;
			}
		}
		// Height needs to be scaled
		else if (imgHeight == SCALE_DIMENSION)
		{
			// Use both original image dimensions
			if (imgWidth == ORIGINAL_DIMENSION)
			{
				
//				theCase = "6";
				imgWidth = img.getWidth();
				imgHeight = img.getHeight();
			}
			// Scale only the height
			else
			{
//				theCase = "3";
				// TODO scale dimension
				imgHeight = 0;
			}
		}
		// Use the original image width
		else if (imgWidth == ORIGINAL_DIMENSION)
		{
//			theCase = "4";
			imgWidth = img.getWidth();
			// Use the original image height
			if (imgHeight == ORIGINAL_DIMENSION)
			{
//				theCase = "5";
				imgHeight = img.getHeight();
			}
		}
		// Use the original image height
		else if (imgHeight == ORIGINAL_DIMENSION)
		{
//			theCase = "2";
			imgHeight = img.getHeight();
		}
//		System.out.println("FrAnim case " + theCase);
		// Reset back to actual image size for testing
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		// Draw the current frame
		if (centerOverCoords)
		{
			GfxManager.drawGraphic(
					event.getContext(),
					img,
					x-imgWidth/2,
					y-imgHeight/2,
					imgWidth,
					imgHeight
					);
		}
		else
		{
			GfxManager.drawGraphic(
					event.getContext(),
					img,
					x,
					y,
					imgWidth,
					imgHeight
					);
		}
	}
	
	/** Changes the animation set to use for this animator.
	 * An "animator set" is a set of frames of an animation in a
	 * sub-folder of where all the animation's frames are stored. For example
	 * you might have separate animation sets for when a character is walking
	 * verses when it is jumping.
	 * @param setName the name of the new animation set
	 */
	public void setAnimationSet(String setName)
	{
		currentSet = setName;
	}
	
	/** Set how many milliseconds should elapse between each animation
	 * frame.
	 * @param interval the number of milliseconds between frame changes
	 */
	public void setInterval(int interval)
	{
		this.interval = (long) interval;
	}
	
	/** Set the dimensions at which to draw the animation images. FrameAnimator
	 * contains several constants for special dimension situations such as 
	 * scaling a dimension to maintain the original aspect ratio.
	 * @param width the width to draw the image at
	 * @param height the height to draw the image at
	 */
	public void setImageSize(int width, int height)
	{
		setImageWidth(width);
		setImageHeight(height);
	}
	
	/** Get the width that this animation will draw its images with. There
	 * are several constants contained in FrameAnimator that this may be
	 * equal to.
	 * @return the width images of this animation will be drawn at
	 */
	public int getImageWidth()
	{
		return imgWidth;
	}
	
	/** Get the height that this animation will draw its images with. There
	 * are several constants contained in FrameAnimator that this may be
	 * equal to.
	 * @return the height images of this animation will be drawn at
	 */
	public int getImageHeight()
	{
		return imgHeight;
	}
	
	/** Set the width to draw the animation images. FrameAnimator
	 * contains several constants for special dimension values.
	 * Negative dimension values could cause undesired widths.
	 * @param width the width to use to draw animation images
	 */
	public void setImageWidth(int width)
	{
		imgWidth = width;
	}
	
	/** Set the height to draw the animation images. FrameAnimator
	 * contains several constants for special dimension values.
	 * Negative dimension values could cause undesired heights.
	 * @param height the height to use to draw animation images
	 */
	public void setImageHeight(int height)
	{
		imgHeight = height;
	}
	
	/** Check if this frame animator is currently centering its images over
	 * the coordinates specified for drawing the animation.
	 * @return true if coordinates passed in for drawing purposes will be used
	 * 		as the center of the drawn images
	 */
	public boolean isCenteringOverCoords()
	{
		return centerOverCoords;
	}
	
	/** Set if this animation should center its images over the coodinates
	 * specified when rendering, or just draw the image with the specified
	 * coordinates as the upper left corner.
	 * @param doCenter true to center over coordinates when rendering
	 */
	public void setCenterOverCoords(boolean doCenter)
	{
		centerOverCoords = doCenter;
	}
	
	/** Gets the integer value for the current frame.
	 * @return the integer portion of the current frame to be rendered
	 */
	public int getFrame()
	{
		return currFrame;
	}
	
	/** Set what frame number should render next. This updates on its own,
	 * so no need to worry about it unless you want to adjust the current
	 * animation (e.g. reset back to 1 when you change animation sets).
	 * <br>
	 * <br><b>Note</b> that the first frame is 1, not 0.
	 * @param frame the integer portion of the frame name
	 */
	public void setFrame(int frame)
	{
		isFinished = false;
		currFrame = frame;
		frameStart = core.ProgramTime.getTime();
	}
	
	/** Selects and updates what the next rendered frame is. */
	private void selectFrame()
	{
		// Increment the animation frame
		if (core.ProgramTime.getTime() - frameStart >= interval)
		{
			setFrame(currFrame+1);
		}
		// Get the path to the current frame's image
		setFramePath();
		// If the image frame doesn't exist...
		if (!GfxManager.getResManager().resExists(currFramePath))
		{
			if (isLooping())
			{
				setFrame(1);
				setFramePath();
			}
			else
			{
				isFinished = true;
			}
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
