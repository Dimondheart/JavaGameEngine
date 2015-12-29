package core.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

/** Manages all graphics/image files and relevant data.
 * @author Bryan Bettis
 */
public class GraphicsResources extends core.GameResourcesBuffer
{
	/** The root directory of all graphics resources. */
	private static final String ROOT_DIR = RES_DIR + "graphics/";
	/** List of supported extensions for image files. */
	private static final String[] EXT_SUPPORTED = {
			".png",
			".bmp",
			".jpg",
			".jpeg"
			};
	
	/** All the graphics, associated with their relative path in the graphics
	 * resources folder.
	 */
	private static ConcurrentHashMap<String, BufferedImage> graphics;
	
	/** Basic constructor. */
	public GraphicsResources()
	{
		graphics = new ConcurrentHashMap<String, BufferedImage>();
		// Preload all graphics
		loadAll();
	}
	
	/** Loads all graphics from the game graphics resources folder, including
	 * any sub-directories.
	 */
	@Override
	public void loadAll()
	{
		loadAll(ROOT_DIR, "graphics");
	}
	
	/** Loads an image given the path relative to GraphicsResources.ROOT_DIR.
	 * @param imagePath the relative path to the image file
	 * @throws IOException if the specified file path was not a valid image
	 */
	@Override
	public void load(String imagePath)
	{
		BufferedImage image = null;
		// File is not a supported format
		if (!extensionSupported(EXT_SUPPORTED, imagePath))
		{
			return;
		}
		// Get an input stream for the file
		InputStream is = getInputStream("/" + ROOT_DIR + imagePath);
		// Try to read the image
		try
		{
			image = ImageIO.read(is);
		}
		catch (IOException e)
		{
			return;
		}
		// Add a successfully loaded graphic to the map
		graphics.put(imagePath, image);
	}
	
	@Override
	public BufferedImage getRes(String graphic)
	{
		return graphics.getOrDefault(graphic, null);
	}
	
	@Override
	public boolean resExists(String graphic)
	{
		return graphics.containsKey(graphic);
	}
}
