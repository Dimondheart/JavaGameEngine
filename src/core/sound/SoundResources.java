package core.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/** Manages all sound files and relevant data.
 * @author Bryan Bettis
 */
public class SoundResources extends core.GameResourcesBuffer
{
	/** The root directory of all sound resources. */
	private static final String ROOT_DIR = RES_DIR + "sounds/";
	/** List of supported extensions for sound files. */
	private static final String[] EXT_SUPPORTED = {
			".wav"
			};
	
	/** All the sound data, associated with their relative path in the sounds
	 * resources folder.
	 */
	private static ConcurrentHashMap<String, byte[]> sounds;
	
	/** Preloads all sounds. */
	public SoundResources()
	{
		sounds = new ConcurrentHashMap<String, byte[]>();
		loadAll();
	}
	
	@Override
	public void loadAll()
	{
		loadAll(ROOT_DIR, "sounds");
	}

	@Override
	public void load(String filePath)
	{
		byte[] data;
		// File is not a supported format
		if (!extensionSupported(EXT_SUPPORTED, filePath))
		{
			return;
		}
		// Get the file input stream
		InputStream is = getInputStream("/" + ROOT_DIR + filePath);
		// TODO optimize the following
		while (true)
		{
			// Bytes loaded from the input stream
			LinkedList<Byte> bytes = new LinkedList<Byte>();
			// Get the next byte
			int next;
			try
			{
				next = is.read();
			}
			// Read failed, stop loading this file
			catch (IOException e)
			{
				return;
			}
			// End of buffer, turn linked list into array of bytes and stop reading
			if (next <= -1)
			{
				data = new byte[bytes.size()];
				for (int i = 0; i < data.length; ++i)
				{
					data[i] = bytes.get(i);
				}
				break;
			}
			// Add the next byte to the linked list
			bytes.add((byte) next);
		}
		// Store the data in the map
		sounds.put(filePath, data);
	}
	
	@Override
	public InputStream getRes(String sound)
	{
		// If the resource exists, return an input stream for reading it
		if (resExists(sound))
		{
			return new ByteArrayInputStream(sounds.get(sound));
		}
		// Resource doesn't exist, return null
		else
		{
			return null;
		}
	}
	
	@Override
	public boolean resExists(String sound)
	{
		return sounds.containsKey(sound);
	}
}
