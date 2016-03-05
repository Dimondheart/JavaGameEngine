package game;

import core.graphics.FrameAnimator;
import core.graphics.RenderEvent;

/** Unit test object for frame animator image dimensioning setup. */
public class FrameAnimatorUT1 implements core.graphics.Renderer
{
	private FrameAnimator fa;
	
	public FrameAnimatorUT1()
	{
		fa = new FrameAnimator("bluering");
	}
	
	@Override
	public void render(RenderEvent event)
	{
		// case 1
		test(event, 100, 100);
		// case 2
		test(event, 100, FrameAnimator.ORIGINAL_DIMENSION);
		// case 3
		test(event, 100, FrameAnimator.SCALE_DIMENSION);
		// case 4
		test(event, FrameAnimator.ORIGINAL_DIMENSION, 100);
		// case 5
		test(event, FrameAnimator.ORIGINAL_DIMENSION, FrameAnimator.ORIGINAL_DIMENSION);
		// case 6
		test(event, FrameAnimator.ORIGINAL_DIMENSION, FrameAnimator.SCALE_DIMENSION);
		// case 7
		test(event, FrameAnimator.SCALE_DIMENSION, 100);
		// case 8
		test(event, FrameAnimator.SCALE_DIMENSION, FrameAnimator.ORIGINAL_DIMENSION);
		// case 9
		test(event, FrameAnimator.SCALE_DIMENSION, FrameAnimator.SCALE_DIMENSION);
		// Stop the program so test results can be seen
		System.exit(0);
	}
	
	private void test(RenderEvent e, int width, int height)
	{
		fa.setImageSize(width, height);
		fa.renderAnimation(e, 0, 0);
	}
}
