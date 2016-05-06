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
import java.util.ArrayList;
import xyz.digitalcookies.objective.DevConfig;
import xyz.digitalcookies.objective.EngineSetupData;

/** Manages the resource packs.
 * @author Bryan Charles Bettis
 */
public class ResourcePackManager
{
	private static boolean isBuffering = false;
	private static String resPackDir = null;
	private static ArrayList<String> indexedPacks =
			new ArrayList<String>();
	private static ArrayList<String> activePacks = 
			new ArrayList<String>();
	
	/** Constructor hidden to prevent instantiation. */
	private ResourcePackManager()
	{
	}
	
	/** Setup the resource pack manager. */
	public static void setup()
	{
		setResDir(DevConfig.getString(DevConfig.RES_PACK_DIR));
		setBufferResources(DevConfig.getBoolean(DevConfig.INIT_BUFFER_RES));
		activePacks.add(DevConfig.getString(DevConfig.DEF_RES_PACK));
	}
	
	public static String getResPackDir()
	{
		return resPackDir;
	}
	
	/** Get all resource pack names that resources are being
	 * loaded from. Resources are loaded from the packs in this list in reverse
	 * order (the last pack is loaded first, then the second to last pack, and
	 * so on.) Resources loaded from one pack will not be overridden if the
	 * same resource is found in a later pack.
	 * @return an array of the resource packs that are currently being used:
	 * 		modifying this array will not change the currently active packs
	 */
	public static String[] getActivePacks()
	{
		return activePacks.toArray(new String[activePacks.size()]);
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
	
	/** Determine and record the names of all available resource packs in
	 * the specified root directory.
	 * @param resDir the root directory containing the resource packs
	 */
	public static void indexResourcePacks(String resDir)
	{
		File dir = new File(resDir);
		if (dir.isDirectory())
		{
			indexedPacks.clear();
			resPackDir = resDir;
			for (File file : dir.listFiles())
			{
				System.out.println(file.getName());
				indexedPacks.add(file.getName());
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
