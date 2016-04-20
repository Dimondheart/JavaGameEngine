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

package xyz.digitalcookies.objective.graphics;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/** A RendererPanel organizes the BoundedRenderers that have been added to it
 * relative to other BoundedRenderers in the panel. RendererPanel only needs
 * to be added to a layer set; when the panel renders, it will render
 * all elements contained in it.
 * @author Bryan Charles Bettis
 */
public class RendererPanel extends BoundedRenderer
{
	/** Indicates an invalid row index. */
	private static final int INVALID_ROW = -10000;
	/** Indicates an invalid column index. */
	private static final int INVALID_COLUMN = -10000;
	
	/** A hash map of the BoundedRenderers in this panel attached to the
	 * name/tag they were added with.
	 */
	protected ConcurrentHashMap<String, BoundedRenderer> renderers;
	/** A 2D list containing BoundedRenderers organized relative to each
	 * other.
	 */
	protected LinkedList<LinkedList<BoundedRenderer>> rendererPositioning;
	/** Number of pixels between elements in the same row. */
	private int columnSpacing = 4;
	/** Number of pixels between element rows. */
	private int rowSpacing = 4;
	
	/** Used when adding a new element to specify where to place the new
	 * element relative to another element in the panel.
	 * @author Bryan Charles Bettis
	 */
	public enum RelativePosition
	{
		/** Place the new renderer to the left of the specified one. */
		LEFT_OF,
		/** Place the new renderer to the right of the specified one. */
		RIGHT_OF,
		/** Place the new renderer above the specified one. If there is
		 * already a row above the specified renderer, a new row will be
		 * created for the Renderer.
		 */
		ABOVE,
		/** Place the new Renderer below the specified one. If there is
		 * already a row below the specified renderer, a new row will be
		 * created for the Renderer.
		 */
		BELOW
	}
	
	/** Standard constructor.
	 * @param x the x coordinate of the panel's upper left corner
	 * @param y the y coordinate of the panel's upper left corner
	 */
	public RendererPanel(int x, int y)
	{
		setPos(x, y);
		setDims(getWidth(), getHeight());
		renderers = new ConcurrentHashMap<String, BoundedRenderer>();
		rendererPositioning = new LinkedList<LinkedList<BoundedRenderer>>();
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		synchronized (renderers)
		{
			for (BoundedRenderer br : renderers.values())
			{
				synchronized (br)
				{
					br.setVisible(visible);
				}
			}
		}
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		super.render(event);
		// Update positions of elements & panel dims before rendering
		updatePositions();
		updateDims();
		// For each row
		for (LinkedList<BoundedRenderer> row : rendererPositioning)
		{
			// Draw each contained element
			for (BoundedRenderer br : row)
			{
				br.render(event);
			}
		}
	}
	
	@Override
	public synchronized void destroy()
	{
		for (BoundedRenderer element : renderers.values())
		{
			element.destroy();
		}
		super.destroy();
	}
	
	/** Get the spacing between renderers in the same row, in pixels.
	 * @return the spacing between elements in the same row
	 */
	public int getColumnSpacing()
	{
		return columnSpacing;
	}
	
	/** Get the spacing between two rows of renderers.
	 * @return the spacing between two rows of elements
	 */
	public int getRowSpacing()
	{
		return rowSpacing;
	}
	
	/** Set the number of pixels of spacing between renderers in the same
	 * row.
	 * @param spacing the spacing between each element in a row
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
	 * @param obj the BoundedRenderer to add to this panel
	 * @param name the name/tag to use to identify and access the new object
	 * 		from this panel
	 */
	public synchronized void addRenderer(BoundedRenderer obj, String name)
	{
		addRenderer(obj, name, 0, 0);
	}
	
	/** Add the specified element, use the specified tag to identify it, and
	 * position it relative to the specified element by it's tag.
	 * @param obj the BoundedRenderer to add to this panel
	 * @param name the name/tag to use to identify and access the new element
	 * 		from this panel
	 * @param relPos where to place the new element relative to the
	 * 		specified element
	 * @param relTo the name of an element already in this panel to add the new
	 * 		element relative to
	 */
	public synchronized void addRenderer(BoundedRenderer obj, String name, RelativePosition relPos, String relTo)
	{
		// No relative to name specified
		if (relTo == null)
		{
			System.out.println("WARNING: No tag specified for the relative to "
					+ "BoundedRenderer when adding a BoundedRenderer to a "
					+ "RendererPanel"
					);
			return;
		}
		// Get the renderer to align relative to
		BoundedRenderer relToObj = renderers.get(relTo);
		// Specified tag is not used currently in this panel
		if (relToObj == null)
		{
			System.out.println(
					"WARNING: Unable to locate BoundedRenderer named \'"
					+ relTo
					+ "\' for relative positioning in a RendererPanel."
					);
		}
		// Otherwise call the next method to continue the process
		else
		{
			addRenderer(obj, name, relPos, relToObj);
		}
	}
	
	/** Add the specified BoundedRenderer, use the specified tag to identify it, and
	 * position it relative to the specified element.
	 * @param obj the BoundedRenderer to add to this panel
	 * @param name the name/tag to use to identify and access the new element
	 * 		from this panel
	 * @param relPos where to place the new element relative to the
	 * 		specified element
	 * @param relTo the BoundedRenderer (must be in this panel) to add the new
	 * 		element relative to
	 */
	public synchronized  void addRenderer(BoundedRenderer obj, String name, RelativePosition relPos, BoundedRenderer relTo)
	{
		// Cannot position relative to element not in the panel
		if (!renderers.containsValue(relTo))
		{
			System.out.println(
					"WARNING: Attempt to add a BoundedRenderer to a "
					+ "RendererPanel relative to an object not in the panel."
					);
			Thread.dumpStack();
			return;
		}
		// The row and column of the positioning system to insert into
		int rowInsert = INVALID_ROW;
		int colInsert = INVALID_COLUMN;
		// Try to determine row and column position
		for (int i = 0; i < rendererPositioning.size(); ++i)
		{
			// Found the element to position relative to
			if (rendererPositioning.get(i).contains(relTo))
			{
				switch (relPos)
				{
					// Position above
					case ABOVE:
						// Place in a new row
						if (i < rendererPositioning.size())
						{
							insertRow(i);
						}
						rowInsert = i;
						colInsert = -1;
						break;
					// Position below
					case BELOW:
						// Place in a new row
						if (i < rendererPositioning.size() - 1)
						{
							insertRow(i+1);
						}
						rowInsert = i+1;
						colInsert = -1;
						break;
					// To the left of
					case LEFT_OF:
						rowInsert = i;
						colInsert = rendererPositioning.get(i).indexOf(relTo);
						break;
					// To the right of
					case RIGHT_OF:
						rowInsert = i;
						colInsert = rendererPositioning.get(i).indexOf(relTo) + 1;
						break;
					// Leave row & column as invalid values
					default:
						break;
				}
				break;
			}
		}
		// Add the GUIObject to the correct position
		addRenderer(obj, name, rowInsert, colInsert);
	}
	
	/** Checks if this panel contains a GUI element with the specified
	 * name/tag.
	 * @param name the name/tag given to the GUIObject when it was added to
	 * 		this panel
	 * @return the requested GUIObject, or null if not found
	 */
	public synchronized boolean contains(String name)
	{
		return renderers.containsKey(name);
	}
	
	/** Get a sub-panel of this RendererPanel.
	 * @param tag the name the desired renderer panel was added with
	 * @return the requested renderer panel, or null if not found
	 */
	public RendererPanel getSubPanel(String tag)
	{
		if (tag == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Specified null argument when "
					+ " attempting to get a sub RendererPanel from a "
					+ "RendererPanel."
					);
			return null;
		}
		BoundedRenderer br = getRenderer(tag);
		// tag not found
		if (br == null)
		{
			return null;
		}
		if (br instanceof RendererPanel)
		{
			return (RendererPanel) br;
		}
		return null;
	}
	
	/** Get the BoundedRenderer with the specified tag from this panel.
	 * @param tag the tag specified with the element when it was added
	 * @return the BoundedRenderer, or null if there is no element with that
	 * 		name/tag
	 */
	public synchronized BoundedRenderer getRenderer(String tag)
	{
		if (tag == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Specified null argument when "
					+ " attempting to get a bounded renderer from a "
					+ "renderer panel."
					);
			return null;
		}
		BoundedRenderer obj = renderers.get(tag);
		if (obj == null)
		{
			Thread.dumpStack();
			System.out.println(
					"WARNING: Attempted to get \'"
					+ tag
					+ "\' from a RendererPanel not containing an element by "
					+ "that tag."
					);
		}
		return obj;
	}
	
	/** Removes the specified element by tag from this panel.
	 * @param tag the tag the element was added with
	 */
	public synchronized void removeRenderer(String tag)
	{
		if (tag == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Specified null argument when "
					+ " attempting to remove a bounded renderer from a "
					+ "renderer panel."
					);
			return;
		}
		// Get & remove the object from the named list
		BoundedRenderer obj = renderers.remove(tag);
		// Object not found; don't try to remove from positioning map
		if (obj == null)
		{
			return;
		}
		// Iterate over the rows to find the element
		for (LinkedList<BoundedRenderer> row : rendererPositioning)
		{
			// Try to remove the element
			row.remove(obj);
		}
	}
	
	/** Adds the specified BoundedRenderer at the specified position. Creates
	 * additional rows above or below current rows as needed.
	 * @param obj the BoundedRenderer to add
	 * @param name the name/tag to use for accessing the added element
	 * @param rowInsert the row to insert the element in
	 * @param colInsert the column (position within the row technically) to
	 * 		insert the new element in
	 */
	protected synchronized void addRenderer(BoundedRenderer obj, String name, int rowInsert, int colInsert)
	{
		// The finalized row values to use (after a few adjustments)
		int fRowIns = rowInsert;
		int fColIns = colInsert;
		// Invalid positioning values; return
		if (rowInsert == INVALID_ROW || colInsert == INVALID_COLUMN)
		{
			System.out.println(
					"WARNING: Unable to locate an element in a RendererPanel to "
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
		else if (rowInsert > rendererPositioning.size() - 1)
		{
			fRowIns = rowInsert;
			fColIns = -1;
			// Add new rows until there are enough
			for (int i = rowInsert; i > rendererPositioning.size() - 1; --i)
			{
				insertRow(rendererPositioning.size());
			}
		}
		// Make sure row is kept valid
		if (fRowIns < 0)
		{
			fRowIns = 0;
		}
		// Remove any placeholder elements from the new row
		if (!renderers.containsValue(rendererPositioning.get(fRowIns).getFirst()))
		{
			rendererPositioning.get(fRowIns).removeFirst();
		}
		// Limit the column value to prevent index exceptions
		if (colInsert > rendererPositioning.get(fRowIns).size())
		{
			fColIns = rendererPositioning.get(fRowIns).size();
		}
		if (fColIns < 0)
		{
			fColIns = rendererPositioning.get(fRowIns).size();
		}
		// Add to the positioning lists
		rendererPositioning.get(fRowIns).add(fColIns, obj);
		// Add to the tagged list (for easier access)
		renderers.put(name, obj);
		// Update positions to prevent glitchy positioning
		updatePositions();
		updateDims();
	}
	
	/** Update the absolute positions of contained elements. */
	protected synchronized void updatePositions()
	{
		// Remove any completely empty rows
		rendererPositioning.removeIf(
				(LinkedList<BoundedRenderer> row)->
				{
					return row.isEmpty();
				}
				);
		// Iterate over rows
		for (int r = 0; r < rendererPositioning.size(); ++r)
		{
			// Get the actual row
			LinkedList<BoundedRenderer> row = rendererPositioning.get(r);
			// Empty row; ignore it
			if (row.isEmpty())
			{
				continue;
			}
			// Iterate over the columns (individual GUIObjects)
			for (int c = 0; c < rendererPositioning.get(r).size(); ++c)
			{
				// Get the actual object
				BoundedRenderer obj = row.get(c);
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
					BoundedRenderer previous = row.get(c-1);
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
					BoundedRenderer aboveFirst =
							rendererPositioning.get(r-1).getFirst();
					if (aboveFirst == null)
					{
						System.out.println(
								"INTERNAL WARNING: "
								+ "RendererPanel alignment error #1 "
								+ "(position update)"
								);
						continue;
					}
					int newY = 
							aboveFirst.getY()
							+ getRowHeight(r-1)
							+ getRowSpacing();
					// Same as panel
					int newX = getX();
					obj.setPos(newX, newY);
				}
				else if (r > 0 && c > 0)
				{
					// Get previous element in row
					BoundedRenderer previous = row.get(c-1);
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
	
	/** Update the dimensions of the panel to fit to its contents. */
	protected void updateDims()
	{
		int width = 0;
		int height = 0;
		for (int i = 0; i < rendererPositioning.size(); ++i)
		{
			int rw = getRowWidth(i);
			if (rw > width)
			{
				width = rw;
			}
			height += getRowHeight(i);
			if (i != rendererPositioning.size() - 1)
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
	protected void insertRow(int insertAt)
	{
		rendererPositioning.add(insertAt, new LinkedList<BoundedRenderer>());
		rendererPositioning.get(insertAt).add(new Placeholder(0,0,100,1));
	}
	
	/** Gets the height of the specified row, which is equal to the height of
	 * the highest element in the specified row (does not include the padding/
	 * spacing around the row.)
	 * @param row the row to check the height of
	 * @return the height of the row, based on the highest element in the row
	 */
	protected int getRowHeight(int row)
	{
		// Invalid row; return 0
		if (row >= rendererPositioning.size())
		{
			return 0;
		}
		// Get the row
		LinkedList<BoundedRenderer> r = rendererPositioning.get(row);
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
			for (BoundedRenderer obj : r)
			{
				if (obj.getHeight() > maxHeight)
				{
					maxHeight = obj.getHeight();
				}
			}
			return maxHeight;
		}
	}
	
	/** Calculate the width of the specified row.
	 * @param row the index of the row to measure (in the positioning grid)
	 * @return the width of the specified row
	 */
	protected int getRowWidth(int row)
	{
		BoundedRenderer obj = rendererPositioning.get(row).getLast();
		return obj.getX() - getX() + obj.getWidth();
	}
}
