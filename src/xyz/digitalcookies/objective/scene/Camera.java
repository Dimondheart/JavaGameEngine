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

package xyz.digitalcookies.objective.scene;

/** A camera contains information used to adjust the viewing area of a
 * scene when it renders, such as an offset or a scale/zoom.
 * @author Bryan Charles Bettis
 */
public interface Camera
{
	/** Get the x offset, in other words how many pixels to the right
	 * a point will need to be drawn. If the camera supports scaling,
	 * this method will apply the scale before it returns precise
	 * the offset value.
	 * @return the value to add to an x coordinate before using it to
	 * 		render
	 */
	public int getX();
	
	public int getX(double x);
	
	/** Get the y offset, in other words how many pixels down a point
	 * will need to be drawn. If the camera supports scaling,
	 * this method will apply the scale before it returns the
	 * offset value.
	 * @return the value to add to a y coordinate before using it to
	 * 		render
	 */
	public int getY();
	
	public int getY(double y);
	
	/** Get the scale/zoom factor. By convention, 0 < scale < 1 should indicate
	 * shrinking drawn objects, while 1 < scale should indicate scaling drawn
	 * objects up. For instance, a scale value of 0.5 should halve the width
	 * and height of a drawn rectangle.
	 * @return the scale factor
	 */
	public double getScale();
	
	public void setScale(double scale);
	
	/** Get the x offset in double precision. By default this will call
	 * getX().
	 * @return the x offset in double precision
	 */
	public default double getPreciseX()
	{
		return getX();
	}
	
	/** Get the y offset in double precision. By default this will call
	 * getY().
	 * @return the y offset in double precision
	 */
	public default double getPreciseY()
	{
		return getY();
	}
}
