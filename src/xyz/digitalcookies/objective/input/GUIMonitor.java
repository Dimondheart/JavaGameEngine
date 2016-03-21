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

import java.util.LinkedList;

import xyz.digitalcookies.objective.input.gui.GUIObject;

/** TODO document
 * @author Bryan Charles Bettis
 */
public class GUIMonitor
{
	/** List of all GUI objects added to a layer. */
	private static LinkedList<GUIObject> guiObjects =
			new LinkedList<GUIObject>();
	
	/** Basic constructor. */
	GUIMonitor()
	{
	}
	
	/** Add a new GUI object to the list of active GUI objects. This is done
	 * automatically by the layering system.  Each GUI element can only be
	 * added once.
	 * @param element the GUI object to add
	 */
	public static void addGUIElement(GUIObject element)
	{
		synchronized(guiObjects)
		{
			if (!guiObjects.contains(element))
			{
				guiObjects.add(element);
			}
		}
	}
	
	/** Remove a GUI object from the list of active GUI objects. This is done
	 * automatically by the layering system.
	 * @param element the GUI object to remove
	 */
	public static void removeGUIElement(GUIObject element)
	{
		synchronized(guiObjects)
		{
			guiObjects.remove(element);
		}
	}
	
	/** Setup different device-dependent stuff. */
	void setup()
	{
		clear();
	}
	
	void poll()
	{
		synchronized(guiObjects)
		{
			guiObjects.forEach(
					(GUIObject element)->
					{
						synchronized(element)
						{
							element.poll();
						}
					}
				);
		}
	}

	void clear()
	{
		synchronized(guiObjects)
		{
			guiObjects.forEach(
					(GUIObject element)->
					{
						synchronized(element)
						{
							element.clear();
						}
					}
				);
		}
	}
}
