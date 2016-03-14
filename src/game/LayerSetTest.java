package game;

import java.awt.Color;

import xyz.digitalcookies.objective.graphics.GraphicsManager;
import xyz.digitalcookies.objective.graphics.RenderEvent;

@SuppressWarnings("javadoc")
public class LayerSetTest
{
	protected xyz.digitalcookies.objective.graphics.LayerSet layers;
	private TestObject to1 = new TestObject(200,100,Color.white, 200);
	private TestObject to2 = new TestObject(250,150,Color.cyan, 100);
	
	public LayerSetTest()
	{
		layers = new xyz.digitalcookies.objective.graphics.LayerSet(4);
		layers.addRenderer(new TestObject(100,100,Color.blue), 0);
		layers.addRenderer(new TestObject(110,110,Color.red), 1);
		layers.addRenderer(new TestObject(120,120,Color.yellow), 2);
		layers.addRenderer(new TestObject(130,130,Color.green), 3);
		GraphicsManager.getMainLayerSet().addRenderer(layers, 1);
		GraphicsManager.getMainLayerSet().addRenderer(to1, 3);
		GraphicsManager.getMainLayerSet().addRenderer(to2, 5);
		
	}
	
	private class TestObject implements xyz.digitalcookies.objective.graphics.Renderer
	{
		private int x;
		private int y;
		private int size;
		private Color color;
		
		public TestObject(int x, int y, Color color)
		{
			this(x,y,color,100);
		}
		
		public TestObject(int x, int y, Color color, int size)
		{
			this.x = x;
			this.y = y;
			this.color = color;
			this.size = size;
		}

		@Override
		public void render(RenderEvent event)
		{
			event.getContext().setColor(color);
			event.getContext().fillRect(x, y, size, size);
		}
	}
	
	public void cleanup()
	{
		layers.destroy();
		to1.destroy();
		to2.destroy();
	}
}
