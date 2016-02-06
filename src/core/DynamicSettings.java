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

import java.util.concurrent.ConcurrentHashMap;

/** The collection of settings that can change during a game, such as
 * volume levels, resource usage settings, etc.
 * @author Bryan Charles Bettis
 */
public class DynamicSettings
{
	/** The map of the different dynamic settings. */
	protected static volatile ConcurrentHashMap<String, Object> settings;
	
	/**
	 * @author Bryan Charles Bettis
	 */
	public enum ThreadingSetting
	{
		/** Runs all thread-able subsystems in their own threads, regardless
		 * of the number of available virtual cores.
		 */
		FULL,
		/** Run all game engine systems in the same thread. */
		SINGLE,
		/** Optimize threading based on the number of virtual cores
		 * available.
		 */
		OPTIMIZE
	}

	/* Setup static stuff. */
	static
	{
		settings = new ConcurrentHashMap<String, Object>();
		setupSettings();
	}

	/** Setup the initial dynamic settings. */
	private static void setupSettings()
	{
		// TODO load from a file, if fails then recreate settings
		// How the core engine systems should utilize threading (ThreadingSetting)
		settings.put("ENGINE_THREADING", ThreadingSetting.OPTIMIZE);
		// Invert the scroll wheel on the mouse (boolean)
		settings.put("INVERT_SCROLL_WHEEL", false);
		// Master volume setting % (int)
		settings.put("MASTER_VOLUME", 100);
		// Background music volume setting % (int)
		settings.put("BGM_VOLUME", 100);
		// Sound effects volume setting % (int)
		settings.put("SFX_VOLUME", 100);
	}

	/** Gets the specified dynamic setting, or returns null if the
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
					"WARNING: Dynamic setting \'"
							+ setting
							+ "\' is not a valid setting. "
							+ "Returning null instead and printing stack trace "
							+ "for debugging. "
					);
			// Print the current stack for debugging
			Thread.dumpStack();
		}
		return obj;
	}

	/** Changes or adds a dynamic setting.
	 * @param name the name of the setting to change/add
	 * @param setting the new value of the setting
	 */
	public static void setSetting(String name, Object setting)
	{
		settings.put(name, setting);
	}
}
