package cs4620.ray2;


import egl.math.Vector3d;

/**
 * This class represents a basic point light which is infinitely small and emits
 * a constant power in all directions. This is a useful idealization of a small
 * light emitter.
 *
 * @author ags
 */
public class PointLight extends Light {
	
	/** Where the light is located in space. */
	public final Vector3d position = new Vector3d();
	public void setPosition(Vector3d position) { this.position.set(position); }

	/** Get direction from light to shaded point. */
	public Vector3d getDirection(Vector3d point) {
		Vector3d dir = position.clone().sub(point);
		return dir;
	}
	
	/**Get radius square from light to shaded point. */
	public double getRSq(Vector3d point) {
		return point.distSq(this.position);
	}
	

	public double getShadowRayEnd(Vector3d point) {
		return this.getDirection(point).len();
	}
	
	/**
	 * Default constructor.  Produces a unit intensity light at the origin.
	 */
	public PointLight() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "PointLight: " + position + " " + intensity;
	}
}