package core;

import java.util.concurrent.ConcurrentHashMap;

/** The collection of settings that can change during a game, such as
 * volume levels, resource usage settings, etc.
 * @author Bryan Bettis
 */
public class DynamicSettings
{
	/** The map of the different dynamic settings. */
	protected static volatile ConcurrentHashMap<String, Object> settings;
	
	/**
	 * @author Bryan Bettis
	 */
	public enum ThreadingSetting
	{
		FULL,  // Runs all systems in their own thread, regardless of CPU specs
		SINGLE,  // Use only one thread for all game engine systems
		OPTIMIZE  // Auto-optimize threading based on available cores
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
		// How the core engine systems should utilize threading
		settings.put("ENGINE_THREADING", ThreadingSetting.OPTIMIZE);
		// Master volume setting %
		settings.put("MASTER_VOLUME", 100);
		// Background music volume setting %
		settings.put("BGM_VOLUME", 100);
		// Sound effects volume setting %
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
