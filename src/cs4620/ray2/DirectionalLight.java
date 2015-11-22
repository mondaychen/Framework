package cs4620.ray2;


import egl.math.Vector3d;

/**
 * This class represents a basic point light which is infinitely small and emits
 * a constant power in all directions. This is a useful idealization of a small
 * light emitter.
 *
 * @author ags
 */
public class DirectionalLight extends Light {
	
	
	/** Direction of the light. */
	public final Vector3d direction = new Vector3d();
	public void setDirection(Vector3d direction) { this.direction.set(direction); }

	/** Get direction from light to shaded point. */
	public Vector3d getDirection(Vector3d point) {
		return direction;
	}
	
	/**Get radius square from light to shaded point. */
	public double getRSq(Vector3d point) {
		return 1.0;
	}
	
	public double getShadowRayEnd(Vector3d point) {
		return Double.MAX_VALUE;
	}
	
	/**
	 * Default constructor.  Produces a unit intensity light at the origin.
	 */
	public DirectionalLight() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "Directionlight: " + direction + " " + intensity;
	}
}