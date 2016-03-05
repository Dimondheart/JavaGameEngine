package game;

import java.awt.Color;

import core.graphics.GfxManager;
import core.graphics.RenderEvent;
import core.graphics.TextDrawer;
import core.userinput.InputManager;

import static java.awt.event.MouseEvent.*;

@SuppressWarnings("javadoc")
public class MouseMotionTester extends SampleRenderer implements core.graphics.Renderer
{
	private boolean isSelected = false;
	
	public MouseMotionTester()
	{
		x = 100;
		y = 150;
	}
	
	@Override
	public void render(RenderEvent event)
	{
		if (event.getLayer() == 9)
		{
			event.getContext().setColor(Color.white);
			event.getContext().fillOval((int)x-1, (int)y-1, 3, 3);
			String toDraw = Integer.toString(x) + "," + Integer.toString(y);
			TextDrawer.drawText(event.getContext(), toDraw, x, y);
		}
		else
		{
			event.getContext().setColor(Color.blue);
			event.getContext().fillOval(x-5, y-5, 10, 10);
		}
	}
	
	@Override
	public void update()
	{
		int x = InputManager.getMS().getUnpolledX();
		int y = InputManager.getMS().getUnpolledY();
		double dist = Math.pow((double)(Math.pow(x-this.x, 2)+Math.pow(y-this.y, 2)), 0.5);
		if (dist < 6 && InputManager.getMS().justPressed(BUTTON1))
		{
			isSelected = true;
			System.out.println(GfxManager.getMainLayerSet().getLayerSetWidth());
		}
		if (isSelected && InputManager.getMS().isDown(BUTTON1))
		{
			this.x = x;
			this.y = y;
		}
		else
		{
			isSelected = false;
		}
	}
}
