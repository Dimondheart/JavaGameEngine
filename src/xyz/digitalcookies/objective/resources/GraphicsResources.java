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

package xyz.digitalcookies.objective.resources;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/** Handles the graphics resources managed for the graphics system.
 * @author Bryan Charles Bettis
 */
public class GraphicsResources extends ResourceHandler<BufferedImage>
{
	/** The default image. */
	private final BufferedImage defImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
	
	/** Standard constructor. */
	public GraphicsResources()
	{
		setSupportsBuffering(true);
	}
	
	@Override
	protected BufferedImage loadResource(File toLoad)
	{
		FileInputStream fis = null;
		// Get an input stream for the file
		try
		{
			fis = new FileInputStream(toLoad);
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
		InputStream is = new BufferedInputStream(fis);
		BufferedImage image = null;
		// Try to read the image
		try
		{
			image = ImageIO.read(is);
		}
		catch (IOException e)
		{
			return null;
		}
		return image;
	}
	
	@Override
	protected BufferedImage getDefaultValue()
	{
		return defImg;
	}
}
