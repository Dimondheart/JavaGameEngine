package game;

import core.graphics.Animator;
import core.graphics.RenderEvent;

public class TestAnimator extends Animator
{
	public TestAnimator(String location, String startingSet)
	{
		super(location, startingSet);
	}

	@Override
	public void render(RenderEvent e)
	{
		this.renderAnimation(e.getContext(), 100, 100);
	}
}
