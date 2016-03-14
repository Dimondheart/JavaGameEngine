package game;

import java.awt.event.MouseEvent;

import xyz.digitalcookies.objective.entity.EntityUpdateEvent;
import xyz.digitalcookies.objective.graphics.RenderEvent;
import xyz.digitalcookies.objective.input.InputManager;

@SuppressWarnings("javadoc")
public class PlayerInteractor extends game.Repulsor
{
	public PlayerInteractor()
	{
		// Make sure parent constructor called first
		super();
		autoRepulse = false;
		pulseAnimator.setLooping(false);
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
//		event.getContext().setColor(Color.blue);
//		event.getContext().fillOval(((int) body.getX())-6, ((int) body.getY())-6, 12, 12);
		super.render(event);
	}
	
	@Override
	public synchronized void update(EntityUpdateEvent event)
	{
		body.setVector(0, 0);
		health = 35;
		super.update(event);
		health = 35;
		if (InputManager.getMS().justReleased(MouseEvent.BUTTON1))
		{
			if (isRepulsing)
			{
				deactivateRepulsion();
				autoRepulse = false;
			}
			else
			{
				activateRepulsion();
				autoRepulse = true;
			}
		}
		if (InputManager.getMS().justReleased(MouseEvent.BUTTON2))
		{
			event.getEntities().addEntity(
					new Enemy(InputManager.getMS().getUnpolledX(), InputManager.getMS().getUnpolledY(), 0 ,0, 10)
					);
		}
		if (InputManager.getMS().justReleased(MouseEvent.BUTTON3))
		{
			Repulsor nr = new Repulsor();
			nr.getBody().setPos(body.getX(), body.getY());
			event.getEntities().addEntity(nr);
		}
		body.setPos(InputManager.getMS().getUnpolledX(), InputManager.getMS().getUnpolledY());
	}
}
