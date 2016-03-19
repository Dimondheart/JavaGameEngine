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

package xyz.digitalcookies.objective.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/** Perform general setup operations for different engine systems.
 * @author Bryan Charles Bettis
 */
public class SetupOperations
{
	/** The source location of the project using this game engine. */
	private static URI sourceLoc = null;
	/** The name of the game engine properties file. */
	private static String propFileName = "objective.properties";
	
	/** TODO Document
	 * @param source the location of the source of the non-game engine
	 * 		files
	 */
	public static void setCodeSource(URL source)
	{
		URI codeSource = null;
		try
		{
			codeSource = source.toURI();
		}
		catch (java.net.URISyntaxException e)
		{
			System.out.println("ERROR GETTING CODE SOURCE LOCATION");
			System.exit(0);
		}
		sourceLoc = codeSource;
	}
	
	/** Set the name of the config/properties file for the Objective
	 * game engine. This value defaults to "objective.properties",
	 * which is the recommended name. You should only have to use this
	 * function if your project setup requires you to use a file name
	 * different from this default name.
	 * @param name relative path to the file (e.g. in same root
	 * 		directory as your source code directory or in the top directory
	 * 		of a .jar file)
	 */
	public static void setOJGEPropFileName(String name)
	{
		propFileName = name;
	}
	
	/** Sets the root directory for all resource packs in ResourcePackManager.
	 * @param relResDir the root directory for all resource packs, within the
	 * 		root location of the code source (e.g. in same root directory as
	 * 		your source code directory or in the top directory of a .jar file)
	 */
	public static void setResDir(String relResDir)
	{
		String rootResDir = null;
		String scheme = sourceLoc.getScheme();
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
			if (sourceLoc.getPath().toLowerCase().contains(".jar"))
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
				File ft = new File(sourceLoc).getParentFile();
				ft = new File(ft, relResDir);
				rootResDir = ft.getAbsolutePath();
			}
		}
		// Index resource packs
		xyz.digitalcookies.objective.resources
		.ResourcePackManager.indexResourcePacks(rootResDir);
	}
	
	/** Get the properties file for the game engine to use. Calls
	 * System.exit(0) if the properties failed to load (to prevent
	 * this issue from only showing up in the game engine classes.)
	 * @return the Properties object for the game engine to use
	 */
	public static Properties getOJGEProperties()
	{
		Properties props = new Properties();
		String scheme = sourceLoc.getScheme();
		try
		{
			// Fail...
			if (scheme == null)
			{
				System.out.println("ERROR: Unknown code source setup");
				System.exit(0);
			}
			// A "runnable" JAR file setup
			else if (scheme.contains("rsrc"))
			{
				File propFile = new File(ClassLoader.getSystemResource("").toURI());
				propFile = new File(propFile, propFileName);
				props.load(new FileInputStream(propFile));
			}
			// A 'non-runnable' JAR or normal directory setup
			else if (scheme.contains("file"))
			{
				// 'Non-runnable' JAR
				if (sourceLoc.getPath().toLowerCase().contains(".jar"))
				{
					File propFile = new File(ClassLoader.getSystemResource("").toURI());
					propFile = new File(propFile, propFileName);
					props.load(new FileInputStream(propFile));
				}
				// Normal directory
				else
				{
					File ft = new File(sourceLoc).getParentFile();
					ft = new File(ft, propFileName);
					props.load(new java.io.FileInputStream(ft));
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("ERROR loading game engine properties file");
			System.exit(0);
		}
		catch (URISyntaxException e)
		{
			System.out.println("ERROR loading game engine properties file");
			e.printStackTrace();
			System.exit(0);
		}
		return props;
	}
}
