package cs4620.ray2.shader;

import cs4620.ray2.RayTracer;

import java.util.ArrayList;

import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Light;
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
	
		Vector3d outgoing = ray.origin.clone().sub(record.location).normalize();
		Vector3d reflectlight = new Vector3d();
		Vector3d refractlight = new Vector3d();
		
		Vector3d normal = record.normal.normalize();
		outIntensity.setZero();
		
		Colord reflect = new Colord();
		Colord refract = new Colord();
		
		double cosangle = outgoing.clone().dot(normal);
		double R = fresnel(normal, outgoing, refractiveIndex);
		
		double theta1 = Math.acos(cosangle);
				
		if(cosangle < 0) {
			theta1 = Math.PI - theta1;
			normal = normal.clone().negate();
			refractiveIndex = 1 / refractiveIndex;
			R = fresnel(normal.clone().negate(), outgoing, refractiveIndex);
		}
		
		reflectlight.set(normal.clone().mul(2 * Math.cos(theta1)).sub(outgoing));
		Ray reflectray = new Ray(record.location, reflectlight);
		
		
		reflectray.makeOffsetRay();
		RayTracer.shadeRay(reflect, scene, reflectray, depth + 1);
		
		outIntensity.add(reflect.mul(R));
		
		if(R != 1) {
			double theta2 = Math.asin(Math.sin(theta1) / refractiveIndex);
			refractlight.set(outgoing.clone().negate().add(normal.clone().mul(Math.cos(theta1))).
					div(refractiveIndex).sub(normal.clone().mul(Math.cos(theta2))));
			Ray refractray = new Ray(record.location, refractlight);
			refractray.makeOffsetRay();
			
			RayTracer.shadeRay(refract, scene, refractray, depth + 1);
			outIntensity.add(refract.mul(1 - R));
		}
		
	
	}  
    
}