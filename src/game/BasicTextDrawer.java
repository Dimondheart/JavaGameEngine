package game;

import core.graphics.GfxManager;
import core.graphics.RenderEvent;
import core.graphics.TextDrawer;

public class BasicTextDrawer implements core.graphics.Renderer
{
	private String[] toDraw;
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		for (int i = 0; i < toDraw.length; ++i)
		{
			String line = toDraw[i];
			int x =
					GfxManager.getMainLayerSet().getLayerSetWidth()
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
