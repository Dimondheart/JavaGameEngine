package main.graphics;

import java.awt.Graphics2D;

public class TestAnimator extends Animator
{

	public TestAnimator(int layer, String location)
	{
		super(layer, location);
	}

	@Override
	public void render(Graphics2D g)
	{
		this.renderAnimation(g, 100, 100);
	}
}
