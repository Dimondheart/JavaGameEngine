package main.graphics;

/** The base class for any object that will be rendered to the screen. */
public interface Renderer
{
	/** Called to draw a renderer to the window. */
	public abstract void render(RenderEvent event);
	
	/** Show the renderer.
	 * @param layer the layer to show in
	 */
	public default void show(int layer)
	{
		main.graphics.GfxManager.showRenderer(this, layer);
	}
	
	/** Shows the renderer in only the specified layer, hides it in all
	 * other layers.
	 * @param layer the only layer to show in
	 */
	public default void showOnly(int layer)
	{
		main.graphics.GfxManager.hideRenderer(this);
		main.graphics.GfxManager.showRenderer(this, layer);
	}
	
	/** Hides a renderer in all layers. */
	public default void hide()
	{
		main.graphics.GfxManager.hideRenderer(this);
	}
	
	/** Hides a renderer in only the specified layer.
	 * @param layer the layer to hide this renderer in
	 */
	public default void hide (int layer)
	{
		main.graphics.GfxManager.hideRenderer(this, layer);
	}
	
	/** Get what layer(s) a renderer is in. */
	public default int[] getLayers()
	{
		// TODO implement
		int[] layers = {};
		return layers;
	}
	
	/** Cleans up a renderer before it is no longer used. */
	public default void destroy()
	{
		GfxManager.hideRenderer(this);
	}
}
