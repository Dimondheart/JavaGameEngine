package xyz.digitalcookies.objective.resources;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ResourceHandler
{
	private String rootResDir;
	protected ConcurrentHashMap<String, Object> resources;
	
	public ResourceHandler()
	{
		rootResDir = null;
		resources = new ConcurrentHashMap<String, Object>();
	}
	
	/** Buffer a single resource into memory. */
	public abstract void bufferResource(Object toBuffer);
	
	public void initialize()
	{
		System.out.println("Initializing resource handler...");
		if (rootResDir != null)
		{
			indexResources();
			if (ResourcePackManager.isBufferingResources())
			{
				bufferResources();
			}
		}
	}
	
	public String getRootResDir()
	{
		return rootResDir;
	}
	
	void setRootDir(String rootResDir)
	{
		// Do nothing if already configured for the specified directory
		if (rootResDir != null && !rootResDir.equals(this.rootResDir))
		{
			this.rootResDir = rootResDir;
			initialize();
		}
	}
	
	private void indexResources()
	{
		System.out.println("Indexing resources...");
		resources.clear();
		// First index resources of the default package
		if (ResourcePackManager.getDefaultPack() != null)
		{
			indexPack(ResourcePackManager.getDefaultPack());
		}
		// Then index any additional resources in current pack
		if (ResourcePackManager.getCurrentPack() != null)
		{
			indexPack(ResourcePackManager.getCurrentPack());
		}
	}
	
	private void indexPack(String pack)
	{
		System.out.println("Indexing \'" + pack + "\'");
		// TODO implement
	}
	
	private void bufferResources()
	{
		System.out.println("Buffering resources...");
		Enumeration<String> keys = resources.keys();
		while (keys.hasMoreElements())
		{
			// TODO implement
		}
	}
}
