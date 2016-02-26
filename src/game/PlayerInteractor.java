package game;

import java.awt.Color;
import java.awt.event.MouseEvent;

import core.entity.EntityUpdateEvent;
import core.graphics.RenderEvent;
import core.userinput.InputManager;

public class PlayerInteractor extends game.Repulsor
{
	public PlayerInteractor()
	{
		// Make sure parent constructor called first
		super();
		pulseAnimator.setLooping(false);
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		event.getContext().setColor(Color.blue);
		event.getContext().fillOval(getX()-6, getY()-6, 12, 12);
		super.render(event);
	}
	
	@Override
	public synchronized void update(EntityUpdateEvent event)
	{
		super.update(event);
		if (InputManager.getMS().isDown(MouseEvent.BUTTON1))
		{
			activateRepulsion();
		}
		else
		{
			deactivateRepulsion();
		}
		setCoords(InputManager.getMS().getMouseX(), InputManager.getMS().getMouseY());
	}
}
