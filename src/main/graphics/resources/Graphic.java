package main.graphics.resources;

import java.awt.image.BufferedImage;

public class Graphic
{
	private BufferedImage graphic;
	
	public Graphic()
	{
		graphic = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
	}
	
	public BufferedImage getGraphic()
	{
		return graphic;
	}
}
