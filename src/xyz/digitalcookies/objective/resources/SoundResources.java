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
import java.io.InputStream;

/** TODO Document
 * @author Bryan Charles Bettis
 */
public class SoundResources extends ResourceHandler<InputStream>
{
	public SoundResources()
	{
		super();
		setSupportsBuffering(false);
	}
	
	@Override
	protected InputStream loadResource(File toLoad)
	{
		System.out.println("Loading sound resource \'" + toLoad.getPath() + "\'...");
		BufferedImage image = null;
		FileInputStream fis = null;
		// Get an input stream for the file
		try
		{
			fis = new FileInputStream(toLoad);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("ERROR LOADING GRAPHIC FILE: File not found");
			return null;
		}
		// Return a buffered input stream
		return new BufferedInputStream(fis);
	}
	
	@Override
	protected InputStream getDefaultValue()
	{
		return null;
	}
}
