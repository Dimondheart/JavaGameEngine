/** Copyright 2016 Bryan Charles Bettis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.digitalcookies.objective.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import xyz.digitalcookies.objective.DevConfig;

/** Manages all graphics/image files and relevant data.
 * @author Bryan Charles Bettis
 */
public class GraphicsResources extends xyz.digitalcookies.objective.GameResourcesBuffer
{
	/** The root directory of all graphics resources. */
	private static final String ROOT_DIR = 
			(String)
			DevConfig.getSetting(DevConfig.GRAPHICS_RES_DIR);
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
	
	@Override
	public void loadAll()
	{
		loadAll(ROOT_DIR, "graphics");
	}
	
	/** Loads an image given the path relative to GraphicsResources.ROOT_DIR.
	 * @param imagePath the relative path to the image file
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
