package cs4620.ray1.shader;

import java.util.List;

import cs4620.ray1.IntersectionRecord;
import cs4620.ray1.Light;
import cs4620.ray1.Ray;
import cs4620.ray1.Scene;
import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector3d;

/**
 * A Lambertian material scatters light equally in all directions. BRDF value is
 * a constant
 *
 * @author ags
 */
public class Lambertian extends Shader {

	/** The color of the surface. */
	protected final Colord diffuseColor = new Colord(Color.White);
	public void setDiffuseColor(Colord inDiffuseColor) { diffuseColor.set(inDiffuseColor); }

	public Lambertian() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "lambertian: " + diffuseColor;
	}

	/**
	 * Evaluate the intensity for a given intersection using the Lambert shading model.
	 * 
	 * @param outIntensity The color returned towards the source of the incoming ray.
	 * @param scene The scene in which the surface exists.
	 * @param ray The ray which intersected the surface.
	 * @param record The intersection record of where the ray intersected the surface.
	 * @param depth The recursion depth.
	 */
	@Override
	public void shade(Colord outIntensity, Scene scene, Ray ray, IntersectionRecord record) {
		// TODO#A2: Fill in this function.
		// 1) Loop through each light in the scene.
		// 2) If the intersection point is shadowed, skip the calculation for the light.
		//	  See Shader.java for a useful shadowing function.
		// 3) Compute the incoming direction by subtracting
		//    the intersection point from the light's position.
		// 4) Compute the color of the point using the Lambert shading model. Add this value
		//    to the output.
	    int size = scene.getLights().size();
	    outIntensity.set(0,0,0);
	    for (int i =0; i<size;i++)
	    {
	    	Shader shade = record.surface.getShader();
	    	scene.getSurfaces().get(i).intersect(record, ray);
	    	Ray shadowRay = new Ray();
	    	shadowRay.origin.set(record.location);
	    	shadowRay.direction.set(scene.getLights().get(i).position.sub(record.location).normalize());
	        Colord intensity = scene.getLights().get(i).intensity;
	        boolean isshadowed = shade.isShadowed(scene, scene.getLights().get(i), record, shadowRay);
	        if(!isshadowed){
	        	record.normal.normalize();
	    		shadowRay.direction.normalize();
		        double angle = Math.max(shadowRay.direction.dot(record.normal),0.0);
		        double distance = shadowRay.direction.len();
		        intensity = (Colord) intensity.div(distance*distance);       
		        outIntensity = (Colord) intensity.mul(angle).mul(diffuseColor);
	    	
	        }

	    	
	    }
	}

}