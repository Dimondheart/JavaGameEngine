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

package xyz.digitalcookies.objective.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/** Manages all sound files and relevant data.
 * @author Bryan Charles Bettis
 */
public class SoundResources extends xyz.digitalcookies.objective.GameResourcesBuffer
{
	/** The root directory of all sound resources. */
	private static final String ROOT_DIR = RES_DIR + "sounds/";
	/** List of supported extensions for sound files. */
	private static final String[] EXT_SUPPORTED = {
			".wav"
			};
	
	/** All the sound data, associated with their relative path in the sounds
	 * resources folder.
	 */
	private static ConcurrentHashMap<String, String> sounds;
	
	/** Preloads all sounds. */
	public SoundResources()
	{
		sounds = new ConcurrentHashMap<String, String>();
		loadAll();
	}
	
	@Override
	public void loadAll()
	{
		loadAll(ROOT_DIR, "sounds");
	}

	@Override
	public void load(String filePath)
	{
		// File is not a supported format
		if (!extensionSupported(EXT_SUPPORTED, filePath))
		{
			return;
		}
		// Get the file input stream to check if it exists
		InputStream is = getInputStream("/" + ROOT_DIR + filePath);
		if (is != null)
		{
			sounds.put(filePath, filePath);
			try
			{
				is.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	@Override
	public InputStream getRes(String sound)
	{
		// If the resource exists, return an input stream for reading it
		if (resExists(sound))
		{
			InputStream is = getInputStream("/" + ROOT_DIR + sound);
			return new BufferedInputStream(is);
		}
		// Resource doesn't exist, return null
		else
		{
			return null;
		}
	}
	
	@Override
	public boolean resExists(String sound)
	{
		return sounds.containsKey(sound);
	}
}
