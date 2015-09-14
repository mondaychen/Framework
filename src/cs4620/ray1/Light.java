package cs4620.ray1;

import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector3d;

/**
 * This class represents a basic point light which is infinitely small and emits
 * a constant power in all directions. This is a useful idealization of a small
 * light emitter.
 *
 * @author ags
 */
public class Light {
	
	/** Where the light is located in space. */
	public final Vector3d position = new Vector3d();
	public void setPosition(Vector3d position) { this.position.set(position); }
	
	/** How bright the light is. */
	public final Colord intensity = new Colord(Color.White);
	public void setIntensity(Colord intensity) { this.intensity.set(intensity); }
	
	/**
	 * Default constructor.  Produces a unit intensity light at the origin.
	 */
	public Light() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "light: " + position + " " + intensity;
	}
}