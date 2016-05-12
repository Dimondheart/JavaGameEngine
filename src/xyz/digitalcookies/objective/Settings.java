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

import java.util.concurrent.ConcurrentHashMap;

/** The collection of settings that can change during a game, such as
 * volume levels, resource usage settings, etc.  The core engine settings
 * have string constants provided for them as public constants.
 * TODO consider merging this into the Game class
 * @author Bryan Charles Bettis
 */
public class Settings
{
	/** If the scroll wheel readings on the mouse should be inverted or not.
	 * <br>
	 * <br> <i>Type:</i> boolean, true (scroll up is positive) or false
	 * (scroll down is positive)
	 */
	public static final String INVERT_SCROLL_WHEEL = "INVERT_SCROLL_WHEEL";
	/** The overall volume level for the sound system.
	 * <br>
	 * <br> <i>Type:</i> integer in the range 0-100 (0 is muted)
	 */
	public static final String MASTER_VOLUME = "MASTER_VOLUME";
	/** The volume change applied to the background music only (effect is
	 * combined with the MASTER_VOLUME setting affecting the overall volume).
	 * <br>
	 * <br> <i>Type:</i> integer in the range 0-100 (0 is muted)
	 */
	public static final String BGM_VOLUME = "BGM_VOLUME";
	/** The volume change applied to the sound effects only (effect is
	 * combined with the MASTER_VOLUME setting affecting the overall volume).
	 * <br>
	 * <br> <i>Type:</i> integer in the range 0-100 (0 is muted)
	 */
	public static final String SFX_VOLUME = "SFX_VOLUME";
	/** The speed to try to run the graphics system at, in
	 * frames per second (FPS).
	 * <br>
	 * <br> <i>Type:</i> integer (for example, 60 would be 60 FPS)
	 * <br>
	 * <br> <b>WARNING:</b> this setting has not yet been incorporated into
	 * the graphics system.
	 */
	public static final String TARGET_FPS = "TARGET_FPS";
	
	/** The map of the different dynamic settings. */
	private static volatile ConcurrentHashMap<String, Object> settings;

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
		// Invert the scroll wheel on the mouse (boolean)
		settings.put(INVERT_SCROLL_WHEEL, false);
		// Master volume setting % (int)
		settings.put(MASTER_VOLUME, 100);
		// Background music volume setting % (int)
		settings.put(BGM_VOLUME, 100);
		// Sound effects volume setting % (int)
		settings.put(SFX_VOLUME, 100);
		// The FPS to try to run the graphics system at (CURRENTLY UNUSED SETTING)
		settings.put(TARGET_FPS, 60);
		
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
