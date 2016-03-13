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

package core.userinput.inputdevice.gui;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import core.graphics.RenderEvent;

/** A GUIPanel organizes the GUIObjects that have been added to it
 * relative to other GUIObjects in the panel. Only the GUIPanel needs
 * to be added to a layer set; when the panel updates or renders,
 * it will update/render all GUI elements added to it.
 * @author Bryan Charles Bettis
 */
public class GUIPanel extends GUIObject
{
	/** Indicates an invalid row index. */
	private static final int INVALID_ROW = -10000;
	/** Indicates an invalid column index. */
	private static final int INVALID_COLUMN = -10000;
	
	/** Number of pixels between GUI elements in the same row. */
	private int columnSpacing = 4;
	/** Number of pixels between GUI element rows. */
	private int rowSpacing = 4;
	/** A hash map of the GUIObjects in this panel attached to the
	 * name/tag they were added with.
	 */
	private ConcurrentHashMap<String, GUIObject> guiObjects;
	/** A 2D list containing GUIObjects organized relative to each other. */
	private LinkedList<LinkedList<GUIObject>> guiPositioning;
	
	/** Used when adding a new GUIObject to specify where to place the new
	 * object relative to another specified object.
	 * @author Bryan Charles Bettis
	 */
	public enum RelativePosition
	{
		/** Place the new GUIObject to the left of the specified one. */
		LEFT_OF,
		/** Place the new GUIObject to the right of the specified one. */
		RIGHT_OF,
		/** Place the new GUIObject above the specified one. If there is
		 * already a row above the specified GUIObject, a new row will be
		 * created for the GUIObject.
		 */
		ABOVE,
		/** Place the new GUIObject below the specified one. If there is
		 * already a row below the specified GUIObject, a new row will be
		 * created for the GUIObject.
		 */
		BELOW
	}
	
	/** Basic constructor. Note that width and height currently have no
	 * effect because they are not yet used by the panel.
	 * @param x the x coordinate of the panel's upper left corner
	 * @param y the y coordinate of the panel's upper left corner
	 */
	public GUIPanel(int x, int y)
	{
		setPos(x, y);
		setDims(width, height);
		guiObjects = new ConcurrentHashMap<String, GUIObject>();
		guiPositioning = new LinkedList<LinkedList<GUIObject>>();
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		if (getBGColor() != null)
		{
			event.getContext().setColor(getBGColor());
			event.getContext().fillRect(getX(), getY(), getWidth(), getHeight());
		}
		// For each row
		for (LinkedList<GUIObject> row : guiPositioning)
		{
			// Draw each contained element
			for (GUIObject gui : row)
			{
				gui.render(event);
			}
		}
	}

	@Override
	public synchronized void poll()
	{
		updatePositions();
		updateDims();
		// Update each contained element
		for (LinkedList<GUIObject> row : guiPositioning)
		{
			for (GUIObject gui : row)
			{
				gui.poll();
			}
		}
	}
	
	@Override
	public synchronized void destroy()
	{
		for (GUIObject element : guiObjects.values())
		{
			element.destroy();
		}
		super.destroy();
	}
	
	/** Get the spacing between GUIObjects in the same row, in pixels.
	 * @return the spacing between GUI elements in the same row
	 */
	public int getColumnSpacing()
	{
		return columnSpacing;
	}
	
	/** Get the spacing between two rows of GUIObjects.
	 * @return the spacing between two rows of GUI elements
	 */
	public int getRowSpacing()
	{
		return rowSpacing;
	}
	
	/** Set the number of pixels of spacing between GUIObjects in the same
	 * row.
	 * @param spacing the spacing between each GUIObject in a row
	 */
	public void setColumnSpacing(int spacing)
	{
		columnSpacing = spacing;
	}
	
	/** Set the number of pixels of spacing between each row.
	 * @param spacing the spacing between rows, measured from the bottom of the
	 * 		highest element in the previous row
	 */
	public void setRowSpacing(int spacing)
	{
		rowSpacing = spacing;
	}
	
	/** Add the specified element and use the specified tag to access it, and
	 * places it on the upper left corner (useful for adding the first
	 * element.)
	 * @param obj the GUIObject to add to this panel
	 * @param name the name/tag to use to identify and access the new object
	 * 		from this panel
	 */
	public synchronized void addGUIObject(GUIObject obj, String name)
	{
		addGUIObject(obj, name, 0, 0);
	}
	
	/** Add the specified gui object use the specified tag to identify it, and
	 * position it relative to the specified GUI object by tag.
	 * @param obj the GUIObject to add to this panel
	 * @param name the name/tag to use to identify and access the new object
	 * 		from this panel
	 * @param relPos where to place the new GUI element relative to the
	 * 		specified element
	 * @param relTo the name of a GUIObject in this panel to add the new
	 * 		GUIObject relative to
	 */
	public synchronized void addGUIObject(GUIObject obj, String name, RelativePosition relPos, String relTo)
	{
		GUIObject gui = guiObjects.get(relTo);
		if (gui == null)
		{
			System.out.println(
					"WARNING: Unable to locate GUIObject by the name of \'"
					+ relTo
					+ "\' for relative positioning in a GUIPanel."
					);
		}
		else
		{
			addGUIObject(obj, name, relPos, gui);
		}
	}
	
	/** Add the specified GUIObject, use the specified tag to identify it, and
	 * position it relative to the specified GUI element.
	 * @param obj the GUIObject to add to this panel
	 * @param name the name/tag to use to identify and access the new object
	 * 		from this panel
	 * @param relPos where to place the new GUI element relative to the
	 * 		specified element
	 * @param relTo the GUIObject (must be in this panel) to add the new
	 * 		GUIObject relative to
	 */
	public synchronized  void addGUIObject(GUIObject obj, String name, RelativePosition relPos, GUIObject relTo)
	{
		// Cannot position relative to element not in the panel
		if (!guiObjects.containsValue(relTo))
		{
			System.out.println(
					"WARNING: Attempt to add a GUIObject to a GUIPanel relative "
					+ "to an object not in the GUIPanel."
					);
			Thread.dumpStack();
			return;
		}
		// The row and column of the positioning system to insert into
		int rowInsert = INVALID_ROW;
		int colInsert = INVALID_COLUMN;
		// Try to determine row and column position
		for (int i = 0; i < guiPositioning.size(); ++i)
		{
			// Found the element to position relative to
			if (guiPositioning.get(i).contains(relTo))
			{
				switch (relPos)
				{
					// Position above
					case ABOVE:
						// Place in a new row
						if (i < guiPositioning.size())
						{
							insertRow(i);
						}
						rowInsert = i;
						colInsert = -1;
						break;
					// Position below
					case BELOW:
						// Place in a new row
						if (i < guiPositioning.size() - 1)
						{
							insertRow(i+1);
						}
						rowInsert = i+1;
						colInsert = -1;
						break;
					// To the left of
					case LEFT_OF:
						rowInsert = i;
						colInsert = guiPositioning.get(i).indexOf(relTo);
						break;
					// To the right of
					case RIGHT_OF:
						rowInsert = i;
						colInsert = guiPositioning.get(i).indexOf(relTo) + 1;
						break;
					// Leave row & column as invalid values
					default:
						break;
				}
				break;
			}
		}
		// Add the GUIObject to the correct position
		addGUIObject(obj, name, rowInsert, colInsert);
	}
	
	/** Checks if this panel contains a GUI element with the specified
	 * name/tag.
	 * @param name the name/tag given to the GUIObject when it was added to
	 * 		this panel
	 * @return the requested GUIObject, or null if not found
	 */
	public synchronized boolean containsGUIObject(String name)
	{
		return guiObjects.containsKey(name);
	}
	
	/** Get the GUIObject with the specified name/tag from this panel.
	 * @param name the name/tag specified with the GUIObject when it was added
	 * @return the GUIObject, or null if there is no element with that name
	 */
	public synchronized GUIObject getGUIObject(String name)
	{
		GUIObject obj = guiObjects.get(name);
		if (obj == null)
		{
			System.out.println(
					"WARNING: Attempted to get \'"
					+ name
					+ "\' from a GUIPanel not containing an element by "
					+ "that tag."
					);
			Thread.dumpStack();
		}
		return obj;
	}
	
	/** Removes the specified element by name from this GUIPanel. Note that
	 * removal by name is the only way currently to remove a GUIObject from
	 * a panel.
	 * @param name the name/tag the GUI element was added with
	 */
	public synchronized void removeGUIObject(String name)
	{
		// Get & remove the object from the named list
		GUIObject obj = guiObjects.remove(name);
		// Object not found
		if (obj == null)
		{
			return;
		}
		// Iterate over the rows to find the GUI element
		for (LinkedList<GUIObject> row : guiPositioning)
		{
			// Try to remove the GUIObject, stop when successful
			if (row.remove(obj))
			{
				break;
			}
		}
	}
	
	/** Adds the specified GUIObject at the specified position. Creates
	 * additional rows above or below current rows as needed.
	 * @param obj the GUIObject to add
	 * @param name the name/tag to use for accessing the added GUIObject
	 * @param rowInsert the row to insert the element in
	 * @param colInsert the column (position within the row technically) to
	 * 		insert the new GUI element in
	 */
	private synchronized void addGUIObject(GUIObject obj, String name, int rowInsert, int colInsert)
	{
		// The finalized row values to use (after a few adjustments)
		int fRowIns = rowInsert;
		int fColIns = colInsert;
		// Invalid positioning values; return
		if (rowInsert == INVALID_ROW || colInsert == INVALID_COLUMN)
		{
			System.out.println(
					"WARNING: Unable to locate a GUIObject in a GUIPanel to "
					+ "set the relative position of \'"
					+ name
					+ "\' relative to."
					);
			return;
		}
		// Adding 1 or more new first rows
		else if (rowInsert < 0)
		{
			fRowIns = 0;
			fColIns = -1;
			// Add as many rows as necessary
			for (int i = rowInsert; i < 0; ++i)
			{
				insertRow(0);
			}
		}
		// Adding 1 or more new last rows
		else if (rowInsert > guiPositioning.size() - 1)
		{
			fRowIns = rowInsert;
			fColIns = -1;
			// Add new rows until there are enough
			for (int i = rowInsert; i > guiPositioning.size() - 1; --i)
			{
				insertRow(guiPositioning.size());
			}
		}
		// Make sure row is kept valid
		if (fRowIns < 0)
		{
			fRowIns = 0;
		}
		// Remove any placeholder elements from the new row
		if (!guiObjects.containsValue(guiPositioning.get(fRowIns).getFirst()))
		{
			guiPositioning.get(fRowIns).removeFirst();
		}
		// Limit the column value to prevent index exceptions
		if (colInsert > guiPositioning.get(fRowIns).size())
		{
			fColIns = guiPositioning.get(fRowIns).size();
		}
		if (fColIns < 0)
		{
			fColIns = guiPositioning.get(fRowIns).size();
		}
		// Add to the positioning lists
		guiPositioning.get(fRowIns).add(fColIns, obj);
		// Add to the tagged list (for easier access)
		guiObjects.put(name, obj);
	}
	
	/** Update the absolute positions of contained GUIObjects. */
	private synchronized void updatePositions()
	{
		// Remove any completely empty rows
		guiPositioning.removeIf((LinkedList<GUIObject> row)->{return row.isEmpty();});
		// Iterate over rows
		for (int r = 0; r < guiPositioning.size(); ++r)
		{
			// Get the actual row
			LinkedList<GUIObject> row = guiPositioning.get(r);
			// Empty row; ignore it
			if (row.isEmpty())
			{
				continue;
			}
			// Iterate over the columns (individual GUIObjects)
			for (int c = 0; c < guiPositioning.get(r).size(); ++c)
			{
				// Get the actual object
				GUIObject obj = row.get(c);
				// Align first row's first element to the panel's (0,0)
				if (r == 0 && c == 0)
				{
					obj.setPos(getX(), getY());
					continue;
				}
				// First row but not first element
				if (r == 0 && c > 0)
				{
					// Get previous element in row
					GUIObject previous = row.get(c-1);
					// Align with top of previous element
					int newY = previous.getY();
					// X of prev + width of prev + element spacing
					int newX =
							previous.getX()
							+ previous.getWidth()
							+ getColumnSpacing();
					obj.setPos(newX, newY);
				}
				// First element of each row
				else if (r > 0 && c == 0)
				{
					// Get the first object in previous row
					GUIObject aboveFirst = guiPositioning.get(r-1).getFirst();
					if (aboveFirst == null)
					{
						System.out.println(
								"INTERNAL WARNING: "
								+ "GUIPanel alignment error #1 (position update)"
								);
						continue;
					}
					int newY = 
							aboveFirst.getY()
							+ getRowHeight(r-1)
							+ getRowSpacing();
					// Same as gui panel
					int newX = getX();
					obj.setPos(newX, newY);
				}
				else if (r > 0 && c > 0)
				{
					// Get previous element in row
					GUIObject previous = row.get(c-1);
					int newY = previous.getY();
					// X of prev + width of prev + element spacing
					int newX =
							previous.getX()
							+ previous.getWidth()
							+ getColumnSpacing();
					obj.setPos(newX, newY);
				}
			}
		}
	}
	
	private void updateDims()
	{
		int width = 0;
		int height = 0;
		for (int i = 0; i < guiPositioning.size(); ++i)
		{
			int rw = getRowWidth(i);
			if (rw > width)
			{
				width = rw;
			}
			height += getRowHeight(i);
			if (i != guiPositioning.size() - 1)
			{
				height += getRowSpacing();
			}
		}
		setDims(width, height);
		
	}
	
	/** Inserts a row before the specified row, with a Placeholder element
	 * added to the new row.
	 * @param insertAt the row to insert the new row before
	 */
	private void insertRow(int insertAt)
	{
		guiPositioning.add(insertAt, new LinkedList<GUIObject>());
		guiPositioning.get(insertAt).add(new Placeholder(0,0,100,1));
	}
	
	/** Gets the height of the specified row, which is equal to the height of
	 * the highest element in the specified row (does not include the padding/
	 * spacing around the row.)
	 * @param row the row to check the height of
	 * @return the height of the row, based on the highest element in the row
	 */
	private int getRowHeight(int row)
	{
		// Invalid row; return 0
		if (row >= guiPositioning.size())
		{
			return 0;
		}
		// Get the row
		LinkedList<GUIObject> r = guiPositioning.get(row);
		// Check if the row is not setup or is empty
		if (r == null || r.isEmpty())
		{
			return 0;
		}
		// Otherwise determine the height
		else
		{
			int maxHeight = 0;
			// Find the height of the highest object in the row
			for (GUIObject obj : r)
			{
				if (obj.getHeight() > maxHeight)
				{
					maxHeight = obj.getHeight();
				}
			}
			return maxHeight;
		}
	}
	
	private int getRowWidth(int row)
	{
		GUIObject obj = guiPositioning.get(row).getLast();
		return obj.getX() - getX() + obj.getWidth();
	}
}
