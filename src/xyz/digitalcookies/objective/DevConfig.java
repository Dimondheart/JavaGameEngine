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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/** A collection of initial settings used by the game engine and available
 * for the developers to use in their code. Most of these settings are
 * set up through the engine configuration/properties file.
 * @author Bryan Charles Bettis
 */
public class DevConfig
{
	/** The title of the main game window. */
	public static final String MAIN_WIN_TITLE = "MAIN_WIN_TITLE";
	/** How many layers to setup in the main layer set.
	 * <br>
	 * <br> <i>Type:</i> integer greater than 0
	 */
	public static final String NUM_MAIN_LAYERS = "NUM_MAIN_LAYERS";
	/** The initial width of the game window.
	 * <br>
	 * <br> <i>Type:</i> integer greater than 0
	 */
	public static final String INIT_MAIN_WIN_WIDTH = "INIT_MAIN_WIN_WIDTH";
	/** The initial height of the game window.
	 * <br>
	 * <br> <i>Type:</i> greater than 0
	 */
	public static final String INIT_MAIN_WIN_HEIGHT = "INIT_MAIN_WIN_HEIGHT";
	/** The default font to use for text drawing when a font is not specified.
	 * <br>
	 * <br> <i>Type:</i> a String corresponding to a supported font
	 */
	public static final String DEF_FONT = "DEF_FONT";
	/** The size of the default font. */
	public static final String DEF_FONT_SIZE = "DEF_FONT_SIZE";
	/** The root directory for sound resources. */
	public static final String SOUND_RES_DIR = "SOUND_RES_DIR";
	/** The root directory for graphics resources. */
	public static final String GRAPHICS_RES_DIR = "GRAPHICS_RES_DIR";
	/** If buffer-able resources should be buffered on startup. */
	public static final String INIT_BUFFER_RES = "INIT_BUFFER_RES";
	/** The default resource pack. */
	public static final String DEF_RES_PACK = "DEF_RES_PACK";
	/** The relative root directory of the resource pack directory. */
	public static final String RES_PACK_DIR = "RES_PACK_DIR";
	
	/** The map of the different developer settings. */
	private static ConcurrentHashMap<String, Object> settings =
			new ConcurrentHashMap<String, Object>();
	
	/** Hidden to prevent instantiation. */
	private DevConfig()
	{
	}
	
	/** Setup the initial developer settings. Note that once a developer
	 * setting has been added, it cannot be changed
	 * during runtime. If developers want to add a setting that can
	 * be modified during runtime, they should use the
	 * Settings class.
	 */
	static void setup()
	{
		Properties props = getOJGEProperties();
		// The name of the main game window
				settings.put(
						MAIN_WIN_TITLE,
						(String) props.getOrDefault(MAIN_WIN_TITLE, "Unnamed Game")
						);
		// The number of layers in the main layer set
		settings.put(
				NUM_MAIN_LAYERS,
				Integer.parseInt(
						(String) props.getOrDefault(NUM_MAIN_LAYERS, "10")
						)
				);
		// The initial width of the main window
		settings.put(
				INIT_MAIN_WIN_WIDTH,
				Integer.parseInt(
						(String) props.getOrDefault(INIT_MAIN_WIN_WIDTH, "480")
						)
				);
		// The initial height of the main window
		settings.put(
				INIT_MAIN_WIN_HEIGHT,
				Integer.parseInt(
						(String) props.getOrDefault(INIT_MAIN_WIN_HEIGHT, "270")
						)
				);
		settings.put(
				DEF_FONT,
				(String) props.getOrDefault(DEF_FONT, "Dialog")
				);
		settings.put(
				DEF_FONT_SIZE,
				Integer.parseInt(
						(String) props.getOrDefault(DEF_FONT_SIZE, "12")
						)
				);
		settings.put(
				SOUND_RES_DIR,
				(String) props.getOrDefault(
						SOUND_RES_DIR,
						"sounds"
						)
				);
		settings.put(
				GRAPHICS_RES_DIR,
				(String) props.getOrDefault(
						GRAPHICS_RES_DIR,
						"graphics"
						)
				);
		settings.put(
				INIT_BUFFER_RES,
				Boolean.parseBoolean(
						(String) props.getOrDefault(INIT_BUFFER_RES, "true")
						)
				);
		settings.put(
				DEF_RES_PACK,
				(String) props.getOrDefault(DEF_RES_PACK, "Default")
				);
		settings.put(
				RES_PACK_DIR,
				(String) props.getOrDefault(RES_PACK_DIR, "resources")
				);
	}
	
	/** Gets the specified developer setting, or returns null if the
	 * specified setting was not found.
	 * @param setting the name of the setting to get
	 * @return an Object representing the specified setting (primitive types
	 * 		are wrapped in their object equivalent), or null if the specified 
	 * 		setting was not found
	 */
	public static Object getSetting(String setting)
	{
		// No setting name specified
		if (setting == null)
		{
			System.out.println(
					"Error: No setting name specified when attempting to get"
					+ "a developer setting."
					);
			return null;
		}
		Object obj = settings.getOrDefault(setting, null);
		// Print debug info if setting not found
		if (obj == null)
		{
			System.out.println(
					"WARNING: Developer setting \'"
					+ setting.toString()
					+ "\' is not a valid developer setting. "
					+ "Returning null instead and printing stack trace "
					+ "for debugging. "
					);
			// Print the current stack for debugging
			Thread.dumpStack();
		}
		return obj;
	}
	
	/** Get the specified setting and cast it to a String.
	 * Failed type casting exceptions are not handled.
	 * @param setting the setting to get
	 * @return the value of the requested setting, or null if not found
	 */
	public static String getString(String setting)
	{
		String value = null;
		value = (String) getSetting(setting);
		return value;
	}
	
	/** Get the specified setting and cast it to a boolean.
	 * Failed type casting exceptions are not handled.
	 * @param setting the setting to get
	 * @return the value of the requested setting, or false if not found
	 */
	public static boolean getBoolean(String setting)
	{
		boolean value = false;
		value = (boolean) getSetting(setting);
		return value;
	}
	
	/** Get the specified setting and cast it to an integer.
	 * Failed type casting exceptions are not handled.
	 * @param setting the setting to get
	 * @return the value of the requested setting, or 0 if not found
	 */
	public static int getInt(String setting)
	{
		int value = 0;
		value = (int) getSetting(setting);
		return value;
	}
	
	/** Get the properties file for the game engine to use. Calls
	 * System.exit(0) if the properties failed to load (to prevent
	 * this issue from only showing up in the game engine classes.)
	 * @return the Properties object for the game engine to use
	 */
	private static Properties getOJGEProperties()
	{
		Properties props = new Properties();
		// Get source location and properties file name
		URI sourceLoc = EngineSetupData.getCodeSource();
		String propFileName = EngineSetupData.getOJGEPropFileName();
		// Determine project setup/scheme
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
