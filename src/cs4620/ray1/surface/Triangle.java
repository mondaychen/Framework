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
    double g, h, i, j, k, l;
    g = rayIn.direction.x;
    h = rayIn.direction.y;
    i = rayIn.direction.z;
    Vector3d v0 = owner.getPosition(index.x);
    j = v0.x - rayIn.origin.x;
    k = v0.y - rayIn.origin.y;
    l = v0.z - rayIn.origin.z;

    double M = a * (e * i - h * f) + b * (g * f - d * i) + c * (d * h - e * g);
    double beta = ( j * (e * i - h * f) + k * (g * f - d * i) + l * (d * h - e * g) ) / M;
    double gamma = ( i * (a * k - j * b) + h * (j * c - a * l) + g * (b * l - k * c) )/ M;
    double t = - ( f * (a * k - j * b) + e * (j * c - a * l) + d * (b * l - k * c) )/ M;

    if (t < rayIn.start || t > rayIn.end) {
      return false;
    }
    if (gamma < 0 || gamma > 1) {
      return false;
    }
    if (beta < 0 || beta > 1 - gamma) {
      return false;
    }

    outRecord.surface = this;
    outRecord.t = t;
    rayIn.evaluate(outRecord.location, t);

    if (norm != null) {
      outRecord.normal.set(norm);
    } else {
      outRecord.normal.setZero()
              .addMultiple(1 - beta - gamma, owner.getNormal(index.x))
              .addMultiple(beta, owner.getNormal(index.y))
              .addMultiple(gamma, owner.getNormal(index.z));
    }
    outRecord.normal.normalize();

    if (owner.hasUVs()) {
      outRecord.texCoords.setZero()
              .addMultiple(1 - beta - gamma, owner.getUV(index.x))
              .addMultiple(beta, owner.getUV(index.y))
              .addMultiple(gamma, owner.getUV(index.z));
    }

	return true;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Triangle ";
  }
}