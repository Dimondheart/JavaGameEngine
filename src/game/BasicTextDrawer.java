package game;

import xyz.digitalcookies.objective.graphics.GraphicsManager;
import xyz.digitalcookies.objective.graphics.RenderEvent;
import xyz.digitalcookies.objective.graphics.TextDrawer;

@SuppressWarnings("javadoc")
public class BasicTextDrawer implements xyz.digitalcookies.objective.graphics.Renderer
{
	private String[] toDraw;
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		for (int i = 0; i < toDraw.length; ++i)
		{
			String line = toDraw[i];
			int x =
					GraphicsManager.getMainLayerSet().getLayerSetWidth()
					- TextDrawer.getTextWidth(event.getContext(), line)
					- 4;
			int y = i * TextDrawer.getTextHeight(event.getContext(), line);
			int[] coords = {x, y};
			TextDrawer.drawText(event.getContext(), line, coords);
		}
	}
	
	public synchronized void setText(String[] text)
	{
		toDraw = text;
	}
}
