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

package core.userinput.inputdevice;

import java.util.LinkedList;

import core.userinput.inputdevice.gui.GUIObject;

/** TODO document
 * @author Bryan Charles Bettis
 */
public class GUIMonitor implements InputDevice
{
	/** List of all GUI objects added to a layer. */
	private LinkedList<GUIObject> guiObjects;
	
	/** Basic constructor. */
	public GUIMonitor()
	{
		guiObjects = new LinkedList<GUIObject>();
	}
	
	@Override
	public synchronized void poll()
	{
		for (GUIObject element : guiObjects)
		{
			element.poll();
		}
	}

	@Override
	public synchronized void clear()
	{
		for (GUIObject element : guiObjects)
		{
			element.clear();
		}
	}
	
	/** Add a new GUI object to the list of active GUI objects. This is done
	 * automatically by the layering system.  Each GUI element can only be
	 * added once.
	 * @param element the GUI object to add
	 */
	public synchronized void addGUIElement(GUIObject element)
	{
		if (guiObjects.contains(element))
		{
			return;
		}
		guiObjects.add(element);
	}
	
	/** Remove a GUI object from the list of active GUI objects. This is done
	 * automatically by the layering system.
	 * @param element the GUI object to remove
	 */
	public synchronized void removeGUIElement(GUIObject element)
	{
		guiObjects.remove(element);
	}
}
