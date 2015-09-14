package cs4620.ray1.surface;

import cs4620.ray1.IntersectionRecord;
import cs4620.ray1.Ray;
import egl.math.Vector3d;
import egl.math.Vector3i;
import cs4620.ray1.shader.Shader;

/**
 * Represents a single triangle, part of a triangle mesh
 *
 * @author ags
 */
public class Triangle extends Surface {
  /** The normal vector of this triangle, if vertex normals are not specified */
  Vector3d norm;
  
  /** The mesh that contains this triangle */
  Mesh owner;
  
  /** 3 indices to the vertices of this triangle. */
  Vector3i index;
  
  double a, b, c, d, e, f;
  public Triangle(Mesh owner, Vector3i index, Shader shader) {
    this.owner = owner;
    this.index = new Vector3i(index);
    
    Vector3d v0 = owner.getPosition(index.x);
    Vector3d v1 = owner.getPosition(index.y);
    Vector3d v2 = owner.getPosition(index.z);
    
    if (!owner.hasNormals()) {
    	Vector3d e0 = new Vector3d(), e1 = new Vector3d();
    	e0.set(v1).sub(v0);
    	e1.set(v2).sub(v0);
    	norm = new Vector3d();
    	norm.set(e0).cross(e1);
    }
    a = v0.x-v1.x;
    b = v0.y-v1.y;
    c = v0.z-v1.z;
    
    d = v0.x-v2.x;
    e = v0.y-v2.y;
    f = v0.z-v2.z;
    
    this.setShader(shader);
  }

  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param rayIn the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
    // TODO#A2: fill in this function.
	  
	return false;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Triangle ";
  }
}