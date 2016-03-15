package xyz.digitalcookies.objective.utility;

/** Various additional math functions, not included in the standard
 * Math class, more accurate versions (trig. mainly), and different
 * versions.
 * @author Bryan Charles Bettis
 *
 */
public class ExtendedMath
{
	/** Convert radians to degrees.
	 * @param rad the radian value
	 * @return the equivalent value in degrees
	 */
	public static double radToDeg(double rad)
	{
		return rad * 180.0 / Math.PI;
	}
	
	/** Convert degrees to radians.
	 * @param deg the degree value
	 * @return the equivalent value in radians
	 */
	public static double degToRad(double deg)
	{
		return deg * Math.PI / 180.0;
	}
}
