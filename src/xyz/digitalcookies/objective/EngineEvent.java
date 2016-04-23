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

package xyz.digitalcookies.objective;

import java.util.HashMap;

/** Base class for all events generate by this game engine.
 * @author Bryan Charles Bettis
 */
public abstract class EngineEvent
{
	private HashMap<String, Object> properties;
	
	public EngineEvent()
	{
		this.properties = new HashMap<String, Object>();
	}
	
	public void setProperty(String property, Object value)
	{
		properties.put(property, value);
	}
	
	public Object getProperty(String property)
	{
		return properties.get(property);
	}
}
