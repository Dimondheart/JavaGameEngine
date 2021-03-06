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

package xyz.digitalcookies.objective.input;

import xyz.digitalcookies.objective.EngineEvent;

/** An event for the InputManager class.
 * @author Bryan Charles Bettis
 */
class InputManagerEvent extends EngineEvent
{
	/** What type of event this is. */
	private Type type;
	
	/** The different types of events.
	 * @author Bryan Charles Bettis
	 */
	public enum Type
	{
		/** Update input devices. */
		POLL,
		/** Clear input devices. */
		CLEAR,
		/** Pause the game when the window has been minimized, etc. */
		PAUSE,
		/** Resume the game after it has been PAUSED. */
		RESUME,
		/** Quit the game. */
		QUIT
	}
	
	/** Basic constructor.
	 * @param type the Type of this event
	 */
	public InputManagerEvent(Type type)
	{
		this.type = type;
	}
	
	/** Gets the type of this event.
	 * @return the Type of this event
	 */
	public Type getType()
	{
		return type;
	}
}
