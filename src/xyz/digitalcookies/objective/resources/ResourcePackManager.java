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

import java.io.File;
import java.net.URI;
import java.util.LinkedList;

/** TODO Document
 * @author Bryan Charles Bettis
 *
 */
public class ResourcePackManager
{
	private static String defPack = null;
	private static String currPack = null;
	private static boolean isBuffering = false;
	private static String resPackDir = null;
	private static LinkedList<String> packs = new LinkedList<String>();
	
	/** Constructor hidden to prevent instantiation. */
	private ResourcePackManager()
	{
	}
	
	public static String getResPackDir()
	{
		return resPackDir;
	}
	
	public static void indexResourcePacks(String resDir)
	{
		File dir = new File(resDir);
		if (dir.isDirectory())
		{
			packs.clear();
			resPackDir = resDir;
			for (File file : dir.listFiles())
			{
				System.out.println(file.getName());
				packs.add(file.getName());
			}
		}
		else
		{
			System.out.println(
					"ERROR: Specified resource location is not a directory."
					);
			Thread.dumpStack();
		}
	}
	
	public static String getDefaultPack()
	{
		return defPack;
	}
	
	public static void setDefaultPack(String packName)
	{
		if (packName != null)
		{
			// TODO also check if pack exists
			if (!packName.equals(currPack))
			{
				defPack = packName;
			}
		}
		else
		{
			defPack = packName;
		}
	}
	
	public static String getCurrentPack()
	{
		return currPack;
	}
	
	public static void setCurrentPack(String packName)
	{
		// TODO also check if pack exists
		if (packName != null && !packName.equals(currPack))
		{
			currPack = packName;
		}
	}
	
	public static boolean isBufferingResources()
	{
		return isBuffering;
	}
	
	public static void setBufferResources(boolean doBuffer)
	{
		isBuffering = doBuffer;
		// TODO Un-buffer resources, etc.
	}
}
