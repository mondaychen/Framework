package cs4620.ray1.surface;

import cs4620.ray1.IntersectionRecord;
import cs4620.ray1.Ray;
import egl.math.Vector3d;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
  
  /** The center of the sphere. */
  protected final Vector3d center = new Vector3d();
  public void setCenter(Vector3d center) { this.center.set(center); }
  
  /** The radius of the sphere. */
  protected double radius = 1.0;
  public void setRadius(double radius) { this.radius = radius; }
  
  protected final double M_2PI = 2*Math.PI;
  
  public Sphere() { }
  
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
    // TODO#A2: fill in this function.
	 double origindotdirec = rayIn.origin.clone().dot(rayIn.direction.normalize());
	 double direcdotcenter = rayIn.direction.clone().dot(center);
	 double origindotcenter = rayIn.origin.clone().dot(center);
	 double centerdotcenter = center.clone().dot(center);
	 double p = origindotdirec-direcdotcenter;
	 double d2 = Math.pow(p,2) - rayIn.origin.clone().dot(rayIn.origin)+2*origindotcenter-centerdotcenter+Math.pow(this.radius, 2);
	 if(d2<0) return false;
	 double d  = -Math.sqrt(d2);
	 double t = -p + d;
	 if(t<0) return false;
	 Vector3d normal = rayIn.origin.clone().addMultiple(t, rayIn.direction).sub(center);
	 Vector3d location = normal;
 	 outRecord.location.set(location);
 	 outRecord.normal.set(normal).normalize();
 	 outRecord.t = t;
     outRecord.surface = this;
     return true;
  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "sphere " + center + " " + radius + " " + shader + " end";
  }

}