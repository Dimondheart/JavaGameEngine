package game;

import java.awt.Color;

import core.graphics.RenderEvent;
import core.graphics.TextDrawer;
import core.userinput.inputdevice.gui.GUIPanel;

@SuppressWarnings("javadoc")
public class GUIPanelTester extends MouseMotionTester
{
	private GUIPanel panel;
	
	public GUIPanelTester(GUIPanel panel)
	{
		this.panel = panel;
		this.x = panel.getX()-4;
		this.y = panel.getY()-4;
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		event.getContext().setColor(Color.white);
		event.getContext().fillOval((int)x-1, (int)y-1, 3, 3);
		String toDraw = "Click & Drag the blue dot to move the GUI panel";
		TextDrawer.drawText(event.getContext(), toDraw, x, y-20);
		event.getContext().setColor(Color.blue);
		event.getContext().fillOval(x-5, y-5, 10, 10);
	}
	
	@Override
	public synchronized void update()
	{
		super.update();
		panel.setPos(x+4, y+4);
	}
}
