/** All resources (sounds, graphics, etc.) for a game.
 * <br>
 * <br> To access a resource
 * file, you need to either <b>(1)</b> get the related system's resource
 * manager (for example GfxManager.getResManager() ) and get the resource
 * from it, or <b>(2)</b> just specify the location of the sound file when
 * accessing a system (for example GfxManager.drawImage(..., "image2.png",...)
 * ).<br>
 * <br>
 * Notes:<br>
 * - Folders and file names are separated by "/" slashes regardless of what
 * your OS's file system uses.<br>
 * - Resources are relative to the root folder for their type, for example
 * you only have to specify "background-images/bg1.png" instead of
 * "game/resources/graphics/background-images/bg1.png".
 */
package game.resources;