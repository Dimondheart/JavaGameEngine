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

import java.awt.Dimension;
import java.util.concurrent.ConcurrentHashMap;

import game.gamestate.MainMenuTest;

/** Contains different settings that a developer can safely modify without
 * damaging the game engine's systems.
 * @author Bryan Charles Bettis
 */
public class DeveloperSettings
{
	/** The map of the different developer settings. */
	protected static volatile ConcurrentHashMap<String, Object> settings;
	
	/* Setup static stuff. */
	static
	{
		settings = new ConcurrentHashMap<String, Object>();
		setupSettings();
	}
	
	/** Setup the initial developer settings. */
	private static void setupSettings()
	{
		// The number of layers in the main layer set
		settings.put("NUM_MAIN_LAYERS", 10);
		// The initial dimensions of the main window
		settings.put("INIT_MAIN_WIN_DIMS", new Dimension(480,270));
		settings.put("INIT_GAME_STATE", MainMenuTest.class);
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
	
	/** Changes or adds a developer setting.
	 * <br>
	 * <b>WARNING:</b> changing developer settings from another class
	 * IS allowed, however it is very risky as most of these settings
	 * are used to initialize core game engine components and could cause
	 * errors/exceptions during runtime.
	 * @param name the name of the setting to change/add
	 * @param setting the new value of the setting
	 */
	public static void setSetting(String name, Object setting)
	{
		System.out.println(
				"WARNING: Developer setting \'"
						+ name
						+ "\' is being added/changed after initial setup. "
						+ "This could cause unpredictable behavior."
						);
		settings.put(name, setting);
	}
}
