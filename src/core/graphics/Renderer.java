package core.graphics;

/** The base class for any object that can be rendered to the screen.
 * @author Bryan Bettis
 */
public interface Renderer
{
	/** Called to draw a renderer to the window.
	 * @param event the context information used to render
	 * @see RenderEvent
	 */
	public abstract void render(RenderEvent event);
	
	/** Add this Renderer to the specified layer. A renderer can be added
	 * to multiple layers, and when its render function is called it
	 * is told what layer is being rendered at that time. Note that a
	 * renderer instance can only be added once per layer.
	 * @param layer the layer to add this renderer to
	 */
	public default void showOnLayer(int layer)
	{
		core.graphics.GfxManager.showRenderer(this, layer);
	}
	
	/** Shows the renderer in only the specified layer, hides it in all
	 * other layers.
	 * @param layer the only layer to show in
	 */
	public default void showOnlyOnLayer(int layer)
	{
		core.graphics.GfxManager.hideRenderer(this);
		core.graphics.GfxManager.showRenderer(this, layer);
	}
	
	/** Hides a renderer in all layers. */
	public default void hideOnAllLayers()
	{
		core.graphics.GfxManager.hideRenderer(this);
	}
	
	/** Hides a renderer in only the specified layer.
	 * @param layer the layer to hide this renderer in
	 */
	public default void hideOnLayer(int layer)
	{
		core.graphics.GfxManager.hideRenderer(this, layer);
	}
	
	/** The layer(s) this Renderer is currently showing on.
	 * @return array of integers corresponding to layer indexes
	 */
	public default int[] getCurrentLayers()
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
