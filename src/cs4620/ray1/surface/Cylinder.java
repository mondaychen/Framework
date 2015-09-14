package cs4620.ray1.surface;

import cs4620.ray1.IntersectionRecord;
import cs4620.ray1.Ray;
import egl.math.Vector3d;

public class Cylinder extends Surface {

  /** The center of the bottom of the cylinder  x , y ,z components. */
  protected final Vector3d center = new Vector3d();
  public void setCenter(Vector3d center) { this.center.set(center); }

  /** The radius of the cylinder. */
  protected double radius = 1.0;
  public void setRadius(double radius) { this.radius = radius; }

  /** The height of the cylinder. */
  protected double height = 1.0;
  public void setHeight(double height) { this.height = height; }

  public Cylinder() { }

  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param ray the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
	// TODO#A2 (extra credit): Fill in this function, and write an xml file with a cylinder in it.
	
    return false;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Cylinder " + center + " " + radius + " " + height + " "+ shader + " end";
  }
}
