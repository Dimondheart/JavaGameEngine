package core.graphics;

import java.awt.Dimension;

/** A thin layer over LayerSet, this provides a few changes to a layer
 * set needed for the main layer set to function properly. For example
 * this class removes the functionality of the resizeLayers(width,height)
 * method and adds a resizeLayers(dimensions) used to keep the main layer
 * set sized to fit the window/display area.
 * @author Bryan Bettis
 */
class MainLayerSet extends LayerSet
{
	/** Basic constructor. */
	public MainLayerSet(int numLayers)
	{
		super(numLayers);
	}
	
	@Override
	public synchronized void resizeLayers(int width, int height)
	{
		System.out.println(
				"WARNING: Attempted to resize the main layers " +
						"outside the graphics manager."
				);
	}
	
	/** Resize the layers, used to only resize the main layers when the
	 * window is resized.
	 * @param newDims the new dimensions of the main layers
	 */
	synchronized void resizeLayers(Dimension newDims)
	{
		super.resizeLayers(newDims.width, newDims.height);
	}

}
