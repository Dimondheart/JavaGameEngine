package core.graphics;

/** A Renderer is an object that can draw to the screen using the game
 * engine's setup.
 * TODO add more documentation here.
 * @author Bryan Bettis
 */
public interface Renderer
{
	/** Called to draw a renderer to the window.
	 * @param event the context information used to render
	 * @see RenderEvent
	 */
	public abstract void render(RenderEvent event);
	
	/** The layer(s) this Renderer is currently showing on.
	 * TODO implement this, it currently only returns an empty array
	 * TODO make this be able to get what layers this is on from all
	 * 		layer sets and relate the layer set object to the layer indexes
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
		GfxManager.getMainLayerSet().recursiveRemoveRenderer(this);
	}
}
