package core.graphics;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

/** Manages all graphics/image files and relevant data.
 * @author Bryan Bettis
 */
public class GraphicsResources
{
	/** The root directory of all graphics resources. */
	private static final String ROOT_DIR = "game/resources/graphics/";
	/** List of supported extensions for image files. */
	private static final String[] EXT_SUPPORTED = {
			".png",
			".PNG",
			".bmp",
			".BMP",
			".jpg",
			".JPG",
			".jpeg",
			".JPEG"
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
	public void loadAll()
	{
		System.out.println("Preloading graphics...");
		// The list of relative paths for files to try to load
		LinkedList<String> toLoad = new LinkedList<String>();
		// Get the URI scheme to see if we are in a jar or not
		// TODO Move this to the GameSession class as public constant
		String uriScheme = "";
		try
		{
			uriScheme = GraphicsResources.class.getResource("/core").toURI().getScheme();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return;
		}
		// Loading images from a jar
		if (uriScheme.equals("jar"))
		{
			// Get the URL for the jar file
			URL jarFile = getClass().getProtectionDomain().getCodeSource().getLocation();
			// Open the jar as a zip input stream
			ZipInputStream jar;
			try
			{
				jar = new ZipInputStream(jarFile.openStream());
			}
			catch (IOException e)
			{
				System.out.println("Error opening Jar to locate images.");
				return;
			}
			// Check all entries in the jar to find the ones in ROOT_DIR
			while (true)
			{
				ZipEntry entry;
				// Get the next entry
				try
				{
					entry = jar.getNextEntry();
				}
				catch (IOException e)
				{
					System.out.println("Error finding images in the jar.");
					break;
				}
				// Last entry has been checked, stop looking
				if (entry == null)
				{
					break;
				}
				// Get the internal jar path to the entry
				String name = entry.getName();
				// Entry is in our resource folder and could be an image file
				if (name.contains(ROOT_DIR) && name.contains("."))
				{
					// Add it to the list of files to try to load
					toLoad.add(name.replace(ROOT_DIR, ""));
				}
			}
		}
		// Loading images not in a jar
		else
		{
			// File system paths to check for files
			LinkedList<String> toCheck = new LinkedList<String>();
			// Start off with the root resource directory
			toCheck.add(new File(GraphicsResources.class.getResource("/" + ROOT_DIR).getPath()).getAbsolutePath());
			// Keep checking until out of file system objects to check
			while (!toCheck.isEmpty())
			{
				// Get the next path ot check
				String checking = toCheck.poll();
				// Get a file system object for the path
				File node = new File(checking);
				// Add contents of directories to be checked later
				if (node.isDirectory())
				{
					for (File newCheck : node.listFiles())
					{
						toCheck.addLast(newCheck.getAbsolutePath());
					}
				}
				// Add a file to the list of files to load
				else if (node.isFile())
				{
					toLoad.add(checking.replace(File.separatorChar, '/').split(ROOT_DIR,2)[1]);
				}
			}
		}
		// Try to load all possible image files found
		while (!toLoad.isEmpty())
		{
			String filePath = toLoad.poll();
			try
			{
				load(filePath);
			}
			// Image not found/is not a valid image
			catch (IOException e)
			{
			}
			System.out.println("Preloading graphics..." + toLoad.size());
		}
	}
	
	/** Loads an image given the path relative to GraphicsResources.ROOT_DIR.
	 * @param imagePath the relative path to the image file
	 * @throws IOException if the specified file path was not a valid image
	 */
	public void load(String imagePath) throws IOException
	{
		BufferedImage image = null;
		// File is not a supported format
		if (!isSupportedExtension(imagePath))
		{
			return;
		}
		// Get an input stream for the file
		InputStream is = this.getClass().getResourceAsStream(
				"/" + ROOT_DIR + imagePath
				);
		// Convert to a buffered input stream (needed in jars)
		is = new BufferedInputStream(is);
		// Try to read the image
		image = ImageIO.read(is);
		// Add a successfully loaded graphic to the map
		graphics.put(imagePath, image);
	}
	
	/** Checks if the given file path or file name contains a supported image
	 * format extension.
	 * @param file the path to or name of the file to be checked
	 * @return true if the extension is supported, false otherwise
	 */
	private boolean isSupportedExtension(String file)
	{
		// Check for each supported extension
		for (String se : EXT_SUPPORTED)
		{
			if (file.contains(se))
			{
				return true;
			}
		}
		// Unsupported extension
		return false;
	}
	
	/** Get the specified graphic buffered image data.
	 * @param graphic the path to the file within the resources folder
	 * @return the BufferedImage for the specified graphic, null if not found
	 */
	public BufferedImage getGraphic(String graphic)
	{
		if (graphicExists(graphic))
		{
			return graphics.get(graphic);
		}
		else
		{
			return null;
		}
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
}
