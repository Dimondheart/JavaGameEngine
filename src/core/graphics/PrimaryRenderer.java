package core.graphics;

/** A type of Renderer that draws directly to the primary set of
 * layers.
 * @author Bryan Bettis
 */
public interface PrimaryRenderer
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
		GfxManager.showRenderer(this, layer);
	}
	
	/** Shows the renderer in only the specified layer, hides it in all
	 * other layers.
	 * @param layer the only layer to show in
	 */
	public default void showOnlyOnLayer(int layer)
	{
		GfxManager.hideRenderer(this);
		GfxManager.showRenderer(this, layer);
	}
	
	/** Hides a renderer in all layers. */
	public default void hideOnAllLayers()
	{
		GfxManager.hideRenderer(this);
	}
	
	/** Hides a renderer in only the specified layer.
	 * @param layer the layer to hide this renderer in
	 */
	public default void hideOnLayer(int layer)
	{
		GfxManager.hideRenderer(this, layer);
	}
	
	/** The layer(s) this Renderer is currently showing on.
	 * TODO implement this, it currently only returns an empty array
	 * @return array of integers corresponding to layer indexes
	 */
	public default int[] getCurrentLayers()
	{
		int[] layers = {};
		return layers;
	}
	
	/** Cleans up a renderer before it is no longer used. */
	public default void destroy()
	{
		GfxManager.hideRenderer(this);
	}
}
