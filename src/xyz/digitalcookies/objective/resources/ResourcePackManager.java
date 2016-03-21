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
import java.util.LinkedList;

import xyz.digitalcookies.objective.DevConfig;
import xyz.digitalcookies.objective.EngineSetupData;

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
	
	public static void setup()
	{
		setResDir((String) DevConfig.getSetting(DevConfig.RES_PACK_DIR));
		ResourcePackManager.setBufferResources((boolean) DevConfig.getSetting(DevConfig.INIT_BUFFER_RES));
		ResourcePackManager.setDefaultPack((String) DevConfig.getSetting(DevConfig.DEF_RES_PACK));
		ResourcePackManager.setCurrentPack((String) DevConfig.getSetting(DevConfig.INIT_RES_PACK));
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
	
	/** Sets the root directory for all resource packs in ResourcePackManager.
	 * @param relResDir the root directory for all resource packs, within the
	 * 		root location of the code source (e.g. in same root directory as
	 * 		your source code directory or in the top directory of a .jar file)
	 */
	private static void setResDir(String relResDir)
	{
		String rootResDir = null;
		String scheme = EngineSetupData.getCodeSource().getScheme();
		// Fail...
		if (scheme == null)
		{
			System.out.println("ERROR: Unknown code source setup");
			System.exit(0);
		}
		// A "runnable" JAR file setup
		else if (scheme.contains("rsrc"))
		{
			String resDir = ClassLoader.getSystemResource("").getPath().replace("%20", " ");
			// Create the URI referencing the resource folder
			rootResDir = new File(
					resDir
					+ relResDir.replace(
							File.separator, "/"
							)
					).getPath();
		}
		// A 'non-runnable' JAR or normal directory setup
		else if (scheme.contains("file"))
		{
			// 'Non-runnable' JAR
			if (EngineSetupData.getCodeSource().getPath().toLowerCase().contains(".jar"))
			{
				String resDir = ClassLoader.getSystemResource("")
						.getPath().replace("%20", " ");
				// Create the URI referencing the resource folder
				rootResDir = new File(
						resDir
						+ relResDir.replace(
								File.separator, "/"
								)
						).getAbsoluteFile().getAbsolutePath();
			}
			// Normal directory
			else
			{
				File ft = new File(EngineSetupData.getCodeSource()).getParentFile();
				ft = new File(ft, relResDir);
				rootResDir = ft.getAbsolutePath();
			}
		}
		// Index resource packs
		indexResourcePacks(rootResDir);
	}
}
