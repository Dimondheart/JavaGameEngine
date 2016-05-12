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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import xyz.digitalcookies.objective.Game;

/** Manages the resource packs.
 * @author Bryan Charles Bettis
 */
public class ResourceManager
{
	private static boolean isBuffering = false;
	private static String resPackDir = null;
	private static ArrayList<String> indexedPacks =
			new ArrayList<String>();
	private static ArrayList<String> activePacks = 
			new ArrayList<String>();
	private static boolean isRunningInJar;
	
	/** Constructor hidden to prevent instantiation. */
	private ResourceManager()
	{
	}
	
	/** Setup the resource pack manager. */
	public static void setup(HashMap<String, Object> config)
	{
		setResDir((String) config.get(Game.RES_PACK_DIR));
		setBufferResources((boolean) config.get(Game.INIT_BUFFER_RES));
		activePacks.add((String) config.get(Game.DEF_RES_PACK));
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
		System.out.println(indexedPacks);
	}
	
	public static InputStream getResource(String relPath)
	{
		if (isRunningInJar())
		{
			return new BufferedInputStream(getResourceZip(relPath));
		}
		else
		{
			return new BufferedInputStream(getResourceDir(relPath));
		}
	}
	
	private static InputStream getResourceZip(String relPath)
	{
		return null;
	}
	
	private static InputStream getResourceDir(String relPath)
	{
		File res = new File(
				ResourceManager.getResPackDir().replace(
						File.separator, "/"
						)
				+ "/"
				+ relPath
				);
		try
		{
			return new FileInputStream(res);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> indexLocation(String relLoc)
	{
		if (isRunningInJar())
		{
			return indexZipEntry(relLoc);
		}
		else
		{
			return indexDirectory(relLoc);
		}
	}
	
	private static List<String> indexDirectory(String relLoc)
	{
		LinkedList<String> found = new LinkedList<String>();
		File resPackDir = null;
		try
		{
			resPackDir = new File(getResPackDir());
		}
		catch (NullPointerException e)
		{
			System.out.println(
					"WARNING: Resource packs not indexed. "
					+ "Unable to locate and index resources."
							);
			return found;
		}
		String relPath = resPackDir.getPath() + "/" + relLoc + "/";
		relPath = relPath.replace("/", File.separator);
		File myResDir = new File(relPath);
		LinkedList<File> subLocToCheck = new LinkedList<File>();
		subLocToCheck.add(myResDir);
		while (true)
		{
			if (subLocToCheck.isEmpty())
			{
				break;
			}
			File nextDir = subLocToCheck.poll();
			for (File node : nextDir.listFiles())
			{
				if (node.isDirectory())
				{
					subLocToCheck.add(node);
				}
				else
				{
					String relRoot = nextDir.getPath().replace(myResDir.getPath(), "");
					if (relRoot.startsWith(File.separator))
					{
						relRoot = relRoot.substring(1, relRoot.length());
					}
					// Change to a cross-platform standard
					relRoot = relRoot.replace(File.separator, "/");
					if (relRoot.equals(""))
					{
						found.add(relRoot + node.getName());
					}
					else
					{
						found.add(relRoot + "/" + node.getName());
					}
				}
			}
		}
		return found;
	}
	
	private static List<String> indexZipEntry(String relLoc)
	{
		return new LinkedList<String>();
	}
	
	public static boolean isRunningInJar()
	{
		return isRunningInJar;
	}
	
	/** Sets the root directory for all resource packs in ResourcePackManager.
	 * @param relResDir the root directory for all resource packs, within the
	 * 		root location of the code source (e.g. in same root directory as
	 * 		your source code directory or in the top directory of a .jar file)
	 */
	private static void setResDir(String relResDir)
	{
		String rootResDir = ClassLoader
				.getSystemResource(relResDir)
				.getPath()
				.replace("%20", " ")
				.replace("/", File.separator);
		if (rootResDir.startsWith(File.separator))
		{
			rootResDir = rootResDir.substring(1, rootResDir.length());
		}
//		if ()
//		System.out.println(rootResDir);
//		URI codeSrc = EngineSetupData.getCodeSource();
		String scheme = null;
		try {
			scheme = ClassLoader
					.getSystemResource(relResDir).toURI().getScheme();
		}
		catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		// Fail...
		if (scheme == null)
		{
			System.out.println("ERROR: Unknown code source setup");
			System.exit(0);
		}
		// Within a jar
		else if (scheme.contains("rsrc") || rootResDir.toLowerCase().contains(".jar"))
		{
			isRunningInJar = true;
		}
		else
		{
			isRunningInJar = false;
		}
//		// Fail...
//		if (scheme == null)
//		{
//			System.out.println("ERROR: Unknown code source setup");
//			System.exit(0);
//		}
//		// A "runnable" JAR file setup
//		else if (scheme.contains("rsrc"))
//		{
//			String resDir = ClassLoader.getSystemResource("").getPath().replace("%20", " ");
//			// Create the URI referencing the resource folder
//			rootResDir = new File(
//					resDir
//					+ relResDir.replace(
//							File.separator, "/"
//							)
//					).getPath();
//		}
//		// A 'non-runnable' JAR or normal directory setup
//		else if (scheme.contains("file"))
//		{
//			// 'Non-runnable' JAR
//			if (codeSrc.getPath().toLowerCase().contains(".jar"))
//			{
//				String resDir = ClassLoader.getSystemResource("")
//						.getPath().replace("%20", " ");
//				// Create the URI referencing the resource folder
//				rootResDir = new File(
//						resDir
//						+ relResDir.replace(
//								File.separator, "/"
//								)
//						).getAbsoluteFile().getAbsolutePath();
//			}
//			// Normal directory
//			else
//			{
//				File ft = new File(codeSrc).getParentFile();
//				ft = new File(ft, relResDir);
//				rootResDir = ft.getAbsolutePath();
//			}
//		}
		// Index resource packs
		indexResourcePacks(rootResDir);
	}
}
