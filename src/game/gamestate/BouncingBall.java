package game.gamestate;

import java.util.concurrent.ConcurrentHashMap;

public class BouncingBall extends core.gamestate.GameState
{
	private game.Ball ball1;
	
	@Override
	protected void setupState(ConcurrentHashMap<String, Object> args)
	{
		ball1 = new game.Ball(100.0,121.0,1.0,-1.0);
		core.graphics.GfxManager.getMainLayerSet().addRenderer(ball1, 0);
	}

	@Override
	protected void cycleState()
	{
		ball1.update();
	}

	@Override
	protected void cleanupState()
	{
		ball1.destroy();
	}

}
