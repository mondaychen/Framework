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
		
		Vector3d viewDirection = ray.direction.clone().negate().normalize();		
		Vector3d Normal = record.normal.clone().normalize();
		
		Vector3d reflectlight = new Vector3d();
		Vector3d refractlight = new Vector3d();
		
		Colord reflect = new Colord();
		Colord refract = new Colord();
		Colord color = new Colord();
		
		double costheta1 = viewDirection.dot(Normal);
		
		double theta1 = Math.acos(costheta1);
		
		if(costheta1 >= 0) {
			
			reflectlight = Normal.clone().mul(2 * Math.cos(theta1)).sub(viewDirection);
			double R = fresnel(Normal, viewDirection, refractiveIndex);
					
			RayTracer.shadeRay(reflect, scene, new Ray(record.location, reflectlight), 6);
			
		    System.out.println(reflectlight);
		    

			color.add(reflect.mul(R));
			
			if(R != 1) {
				double theta2 = Math.asin(Math.sin(theta1) / refractiveIndex);
				Vector3d refp = Normal.clone().mul(Math.cos(theta1)).sub(viewDirection).normalize();
				refractlight = Normal.clone().negate().normalize().add(refp.mul(Math.tan(theta2)));
				
				System.out.println(refractlight);
				
				RayTracer.shadeRay(refract, scene, new Ray(record.location, refractlight), 6);
				
				 System.out.println(refract);
				 
				color.add(refract.mul(1 - R));			
			}
			
		}
		
		else {
			theta1 = Math.PI - theta1;
			reflectlight = Normal.clone().mul(2 * Math.cos(theta1)).sub(viewDirection);
			
			double R = fresnel(Normal.clone().negate(), viewDirection, refractiveIndex);
			
			RayTracer.shadeRay(reflect, scene, new Ray(record.location, reflectlight), 6);
			color.add(reflect.mul(R));
			
			if(R != 1) {
				double theta2 = Math.asin(refractiveIndex * Math.sin(theta1));
				Vector3d refp = Normal.clone().negate().mul(Math.cos(theta1)).sub(viewDirection).normalize();
				refractlight = Normal.clone().normalize().add(refp.mul(Math.tan(theta2)));
				
				RayTracer.shadeRay(refract, scene, new Ray(record.location, refractlight), 6);
				color.add(refract.mul(1 - R));			
			}	
		}  
		outIntensity.set(color);
    
	}
}