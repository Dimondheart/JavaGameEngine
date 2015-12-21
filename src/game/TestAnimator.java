package game;

/** A test of the basic frame-based animation.
* @author Bryan Bettis
*/
import main.graphics.Animator;
import main.graphics.RenderEvent;

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
