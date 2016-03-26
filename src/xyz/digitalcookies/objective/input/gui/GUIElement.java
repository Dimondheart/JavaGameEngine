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

package xyz.digitalcookies.objective.input.gui;

/** TODO Document
 * @author Bryan Charles Bettis
 */
public interface GUIElement
{
	/** Update the GUI object. */
	public void poll();
	
	/** Clear stored data for this GUI object. */
	public default void clear()
	{
	}
	
	/** Check if this element is enabled.
	 * @return true if enabled, false if not enabled (not enabled usually means
	 * 		it cannot be interacted with)
	 */
	public boolean isEnabled();
	
	/** Set if this element is enabled. An element that is set to enabled
	 * should also be automatically made visible.
	 * @param enabled true to enable the element, false to disable it
	 */
	public void setEnabled(boolean enabled);
}
