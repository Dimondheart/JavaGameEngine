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

package xyz.digitalcookies.objective;

import java.net.URI;
import java.net.URL;

/** Perform general setup operations for different engine systems.
 * @author Bryan Charles Bettis
 */
public class EngineSetupData
{
	/** The source location of the project using this game engine. */
	private static URI sourceLoc = null;
	/** The name of the game engine properties file. */
	private static String propFileName = "objective.properties";
	
	/** Hidden to prevent instantiation. */
	private EngineSetupData()
	{
	}
	
	/** Gets the URI to the root of the source code of the program using
	 * this game engine.
	 * @return the URI equivalent of the URL specified by the developer
	 * 		when setting up the game session
	 */
	public static URI getCodeSource()
	{
		return sourceLoc;
	}
	
	/** TODO Document
	 * @param source a URL to the source of the program using this game
	 * 		engine; used to get access to the engine configuration file and
	 * 		resources directory
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
	
	/** Get the name of the config/properties file for the Objective
	 * game engine. This value defaults to "objective.properties",
	 * which is the recommended name.
	 * @return the name of the properties file to load engine
	 * 		config/properties data from
	 */
	public static String getOJGEPropFileName()
	{
		return propFileName;
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
}
