package game;

import java.awt.Color;
import java.util.LinkedList;

import core.graphics.RenderEvent;
import core.graphics.TextDrawer;
import core.userinput.InputManager;

import static java.awt.event.MouseEvent.*;

@SuppressWarnings("javadoc")
public class MouseMotionTester extends SampleRenderer implements core.graphics.Renderer
{
	private boolean isSelected = false;
	private LinkedList<SampleRenderer> minions;
	
	public MouseMotionTester()
	{
		minions = new LinkedList<SampleRenderer>();
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
		int x = InputManager.getMS().getMouseX();
		int y = InputManager.getMS().getMouseY();
		double dist = Math.pow((double)(Math.pow(x-this.x, 2)+Math.pow(y-this.y, 2)), 0.5);
		for (SampleRenderer minion : minions)
		{
			minion.update();
		}
		if (dist < 6 && InputManager.getMS().justPressed(BUTTON3))
		{
			isSelected = true;
		}
		if (isSelected && InputManager.getMS().isDown(BUTTON3))
		{
			this.x = x;
			this.y = y;
			if (InputManager.getMS().justPressed(BUTTON1))
			{
				int randomizer = minions.size();
				SampleRenderer newMinion;
				if (randomizer % 2 == 0)
				{
					newMinion = new SampleRenderer(x,y,-1,-1);
				}
				else if (randomizer % 3 == 0)
				{
					newMinion = new SampleRenderer(x,y,-1,1);
				}
				else if (randomizer % 5 == 0)
				{
					newMinion = new SampleRenderer(x,y,1,1);
				}
				else
				{
					newMinion = new SampleRenderer(x,y,1,-1);
				}
				minions.add(newMinion);
				game.gamestate.SamplePlay.entityLayers.addRenderer(newMinion, 1);
			}
		}
		else
		{
			isSelected = false;
		}
	}
	
	@Override
	public void destroy()
	{
		for (SampleRenderer minion : minions)
		{
			minion.destroy();
		}
		super.destroy();
	}
}
