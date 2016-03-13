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

package core;

import java.awt.Font;
import java.util.concurrent.ConcurrentHashMap;

/** Contains different settings that a developer can safely modify without
 * hindering or breaking the game engine's systems.  However, these settings
 * cannot safely be changed once the program has been started up. The core engine
 * settings have string constants provided for them as public constants.
 * @author Bryan Charles Bettis
 */
public class DeveloperSettings
{
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
	/** The GameState subclass to use when first starting up the game engine.
	 * <br>
	 * <br> <i>Type:</i> a class (for example, game.gamestates.MainMenu.class)
	 */
	public static final String INIT_GAME_STATE = "INIT_GAME_STATE";
	/** The default font to use for text drawing when a font is not specified.
	 * <br>
	 * <br> <i>Type:</i> a Font instance
	 */
	public static final String DEF_FONT = "DEF_FONT";
	
	/** The map of the different developer settings. */
	protected static volatile ConcurrentHashMap<String, Object> settings;
	
	/* Setup static stuff. */
	static
	{
		settings = new ConcurrentHashMap<String, Object>();
		setupSettings();
	}
	
	/** Setup the initial developer settings. Note that once a developer
	 * setting has been added, it cannot (and should not) be changed
	 * during runtime. If developers want to add a setting that can
	 * be modified during runtime, they should add it to the
	 * DynamicSettings class.
	 */
	private static void setupSettings()
	{
		// The number of layers in the main layer set
		settings.put("NUM_MAIN_LAYERS", 10);
		// The initial width of the main window
		settings.put("INIT_MAIN_WIN_WIDTH", 480);
		// The initial height of the main window
		settings.put("INIT_MAIN_WIN_HEIGHT", 270);
		// The game state to use when the game first starts
		settings.put("INIT_GAME_STATE", game.gamestate.MainMenu.class);
		settings.put("DEF_FONT", new Font("Dialog", Font.PLAIN, 12));
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
		Object obj = settings.getOrDefault(setting, null);
		// Print debug info if setting not found
		if (obj == null)
		{
			System.out.println(
					"WARNING: Developer setting \'"
					+ setting
					+ "\' is not a valid developer setting. "
					+ "Returning null instead and printing stack trace "
					+ "for debugging. "
					);
			// Print the current stack for debugging
			Thread.dumpStack();
		}
		return obj;
	}
}
