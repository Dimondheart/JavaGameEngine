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

package core.userinput;

/** An event for the InputManager class.
 * @author Bryan Charles Bettis
 */
class InputManagerEvent
{
	/** What type of event this is. */
	private Type type;
	
	/** The different types of events.
	 * @author Bryan Charles Bettis
	 */
	public enum Type
	{
		POLL,
		CLEAR,
		PAUSE,
		RESUME,
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
