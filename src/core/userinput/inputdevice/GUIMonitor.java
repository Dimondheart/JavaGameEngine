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

import core.graphics.gui.GUIObject;

/** TODO document
 * @author Bryan Charles Bettis
 */
public class GUIMonitor implements InputDevice
{
	private LinkedList<GUIObject> guiObjects;
	
	public GUIMonitor()
	{
		guiObjects = new LinkedList<GUIObject>();
	}
	
	@Override
	public synchronized void poll()
	{
		for (GUIObject element : guiObjects)
		{
			element.update();
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
	
	public synchronized void addGUIElement(GUIObject element)
	{
		if (guiObjects.contains(element))
		{
			return;
		}
		guiObjects.add(element);
	}
	
	public synchronized void removeGUIElement(GUIObject element)
	{
		guiObjects.remove(element);
	}
}
