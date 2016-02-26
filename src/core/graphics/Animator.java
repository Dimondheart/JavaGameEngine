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
	private boolean loopAnimation;
	protected boolean isFinished;
	
	public Animator()
	{
		setLooping(false);
		isFinished =  false;
	}
	
	public abstract void renderAnimation(RenderEvent event, int x, int y);
	
	public boolean isLooping()
	{
		return loopAnimation;
	}
	
	public void setLooping(boolean doLoop)
	{
		loopAnimation = doLoop;
	}
	
	public boolean isAnimationDone()
	{
		return isFinished;
	}
}
