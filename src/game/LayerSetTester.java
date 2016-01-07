package game;

import java.awt.Color;

import core.graphics.LayerSet;

public class LayerSetTester
{
	public LayerSet layers;
	
	public LayerSetTester()
	{
		layers = new LayerSet(4);
		layers.addRenderer(new LayerSetTesterObject(200,0, Color.BLUE), 0);
		layers.addRenderer(new LayerSetTesterObject(205,5, Color.RED), 1);
		layers.addRenderer(new LayerSetTesterObject(210,10, Color.YELLOW), 2);
		layers.addRenderer(new LayerSetTesterObject(215,15, Color.GREEN), 3);
		core.graphics.GfxManager.getMainLayerSet().addRenderer(layers, 3);
	}
}
