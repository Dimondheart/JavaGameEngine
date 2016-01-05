package core.graphics.gui;

/** Base class for user inputs that are rendered on the screen and the use
 * interacts with using input devices (mouse, keyboard, etc.).
 * @author Bryan Bettis
 */
public abstract class GUIObject implements core.graphics.PrimaryRenderer
{
	/** The x coordinate of this object. */
	protected int x;
	/** The y coordinate of this object. */
	protected int y;
	/** The width of this object. */
	protected int width;
	/** The height of this object. */
	protected int height;
	
	/** Update the state-related information of a GUI element. */
	public abstract void update();
	
	/** Update the position of this element.
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 */
	public void setPos(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	/** Update the x coordinate of this element.
	 * @param x the new x coordinate
	 */
	public void setX(int x)
	{
		this.x = x;
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
	
	/** Update the width of this element.
	 * @param width the new width
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	/** Update the height of this element.
	 * @param height the new height
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/** Get the x coordinate.
	 * @return the screen x coordinate
	 */
	public int getX()
	{
		return x;
	}
	
	/** Get the y coordinate.
	 * @return the screen y coordinate
	 */
	public int getY()
	{
		return y;
	}
	
	/** Get the width.
	 * @return the width on the screen
	 */
	public int getWidth()
	{
		return width;
	}
	
	/** Get the height.
	 * @return the height on the screen
	 */
	public int getHeight()
	{
		return height;
	}
}
