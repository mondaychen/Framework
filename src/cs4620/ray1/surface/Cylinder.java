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
	double ox,oy,oz,dx,dy,dz,t;
	ox = rayIn.origin.x;
	oy = rayIn.origin.y;
	oz = rayIn.origin.z;
	dx = rayIn.direction.x;
	dy = rayIn.direction.y;
	dz = rayIn.direction.z;
	double zmax = this.center.z + this.height/2;
	double zmin = this.center.z + this.height/2;
	double a = ox * dx + oy * dy;
	double b = 2 * (ox * dx + oy * dy);
	double c = ox * ox + oy * oy -1;
	double root = Math.pow(b, 2) - 4 * a * c;
	if (root < 0 ) return false;
	double t1 = (-b + Math.sqrt(root)) / 2 * a;
	double t2 = (-b - Math.sqrt(root)) / 2 * a;
	double z1 = oz + t1 * dz;
	double z2 = oz + t2 * dz;
    //the round of the cylinder
	if( (z1 < zmax && z1 > zmin) && (z2 < zmax && z2 > zmin) ) {
		 t = Math.min(t1, t2);
		 outRecord.t = t;
		 Vector3d location = new Vector3d ();
		 location.set(ox + t * dx, oy + t * dy, oz + t * dz);
		 outRecord.location.set(location);
		 outRecord.normal.set(ox + t * dx, oy + t * dy, 0).normalize();
		 
	}
	if( (z1 > zmax && z1 < zmin) && (z2 > zmax && z2 < zmin) ) {
		return false;
	}
	if( (z1 < zmax && z1 > zmin) && (z2 > zmax && z2 < zmin) ) {
		 t = t1;
		 outRecord.t = t;
		 Vector3d location = new Vector3d ();
		 location.set(ox + t * dx, oy + t * dy, oz + t * dz);
		 outRecord.location.set(location);
		 outRecord.normal.set(ox + t * dx, oy + t * dy, 0).normalize();
		 
	}
	if( (z1 > zmax && z1 < zmin) && (z2 < zmax && z2 > zmin) ) {
		 t = t2; 
		 outRecord.t = t;
		 Vector3d location = new Vector3d ();
		 location.set(ox + t * dx, oy + t * dy, oz + t * dz);
		 outRecord.location.set(location);
		 outRecord.normal.set(ox + t * dx, oy + t * dy, 0).normalize();
	}
	//the cap of the cylinder
	double t3 = ( zmin - oz ) / dz ;
	double t4 = ( zmax - oz ) / dz ;
	double x3 = ox + t3 * dx;
	double y3 = oy + t3 * dy;
	double x4 = ox + t4 * dx;
	double y4 = oy + t4 * dy;
	double s3 = x3 * x3 + y3 * y3;
	double s4 = x4 * x4 + y4 * y4;
	
    if( s3 < 1 && s4 < 1) {
    	t = Math.min(t3, t4);
    	outRecord.t = t;
        if ( t == t3 ) {
        	outRecord.normal.set( 0, 0, zmin).normalize();
        	outRecord.location.set( x3 , y3 , zmin);
        }
        else {
        	outRecord.normal.set( 0, 0, zmax).normalize();
        	outRecord.location.set( x4 , y4 , zmax);
        }
    }
    if( s3 > 1 && s4 > 1) {
    	return false;
    }
    if( s3 < 1 && s4 > 1) {
    	outRecord.t = t3;
    	outRecord.normal.set( 0, 0, zmin).normalize();
    	outRecord.location.set( x3 , y3 , zmin);
    }
    if( s3 > 1 && s4 < 1) {
    	outRecord.t = t4;
    	outRecord.normal.set( 0, 0, zmax).normalize();
    	outRecord.location.set( x4 , y4 , zmax);
    }
    outRecord.surface = this;
    return false;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Cylinder " + center + " " + radius + " " + height + " "+ shader + " end";
  }
}
