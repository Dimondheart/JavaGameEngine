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

package xyz.digitalcookies.objective.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/** Base class for all resource handlers. A resource handler loads,
 * buffers, and other management-related operations for a collection of
 * resources.
 * @author Bryan Charles Bettis
 * @param <T> The type of data managed by a resource handler
 */
public abstract class ResourceHandler<T>
{
	/** File extensions supported. */
	private String[] supportedExt;
	/** Relative path containing resources managed. */
	private String rootResDir;
	/** Map of resource indexes to buffered resources. */
	private HashMap<String, T> resources;
	/** If this resource handler's bufferResources(...) method has been called
	 * and finished normally (meaning it tried to buffer all indexed
	 * resources.)
	 */
	private boolean isBuffered;
	/** If this resource handler supports buffering of its resources. */
	private boolean supportsBuffering;
	
	/** Basic constructor. */
	protected ResourceHandler()
	{
		rootResDir = null;
		resources = new HashMap<String, T>();
		supportedExt = null;
		isBuffered = false;
		supportsBuffering = false;
	}
	
	/** Load data from the specified file and return the data that
	 * should be buffered as a resource.
	 * @param toLoad the file to buffer
	 * @return the resource data that will be buffered
	 **/
	protected abstract T loadResource(File toLoad);
	/** Get the default value when a resource could not be loaded or
	 * a requested resource is not currently indexed.
	 * @return a generic default object/value
	 */
	protected abstract T getDefaultValue();
	
	/** Check if the specified named resource exists in this resource
	 * manager.
	 * @param resource the name of the resource relative to the root
	 * directory of this resource manager.
	 * @return true if the resource exists, false if it does not exist or there
	 * 		was a problem checking (for example if this resource manager
	 * 		has not been setup yet)
	 */
	public boolean resExists(String resource)
	{
		return resources != null && resources.containsKey(resource);
	}
	
	/** Get the specified resource.
	 * @param resource the relative name/path to the resource (meaning the
	 * 		path to the resource within this resource handler's root directory)
	 * @return the specified resource; if the specified resource does not
	 * 		exist, a default value specific to this resource handler will be
	 * 		returned (could be null or a special placeholder resource)
	 */
	public T getRes(String resource)
	{
		if (resources != null)
		{
			// Resource handler does not support buffering of its resources
			if (!supportsBuffering())
			{
				// Load the resource
				T res = loadResFromPacks(
						ResourcePackManager.getCurrentPack(),
						ResourcePackManager.getDefaultPack(),
						resource
						);
				// Resource not found, use default value
				if (res == null)
				{
					res = getDefaultValue();
				}
				return res;
			}
			// Supports buffering, and all resources are already buffered
			else if (isBuffered())
			{
				return resources.getOrDefault(resource, getDefaultValue());
			}
			// Supports buffering, but the specified resource not yet buffered
			else if (resources.getOrDefault(resource, null) == null)
			{
				// Load the resource
				T res = loadResFromPacks(
						ResourcePackManager.getCurrentPack(),
						ResourcePackManager.getDefaultPack(),
						resource
						);
				// Resource not found, use default value
				if (res == null)
				{
					res = getDefaultValue();
				}
				// Buffer the resource
				setResValue(resource, res);
				return resources.getOrDefault(resource, getDefaultValue());
			}
			// Unknown situation
			else
			{
				System.out.println(
						"INFO: Internal resource handeler issue in locating "
						+ "the resource \'"
						+ resource
						+ "\'. returning null. "
						
						);
				return null;
			}
		}
		System.out.println(
				"WARNING: Attempted to get a resource from an uninitialized "
				+ "resource handler."
				);
		Thread.dumpStack();
		return null;
	}
	
	/** Initialize this resource handler. This will index the contents of the
	 * default and current resource packs in ResourcePackManager, and then
	 * if ResourcePackManager has been told to pre-load all resources, buffer
	 * all indexed resources (if this resource handler supports buffering.)
	 * This method can also be reused to refreash this resource handler, such
	 * as when default or current ResourcePackManager packs have changed, or
	 * when the contents of the resource packs have been changed.
	 */
	public void initialize()
	{
		System.out.println("(Re)initializing resource handler...");
		if (rootResDir != null)
		{
			isBuffered = false;
			// Index the resources
			indexResources(
					ResourcePackManager.getCurrentPack(),
					ResourcePackManager.getDefaultPack()
					);
			// Buffer resources if specified
			if (ResourcePackManager.isBufferingResources())
			{
				bufferResources(
						ResourcePackManager.getCurrentPack(),
						ResourcePackManager.getDefaultPack()
						);
			}
		}
	}
	
	/** Initialize this resource handler with the specified properties.
	 * This is the best way to initialize a resource handler for the first
	 * time, as setSupportedExtensions(...) and setRootDir(...) will call
	 * the no-argument initialize() function if the value they set changes.
	 * @param rootResDir the root directory of the resources to be managed
	 * 		by this resource handler, within any resource pack
	 * @param supportedExt all supported extensions; files without one of these
	 * 		extensions will be ignored (see
	 * 		{@link #setSupportedExtensions(String...)} for special cases)
	 */
	public void initialize(String rootResDir, String... supportedExt)
	{
		this.rootResDir = rootResDir;
		this.supportedExt = supportedExt;
		initialize();
	}
	
	/** Get the root directory of this resource handler, within a resource
	 * pack.
	 * @return the relative directory containing all of this handler's
	 * 		resources
	 */
	public String getRootResDir()
	{
		return rootResDir;
	}
	
	/** Get the extensions supported by this resource handler.
	 * @return a list of supported extensions, with some special return values:
	 * 		null means all file extensions are supported, empty means only
	 * 		files without extensions are supported (such as somefolder/FILE_X)
	 */
	public String[] getSupportedExtensions()
	{
		return supportedExt;
	}
	
	/** Set the relative root directory of the resources managed
	 * by this resource handler.
	 * @param rootResDir the root directory within a resource pack
	 */
	public void setRootDir(String rootResDir)
	{
		// Ignore when no specified path
		if (rootResDir != null)
		{
			// Do nothing if already configured for the specified directory
			if (this.rootResDir == null || !rootResDir.equals(this.rootResDir))
			{
				this.rootResDir = rootResDir;
				initialize();
			}
		}
	}
	
	/** Set what file extensions are supported by this resource handler. If
	 * the specified extensions are not the same 
	 * @param supported all supported file extensions, preceded by a dot "."
	 * 		(for example ".png") and in all lower case. Pass in null if all
	 * 		extensions should be supported
	 */
	public void setSupportedExtensions(String... supported)
	{
		// All extensions are supported; don't re-init if already set to this
		if (supported == null && supportedExt != null)
		{
			supportedExt = supported;
			initialize();
		}
		// Only specified ext. supported; don't re-init if already set to this
		else if (supported != null && !supported.equals(this.supportedExt))
		{
			supportedExt = supported;
			initialize();
		}
	}
	
	/** Check if this resource handler buffered all of its resources when
	 * it was initialized. This will always return false if this resource
	 * handler does not support buffering of its resources. If a resource
	 * handler supports buffering but not all resources were loaded during
	 * initialization, when a resource is loaded for the first time it will
	 * be buffered.
	 * @return true if all resources were buffered when this handler was
	 * 		initialized, false otherwise
	 */
	public boolean isBuffered()
	{
		return isBuffered;
	}
	
	/** Check if this resource handler supports buffering of its resources.
	 * If this is false, resources will be loaded each time they are requested.
	 * @return true if this resource handler can buffer its resources
	 */
	public boolean supportsBuffering()
	{
		return supportsBuffering;
	}
	
	/** Set if this resource handler supports buffering of its elements.
	 * If set to false, the loadResource(...) method will be called each
	 * time a resource from this resource hander is requested.
	 * @param isSupported true if buffering is supported, false if buffering
	 * 		is not supported
	 */
	protected void setSupportsBuffering(boolean isSupported)
	{
		supportsBuffering = isSupported;
	}
	
	protected boolean isExtensionSupported(String file)
	{
		// All extensions are supported
		if (getSupportedExtensions() == null)
		{
			return true;
		}
		// Only files w/o extensions supported
		if (getSupportedExtensions().length <= 0)
		{
			return !file.contains(".");
		}
		// Check for each supported extension
		for (String ext : getSupportedExtensions())
		{
			if (file.toLowerCase().contains(ext))
			{
				return true;
			}
		}
		return false;
	}
	
	/** Set the buffered value of a resource.
	 * @param resource the access name of the resource
	 * @param value the value to store
	 */
	private void setResValue(String resource, T value)
	{
		resources.put(resource, value);
	}
	
	/** Index all resources contained in one or more of the specified packs.
	 * This method maps out the resources in the primary pack, then looks in
	 * the secondary pack to fill in resources that were missing in the
	 * primary pack. This method does not buffer resources; it determines
	 * what resources are available in the specified packs, which is used later
	 * for loading and (if enabled) buffering.
	 * @param primaryPack the pack to first index resources from
	 * @param secondaryPack indexed after the primary pack; fills in missing
	 * 		resources not found in the primary pack
	 */
	private void indexResources(String primaryPack, String secondaryPack)
	{
		System.out.println("Indexing resources...");
		// Clear current resource index
		resources.clear();
		// First index resources of the default package
		if (primaryPack != null)
		{
			indexPack(primaryPack);
		}
		// Then index any additional resources in current pack
		if (secondaryPack != null && !secondaryPack.equals(primaryPack))
		{
			indexPack(secondaryPack);
		}
	}
	
	/** Index the resources in the specified pack.
	 * @param pack the name of the pack to index
	 */
	private void indexPack(String pack)
	{
		if (!pack.toLowerCase().contains(".zip"))
		{
			System.out.println("Indexing res pack directory \'" + pack + "\'");
			indexDirectory(pack);
		}
		else
		{
			System.out.println("Indexing res pack .zip \'" + pack + "\'");
			System.out.println(
					"WARNING: Resource packs as zip files are "
					+ "not yet supported."
					);
			// TODO index .zip contents here
		}
	}
	
	/** Index the contents of a pack formatted as a directory. Mainly provided
	 * as a convenience method for heavy development stages.
	 * @param packDir the name of the pack directory to index
	 */
	private void indexDirectory(String packDir)
	{
		File resPackDir = new File(ResourcePackManager.getResPackDir());
		System.out.println("Res Pack Dir: " + resPackDir.getPath());
		File myResDir = new File(resPackDir.getPath() + File.separator + packDir + File.separator + getRootResDir());
		System.out.println("Res Pack Path: " + myResDir.getPath());
		LinkedList<File> dirToCheck = new LinkedList<File>();
		dirToCheck.add(myResDir);
		while (true)
		{
			if (dirToCheck.isEmpty())
			{
				break;
			}
			File nextDir = dirToCheck.poll();
			for (File node : nextDir.listFiles())
			{
				if (node.isDirectory())
				{
					System.out.println("Found Subdir: " + node.getName());
					dirToCheck.add(node);
				}
				else
				{
					if (!isExtensionSupported(node.getPath()))
					{
						continue;
					}
					String relRoot = nextDir.getPath().replace(myResDir.getPath(), "");
					if (relRoot.startsWith(File.separator))
					{
						relRoot = relRoot.substring(1, relRoot.length());
					}
					// Change to a cross-platform standard
					relRoot = relRoot.replace(File.separator, "/");
					if (relRoot.equals(""))
					{
						System.out.println("Found File: " + relRoot + node.getName());
						setResValue(relRoot + node.getName(), null);
					}
					else
					{
						System.out.println("Found File: " + relRoot + "/" + node.getName());
						setResValue(relRoot + "/" + node.getName(), null);
					}
				}
			}
		}
		// TODO implement directory indexing
	}
	
	/** Buffer resources into the resources map in this resource handler.
	 * Returns quickly if this resource handler does not support buffering.
	 * @param primaryPack first buffer resources from this pack
	 * @param secondaryPack if an index resource is not found in the primary
	 * 		pack, try to load the resource from this pack
	 */
	private void bufferResources(String primaryPack, String secondaryPack)
	{
		if (!supportsBuffering())
		{
			System.out.println(
					"Buffering not supported for this resource handler."
					);
			return;
		}
		System.out.println("Buffering resources...");
		Iterator<String> keys = resources.keySet().iterator();
		while (keys.hasNext())
		{
			String key = keys.next();
			// Try to load the resource from the two packs
			System.out.println("Buffering resource \'" + key + "\'...");
			T resObj = loadResFromPacks(primaryPack, secondaryPack, key);
			// Resource not found in either pack, use default value
			if (resObj == null)
			{
				resObj = getDefaultValue();
			}
			// Set the resource value
			setResValue(key, resObj);
		}
		isBuffered = true;
	}
	
	/** Try to load the resource from the primary pack, if it fails try
	 * the secondary pack, if that fails return null.
	 * @param primaryPack the pack to search first
	 * @param secondaryPack the pack to search second
	 * @param resource the resource to search for
	 * @return the specified resource data, or null if loading failed
	 */
	private T loadResFromPacks(String primaryPack, String secondaryPack, String resource)
	{
		T resObj = null;
		// Resource not found in primary, check secondary
		if (!primaryPack.contains(".zip"))
		{
			try
			{
				
				resObj = loadResource(
						new File(
								ResourcePackManager.getResPackDir().toURL().getPath().replace(
										File.separator, "/"
										)
								+ "/"
								+ primaryPack
								+ "/"
								+ getRootResDir()
								+ resource
								)
						);
			}
			catch (MalformedURLException e)
			{
				resObj = null;
			}
			if (resObj != null)
			{
				return resObj;
			}
		}
		// Resource not found in primary, check secondary
		if (!secondaryPack.contains(".zip"))
		{
			try
			{
				resObj = loadResource(
						new File(
								ResourcePackManager.getResPackDir().toURL().getPath().replace(
										File.separator, "/"
										)
								+ "/"
								+ secondaryPack
								+ "/"
								+ getRootResDir()
								+ resource
								)
						);
			}
			catch (MalformedURLException e)
			{
				resObj = null;
			}
			if (resObj != null)
			{
				return resObj;
			}
		}
		return null;
	}
}
