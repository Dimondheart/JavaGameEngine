package core.userinput.inputdevice;

import java.util.LinkedList;

import core.graphics.gui.GUIObject;

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
