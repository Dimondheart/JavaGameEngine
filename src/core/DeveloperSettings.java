package core;

import java.awt.Dimension;
import java.util.concurrent.ConcurrentHashMap;

/** Contains different settings that a developer can safely modify without
 * damaging the game engine's systems.
 * @author Bryan Bettis
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
