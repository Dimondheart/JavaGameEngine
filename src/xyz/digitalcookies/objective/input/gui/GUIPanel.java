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

import java.util.LinkedList;

import xyz.digitalcookies.objective.graphics.BoundedRenderer;
import xyz.digitalcookies.objective.graphics.RendererPanel;

/** A thin layer over RendererPanel that automatically polls the GUIElements
 * that are part of the panel. It also provides multiple convenience methods
 * for getting specific types of GUIElements from the panel (to reduce the
 * amount of type casting that developers have to do.)
 * @author Bryan Charles Bettis
 */
public class GUIPanel extends RendererPanel implements GUIElement
{
	/** If this panel is enabled or not. */
	private boolean isEnabled;
	
	/** Standard constructor.
	 * @param x the x coordinate of the panel's upper left corner
	 * @param y the y coordinate of the panel's upper left corner
	 */
	public GUIPanel(int x, int y)
	{
		super(x,y);
		setVisible(true);
		setEnabled(true);
	}

	@Override
	public synchronized void poll()
	{
		synchronized (rendererPositioning)
		{
			// Update each contained element
			for (LinkedList<BoundedRenderer> row : rendererPositioning)
			{
				for (BoundedRenderer br : row)
				{
					if (br instanceof GUIElement)
					{
						((GUIElement) br).poll();
					}
				}
			}
		}
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		// If not visible, disable all GUI elements
		if (!isVisible())
		{
			synchronized (renderers)
			{
				for (BoundedRenderer br : renderers.values())
				{
					if (br instanceof GUIElement)
					{
						((GUIElement) br).setEnabled(false);
					}
				}
			}
		}
	}
	
	@Override
	public boolean isEnabled()
	{
		return isEnabled;
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		isEnabled = enabled;
		// Should not be enabled and not visible
		if (isEnabled)
		{
			setVisible(true);
		}
		synchronized (renderers)
		{
			// Update enabled state of each contained element
			for (BoundedRenderer br : renderers.values())
			{
				if (br instanceof GUIElement)
				{
					((GUIElement) br).setEnabled(enabled);
				}
			}
		}
	}
	
	/** Get a sub-GUIPanel from this GUI panel.
	 * @param tag the tag the desired panel was added with
	 * @return the requested GUI panel, or null if not found
	 */
	public GUIPanel getSubGUIPanel(String tag)
	{
		if (tag == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Specified null argument when "
					+ " attempting to get a sub GUIPanel from a "
					+ "GUIPanel."
					);
			return null;
		}
		RendererPanel panel = getSubPanel(tag);
		// tag not found
		if (panel == null)
		{
			return null;
		}
		if (panel instanceof GUIPanel)
		{
			return (GUIPanel) panel;
		}
		return null;
	}
	
	/** Gets the specified GUI element from this panel.
	 * @param tag the tag the desired GUI element was added with
	 * @return the requested GUI element, or null if not found
	 */
	public GUIElement getGUIElement(String tag)
	{
		if (tag == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Specified null argument when "
					+ " attempting to get a GUIElement from a GUIPanel."
					);
			return null;
		}
		BoundedRenderer br = renderers.get(tag);
		// tag not found
		if (br == null)
		{
			return null;
		}
		if (br instanceof GUIElement)
		{
			return (GUIElement) br;
		}
		return null;
	}
	
	/** Gets the specified GUIObject from this panel.
	 * @param tag the tag the desired GUI object was added with
	 * @return the requested GUI object, or null if not found
	 */
	public GUIObject getGUIObject(String tag)
	{
		if (tag == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Specified null argument when "
					+ " attempting to get a GUIObject from a GUIPanel."
					);
			return null;
		}
		GUIElement obj = getGUIElement(tag);
		// Object not found in panel
		if (obj == null)
		{
			return null;
		}
		if (obj instanceof GUIObject)
		{
			return (GUIObject) obj;
		}
		return null;
	}
	
	/** Get a button from this panel.
	 * @param tag the tag the desired button was added with
	 * @return the requested button, or null if not found
	 */
	public Button getButton(String tag)
	{
		if (tag == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Specified null argument when "
					+ " attempting to get a Button from a GUIPanel."
					);
			return null;
		}
		GUIObject obj = getGUIObject(tag);
		// Object not found in panel
		if (obj == null)
		{
			return null;
		}
		if (obj instanceof Button)
		{
			return (Button) obj;
		}
		return null;
	}
}
