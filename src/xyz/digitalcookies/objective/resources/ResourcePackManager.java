package xyz.digitalcookies.objective.resources;

import java.io.File;
import java.net.URI;

public class ResourcePackManager
{
	private static String defPack = null;
	private static String currPack = null;
	private static boolean isBuffering = false;
	
	/** Constructor hidden to prevent instantiation. */
	private ResourcePackManager()
	{
	}
	
	public static void indexResourcePacks(URI resDir)
	{
		File dir = new File(resDir);
		if (dir.isDirectory())
		{
			for (File file : dir.listFiles())
			{
				// TODO index
			}
		}
		else
		{
			System.out.println(
					"ERROR: Specified resource location is not a directory."
					);
			Thread.dumpStack();
		}
	}
	
	public static String getDefaultPack()
	{
		return defPack;
	}
	
	public static void setDefaultPack(String packName)
	{
		defPack = packName;
	}
	
	public static String getCurrentPack()
	{
		return currPack;
	}
	
	public static void setCurrentPack(String packName)
	{
		if (packName != null && packName.equals(currPack))
		{
			currPack = packName;
			// TODO index pack here
		}
		else
		{
			currPack = packName;
		}
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
}
