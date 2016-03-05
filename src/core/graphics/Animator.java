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

/** The base class for all graphics classes that handle animating
 * things on the screen, like rotation or image-frame-based.
 * @author Bryan Charles Bettis
 */
public abstract class Animator
{
	/** If the animation should reset to the beginning each time it reaches
	 * the end of the animation.
	 */
	private boolean loopAnimation;
	/** Indicates when the animation has finished the last frame,
	 * and the animation is not looping. Subclasses should directly set this
	 * when their animation is complete and they are not set to loop.
	 */
	protected boolean isFinished;
	
	/** Basic constructor. */
	public Animator()
	{
		setLooping(false);
		isFinished =  false;
	}
	
	/** Called to render an animation, similar to the render(...) method
	 * of core.graphics.Renderer.
	 * @param event the render event containing the graphics context and other
	 * 		info to use for rendering
	 * @param x the x coordinate to render the animation at
	 * @param y the y coordinate to render the animation at
	 */
	public abstract void renderAnimation(RenderEvent event, int x, int y);
	
	/** Check if this animation is set to loop when it reaches the end of the
	 * animation.
	 * @return true if currently set to loop
	 */
	public boolean isLooping()
	{
		return loopAnimation;
	}
	
	/** Set if this animation should loop when it finishes with its
	 * animation.
	 * @param doLoop true to loop the animation
	 */
	public void setLooping(boolean doLoop)
	{
		loopAnimation = doLoop;
	}
	
	/** Check if the animation is finished (usually means the actual animation
	 * has finished and is not set to loop.)
	 * @return true if the animation is complete
	 */
	public boolean isAnimationDone()
	{
		return isFinished;
	}
}
