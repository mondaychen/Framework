package cs4620.ray2.shader;

import cs4620.ray2.RayTracer;
import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Ray;
import cs4620.ray2.Scene;
import egl.math.Colord;
import egl.math.Vector3d;

/**
 * A Phong material.
 *
 * @author ags, pramook
 */
public class Glass extends Shader {

	/**
	 * The index of refraction of this material. Used when calculating Snell's Law.
	 */
	protected double refractiveIndex;
	public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }


	public Glass() { 
		refractiveIndex = 1.0;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "glass " + refractiveIndex + " end";
	}

	/**
	 * Evaluate the intensity for a given intersection using the Glass shading model.
	 *
	 * @param outIntensity The color returned towards the source of the incoming ray.
	 * @param scene The scene in which the surface exists.
	 * @param ray The ray which intersected the surface.
	 * @param record The intersection record of where the ray intersected the surface.
	 * @param depth The recursion depth.
	 */
	@Override
	public void shade(Colord outIntensity, Scene scene, Ray ray, IntersectionRecord record, int depth) {
		// TODO#A7: fill in this function.
        // 1) Determine whether the ray is coming from the inside of the surface or the outside.
        // 2) Determine whether total internal reflection occurs.
        // 3) Compute the reflected ray and refracted ray (if total internal reflection does not occur)
        //    using Snell's law and call RayTracer.shadeRay on them to shade them
		
	    Vector3d normal = record.normal;
	    Vector3d viewdirection = ray.direction.clone().negate().normalize();
	    
	    Vector3d reflectlight = new Vector3d();
	    
	    Vector3d refractlight = new Vector3d();
	    
	    Vector3d outlight = new Vector3d();
	    
	    Colord colorreflect = new Colord();
	    Colord colorrefract = new Colord();
	 
	    Colord color = new Colord();
	    
	    double cosangle = viewdirection.dot(normal);
    	double angle = Math.acos(cosangle);

	    //light coming from the material;
	    if(cosangle >= 0) {
	    	
	    	reflectlight = viewdirection.mul(2 * cosangle - 1);
	    	Ray outray = new Ray(record.location, reflectlight);
	    	
	    	RayTracer.shadeRay(colorreflect, scene, outray, depth);
	    	
	    	color.add(colorreflect);
	    	
	    	if(refractiveIndex != 1) {
		    	double sinrefract = (1 - refractiveIndex) * Math.sin(angle) / refractiveIndex;
		    	double cosrefract = Math.cos(Math.asin(sinrefract));
		    	refractlight = normal.clone().negate().normalize().div(cosrefract);
		    	
		    	RayTracer.shadeRay(colorrefract, scene, new Ray(record.location, refractlight), depth);
		    	color.add(refractlight);
	    	}
	    }
	    else {
	    	angle = Math.PI - angle;
	    	reflectlight = viewdirection.mul(2 * Math.cos(angle) - 1);
	    	
	    	RayTracer.shadeRay(colorreflect, scene, new Ray(record.location, reflectlight), depth);
	    	color.add(colorreflect);
	    	
	    	if(refractiveIndex != 1) {
	    		double sinrefract = refractiveIndex * Math.sin(angle) / (1 - refractiveIndex);
	    		double cosrefract = Math.cos(Math.asin(sinrefract));
	    		refractlight = normal.clone().div(cosrefract);
	    		
	    		RayTracer.shadeRay(colorrefract, scene, new Ray(record.location, refractlight), depth);
		    	color.add(refractlight);
	    	}
	    }
	    
	    outIntensity.set(color);      
	}
	

}