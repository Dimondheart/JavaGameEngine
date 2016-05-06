package xyz.digitalcookies.objective.graphics;

import java.awt.Graphics2D;

/** TODO Document
 * @author Bryan Charles Bettis
 */
public abstract class BoundedRenderer implements Renderer
{
	/** The x coordinate of this object. */
	private int x;
	/** The y coordinate of this object. */
	private int y;
	/** The width of this object. */
	private int width;
	/** The height of this object. */
	private int height;
	/** If this bounded renderer is being rendered. */
	private boolean isVisible;
	/** The coordinates to center over when normal center mode is enabled. */
	private int[] autoCenterCoords;
	/** If auto centering over certain coordinates. */
	private boolean autoCenter;
	/** If auto centering over the center of the main game window. */
	private boolean autoCenterOverWindow;
	private boolean enforceBounds;
	
	/** Standard constructor. */
	public BoundedRenderer()
	{
		setPos(0,0);
		setDims(0,0);
		isVisible = true;
		autoCenterCoords = new int[2];
		autoCenter = false;
		autoCenterOverWindow = false;
		enforceBounds = false;
	}
	
	/** Update the position of this element.
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 */
	public void setPos(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	public void centerOver(int x, int y, boolean autoUpdate)
	{
		int nx = x - getWidth()/2;
		int ny = y - getHeight()/2;
		setPos(nx, ny);
		setAutoCenterCoords(nx, ny);
		autoCenter = autoUpdate;
	}
	
	public void centerOverWindow(boolean center, boolean autoUpdate)
	{
		// Initialize coordinates to center over to the current center
		int cx = getX()-getWidth()/2;
		int cy = getY()-getHeight()/2;
		// Should be centered over the main window
		if (center)
		{
			cx = GraphicsManager.getMainLayerSet().getWidth()/2;
			cy = GraphicsManager.getMainLayerSet().getHeight()/2;
		}
		centerOver(cx, cy, autoUpdate);
		autoCenterOverWindow = autoUpdate;
	}
	
	public void centerOverWindow(boolean center)
	{
		centerOverWindow(center, center);
	}
	
	/** Get the x coordinate.
	 * @return the screen x coordinate
	 */
	public int getX()
	{
		return x;
	}
	
	/** Update the x coordinate of this element.
	 * @param x the new x coordinate
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/** Get the y coordinate.
	 * @return the screen y coordinate
	 */
	public int getY()
	{
		return y;
	}
	
	/** Update the y coordinate of this element.
	 * @param y the new y coordinate
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/** Update the dimensions of this element.
	 * @param width the new width
	 * @param height the new height
	 */
	public void setDims(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	/** Get the width.
	 * @return the width on the screen
	 */
	public int getWidth()
	{
		return width;
	}
	
	/** Update the width of this element.
	 * @param width the new width
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	/** Get the height.
	 * @return the height on the screen
	 */
	public int getHeight()
	{
		return height;
	}
	
	/** Update the height of this element.
	 * @param height the new height
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public boolean isVisible()
	{
		return isVisible;
	}
	
	/** Set if this bounded renderer is visible, meaning if it will be called
	 * to render during the render loop.
	 * @param visible true to make this renderer render, false to hide it
	 */
	public void setVisible(boolean visible)
	{
		isVisible = visible;
	}
	
	public boolean isEnforcingBounds()
	{
		return enforceBounds;
	}
	
	public void setEnforceBounds(boolean enforce)
	{
		enforceBounds = enforce;
	}
	
	/** All bounded renderer subclasses should call super.render(event)
	 * from their own render function, if they wish to support features
	 * like auto updating centering coordinates over other bounded renderers
	 * or the main window.
	 */
	@Override
	public void render(RenderEvent event)
	{
		// TODO replace this with a separate protected RenderEvent update(event)
		if (autoCenterOverWindow)
		{
			centerOverWindow(true);
		}
		else if (autoCenter)
		{
			centerOver(true);
		}
		if (!isVisible())
		{
			event.getGC().dispose();
		}
		else if (isEnforcingBounds())
		{
			event.setGC(
					(Graphics2D)
					event.getGC().create(
							getX(),
							getY(),
							getWidth(),
							getHeight()
							)
					);
		}
	}
	
	/** Set the auto center coordinates for when it is enabled.
	 * @param x the x coordinate to center over
	 * @param y the y coordinate to center over
	 */
	private void setAutoCenterCoords(int x, int y)
	{
		autoCenterCoords[0] = x;
		autoCenterCoords[1] = y;
	}
	
	/** A convenience method for centering, for when updating during the
	 * render loop/cycle.
	 * @param autoUpdate if the bounded renderer should continue centering
	 * 		over the set coordinates
	 */
	private void centerOver(boolean autoUpdate)
	{
		centerOver(autoCenterCoords[0], autoCenterCoords[1], autoUpdate);
	}
}
