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

/** An event used to play a sound effect.
* @author Bryan Charles Bettis
*/
public class SFXEvent extends BaseSoundEvent
{
	/** How long a sound effect can be queued before it is stale/old. */
	private static final long MAX_QUEUE_TIME = 200;
	
	/** The sound effect to play. */
	private String sfx;
	
	/** Basic constructor.
	 * @param sfx the name of the file
	 */
	public SFXEvent(String sfx)
	{
		this.sfx = sfx;
	}
	
	/** Get the name/path of the sound effect to play.
	 * @return the name of the file
	 */
	public String getSFX()
	{
		return sfx;
	}
	
	/** Set the name of the sound effect to play.
	 * @param sfx the sound effect to play
	 */
	public void setSFX(String sfx)
	{
		this.sfx = sfx;
	}
	
	@Override
	boolean isStale()
	{
		return getTimeInQueue() >= MAX_QUEUE_TIME;
	}
}
