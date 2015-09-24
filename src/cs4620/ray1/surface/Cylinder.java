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
	double ox,oy,oz,dx,dy,dz,t= Double.MAX_VALUE;
	ox = rayIn.origin.x;
	oy = rayIn.origin.y;
	oz = rayIn.origin.z;
	dx = rayIn.direction.x;
	dy = rayIn.direction.y;
	dz = rayIn.direction.z;
	double zmax = this.center.z + this.height/2;
	double zmin = this.center.z - this.height/2;
	double a = dx * dx + dy * dy;
	double b = 2 * (ox * dx + oy * dy);
	double c = ox * ox + oy * oy -1;
	double root = Math.pow(b, 2) - 4 * a * c;
	if (root < 0 ) return false;
	double t1 = (-b + Math.sqrt(root)) / 2 * a;
	double t2 = (-b - Math.sqrt(root)) / 2 * a;
	double t3 = ( zmin - oz ) / dz ;
	double t4 = ( zmax - oz ) / dz ;
	double x,y;

	if (checkCap(t3, ox, dx, oy, dy)) {
		t = Math.min(t, t3);
		//System.out.println (t);
	}
	if (checkCap(t4, ox, dx, oy, dy)) {
		t = Math.min(t, t4);
	}
    
	    
	if (checkround(t1, zmax, zmin, oz, dz)) {
	
		 t = Math.min(t, t1);
	
	}
	if (checkround(t2, zmax, zmin, oz, dz)) {
		t = Math.min(t, t2);
	}    
	if (t == Double.MAX_VALUE) {
		return false;
	}
      
	System.out.println (t);
	 outRecord.t = t;
	
	if (t == t3) {
		x = ox + t * dx;
		y = oy + t * dy;
		outRecord.normal.set(0, 0, zmin).normalize();
	    outRecord.location.set(x, y, zmin);
	    
	}
	
	if (t == t4) {
		x = ox + t * dx;
		y = oy + t * dy;
		outRecord.normal.set(0, 0, zmax).normalize();
	    outRecord.location.set(x, y, zmax);
	

	}

    if ( t== t1 || t ==t2) {
    	x = ox + t * dx;
		y = oy + t * dy;
		double z = oz + t * dz;
      	outRecord.normal.set ( x , y , 0).normalize();
      	outRecord.location.set(x,y,z);
      
    }
    outRecord.surface = this;
	return true;
	

   
  }

  private boolean checkround(double t0, double zmax, double zmin, double oz, double dz) {
	  if ( t0 < 0) return false;
	  double z =  oz + t0 * dz;
	  if ( z <= zmax && z >= zmin) return true;
	  return false;
	  
  }
  private boolean checkCap(double t0, double ox, double dx, double oy, double dy) {
	  if ( t0<0 ) return false;
	  double x = ox + t0 * dx;
	  double y = oy + t0 * dy;
	  double s = x * x + y * y;
	  if ( s < 1 ) return true;
	  return false;
  }
 
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Cylinder " + center + " " + radius + " " + height + " "+ shader + " end";
  }
}
