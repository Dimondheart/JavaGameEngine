package game;

import core.graphics.FrameAnimator;
import core.graphics.RenderEvent;
import core.graphics.Renderer;

@SuppressWarnings("javadoc")
public class TestAnimator implements Renderer
{
	private FrameAnimator animate;
	
	public TestAnimator(String location, String startingSet)
	{
		animate = new FrameAnimator(location, startingSet);
		animate.setLooping(true);
	}

	@Override
	public void render(RenderEvent e)
	{
		animate.renderAnimation(e, 100, 100);
	}
}
