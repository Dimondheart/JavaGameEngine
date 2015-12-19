package main.graphics;

public class TestAnimator extends Animator
{
	public TestAnimator(int layer, String location)
	{
		super(layer, location);
	}

	@Override
	public void render(RenderEvent e)
	{
		this.renderAnimation(e.getContext(), 100, 100);
	}
}
