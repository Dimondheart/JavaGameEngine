package game;

import static java.awt.event.MouseEvent.BUTTON1;

import java.awt.Color;

import core.graphics.GfxManager;
import core.graphics.RenderEvent;
import core.graphics.TextDrawer;
import core.userinput.InputManager;
import core.userinput.inputdevice.gui.GUIPanel;

@SuppressWarnings("javadoc")
public class GUIPanelTester implements core.graphics.Renderer
{
	private GUIPanel panel;
	private int x;
	private int y;
	private boolean isSelected;
	
	public GUIPanelTester(GUIPanel panel)
	{
		this.panel = panel;
		x = panel.getX()-4;
		y = panel.getY()-4;
		isSelected = false;
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		event.getContext().setColor(Color.white);
		event.getContext().fillOval((int)x-1, (int)y-1, 3, 3);
		TextDrawer.drawText(
				event.getContext(),
				"Click & Drag the blue dot to move the GUI panel",
				x,
				y-20
				);
		event.getContext().setColor(Color.blue);
		event.getContext().fillOval(x-5, y-5, 10, 10);
	}
	
	public synchronized void update()
	{
		int x = InputManager.getMS().getUnpolledX();
		int y = InputManager.getMS().getUnpolledY();
		double dist = Math.pow((double)(Math.pow(x-this.x, 2)+Math.pow(y-this.y, 2)), 0.5);
		if (dist < 6 && InputManager.getMS().justPressed(BUTTON1))
		{
			isSelected = true;
		}
		if (isSelected && InputManager.getMS().isDown(BUTTON1))
		{
			this.x = x;
			this.y = y;
			panel.setBGColor(Color.blue);
		}
		else
		{
			isSelected = false;
			panel.setBGColor(null);
		}
		panel.setPos(this.x+4, this.y+4);
	}
}
