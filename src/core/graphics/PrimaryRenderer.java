package core.graphics;

/** A type of Renderer that draws directly to the main set of
 * layers. This is just a simplified alternative to using LayerSet
 * methods on GfxManager.getMainLayerSet()
 * @author Bryan Bettis
 */
public interface PrimaryRenderer extends Renderer
{
	/** Add this Renderer to the specified layer. A renderer can be added
	 * to multiple layers, and when its render function is called it
	 * is told what layer is being rendered at that time. Note that a
	 * renderer instance can only be added once per layer.
	 * @param layer the layer to add this renderer to
	 */
	public default void showOnLayer(int layer)
	{
		GfxManager.getMainLayerSet().addRenderer(this, layer);
	}
	
	/** Shows the renderer in only the specified layer, hides it in all
	 * other layers.
	 * @param layer the only layer to show in
	 */
	public default void showOnlyOnLayer(int layer)
	{
		GfxManager.getMainLayerSet().removeRenderer(this);
		GfxManager.getMainLayerSet().addRenderer(this, layer);
	}
	
	/** Hides a renderer in all layers. */
	public default void hideOnAllLayers()
	{
		GfxManager.getMainLayerSet().removeRenderer(this);
	}
	
	/** Removes this Renderer only from the specified layer.
	 * @param layer the layer to hide this renderer in
	 */
	public default void hideOnLayer(int layer)
	{
		GfxManager.getMainLayerSet().removeRenderer(this, layer);
	}
}
