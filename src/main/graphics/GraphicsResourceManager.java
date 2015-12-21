package main.graphics;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

/** Manages all graphics/image files and relevant data.
 * @author Bryan Bettis
 */
public class GraphicsResourceManager
{
	
	private static ConcurrentHashMap<String, Graphic> graphics;
	
	/** Basic constructor. */
	public GraphicsResourceManager()
	{
		graphics = new ConcurrentHashMap<String, Graphic>();
		try
		{
			Graphic sample = new Graphic("asteroid.png");
			graphics.put("asteroid.png", sample);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Get the specified graphic buffered image data.
	 * @param graphic the path to the file within the resources folder
	 * @return the BufferedImage for the specified graphic, null if not found
	 */
	public BufferedImage getGraphic(String graphic)
	{
		return graphics.get(graphic).getGraphic();
//		if (graphicExists(graphic))
//		{
//			return graphics.get(graphic).getGraphic();
//		}
//		else
//		{
//			return null;
//		}
	}
	
	/** Check if the specified graphic exists. */
	public boolean graphicExists(String graphic)
	{
		if (graphics.containsKey(graphic))
		{
			return true;
		}
		return false;
	}
	
	/** Contains all info related to a particular graphic. */
	private class Graphic
	{
		/** The image data for this graphic. */
		private BufferedImage image;
		
		/** Basic constructor, loads the graphic.
		 * @param filePath path to the file relative to the resources directory
		 * @throws IOException If the specified file could not be found
		 */
		public Graphic(String filePath) throws IOException
		{
			loadGraphic(filePath);
		}
		
		/** Loads the graphic data from the file.
		 * @param filePath path to the file relative to the resources directory
		 * @throws IOException If the specified file could not be found
		 */
		public void loadGraphic(String filePath) throws IOException
		{
			InputStream is = this.getClass().getResourceAsStream(
					"/game/resources/graphics/" + filePath
					);
			// Convert to a buffered input stream
			is = new BufferedInputStream(is);
			image = ImageIO.read(is);
		}
		
		/** Gets the image data for this graphic.
		 * @return the BufferedImage for this graphic
		 */
		public BufferedImage getGraphic()
		{
			return image;
		}
	}
}
