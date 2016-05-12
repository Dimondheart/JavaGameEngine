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

import xyz.digitalcookies.objective.Game;

/** Base class for all sound system events.
 * @author Bryan Charles Bettis
 */
abstract class BaseSoundEvent
{
	/** The core.ProgramClock time when the sound effect was queued. */
	private final long queueTime;
	
	/** Basic constructor. */
	public BaseSoundEvent()
	{
		this.queueTime = (long) Game.getTimeMilli();
	}
	
	/** How long this event has been queued.
	 * @return the core.ProgramClock time that this event has been queued
	 */
	public final long getTimeInQueue()
	{
		return (long) (Game.getTimeMilli() - queueTime);
	}
	
	/** Determines if this event has met any conditions that make
	 * it "stale"/old, for example when a sound effect event has been
	 * queued for a long time.
	 * @return true if this event is old/stale
	 */
	boolean isStale()
	{
		return false;
	}
}
