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

package core.sound;

/** Used to represent all general sound events on a queue.
 * @author Bryan Charles Bettis
 */
abstract class BaseSoundEvent
{
	/** The time the sound effect was queued. */
	private final long queueTime;
	
	/** Basic constructor.
	 * @param type the Type of this sound event
	 */
	public BaseSoundEvent()
	{
		this.queueTime = core.ProgramClock.getTime();
	}
	
	/** How long this event has been queued.
	 * @return the ProgramTimer time that this event has been queued
	 */
	public final long getTimeInQueue()
	{
		return core.ProgramClock.getTime() - queueTime;
	}
	
	/** Determines if this event has met any conditions that make
	 * it "stale"/old.
	 * @return if this event is old/stale
	 */
	public boolean isStale()
	{
		return false;
	}
}
