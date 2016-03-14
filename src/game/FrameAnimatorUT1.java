package game;

import xyz.digitalcookies.objective.graphics.FrameAnimator;
import xyz.digitalcookies.objective.graphics.RenderEvent;

@SuppressWarnings("javadoc")
public class FrameAnimatorUT1 implements xyz.digitalcookies.objective.graphics.Renderer
{
	private FrameAnimator fa;
	
	public FrameAnimatorUT1()
	{
		fa = new FrameAnimator("bluering");
	}
	
	@Override
	public void render(RenderEvent event)
	{
		System.out.println(
				"Testing FrameAnimator Dimensioning with width:100 height:150"
				);
		// case 1
		test(event, 100, 150);
		// case 2
		test(event, 100, FrameAnimator.SpecialDimension.ORIGINAL);
		// case 3
		test(event, 100, FrameAnimator.SpecialDimension.SCALE);
		// case 4
		test(event, FrameAnimator.SpecialDimension.ORIGINAL, 150);
		// case 5
		test(event, FrameAnimator.SpecialDimension.ORIGINAL, FrameAnimator.SpecialDimension.ORIGINAL);
		// case 6
		test(event, FrameAnimator.SpecialDimension.ORIGINAL, FrameAnimator.SpecialDimension.SCALE);
		// case 7
		test(event, FrameAnimator.SpecialDimension.SCALE, 150);
		// case 8
		test(event, FrameAnimator.SpecialDimension.SCALE, FrameAnimator.SpecialDimension.ORIGINAL);
		// case 9
		test(event, FrameAnimator.SpecialDimension.SCALE, FrameAnimator.SpecialDimension.SCALE);
		// Stop the program so test results can be seen
		System.exit(0);
	}
	
	private void test(RenderEvent e, int width, int height)
	{
		fa.setImageSize(width, height);
		fa.renderAnimation(e, 0, 0);
	}
	
	private void test(RenderEvent e, FrameAnimator.SpecialDimension width, int height)
	{
		fa.setImageSize(width, height);
		fa.renderAnimation(e, 0, 0);
	}
	
	private void test(RenderEvent e, int width, FrameAnimator.SpecialDimension height)
	{
		fa.setImageSize(width, height);
		fa.renderAnimation(e, 0, 0);
	}
	
	private void test(RenderEvent e, FrameAnimator.SpecialDimension width, FrameAnimator.SpecialDimension height)
	{
		fa.setImageSize(width, height);
		fa.renderAnimation(e, 0, 0);
	}
}
