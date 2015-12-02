package cs4620.ray2.shader;

import java.util.ArrayList;

import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Light;
import cs4620.ray2.Ray;
import cs4620.ray2.Scene;
import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector3d;

public class CookTorrance extends Shader {

	/** The color of the diffuse reflection. */
	protected final Colord diffuseColor = new Colord(Color.White);
	public void setDiffuseColor(Colord diffuseColor) { this.diffuseColor.set(diffuseColor); }

	/** The color of the specular reflection. */
	protected final Colord specularColor = new Colord(Color.White);
	public void setSpecularColor(Colord specularColor) { this.specularColor.set(specularColor); }

	/** The roughness controlling the roughness of the surface. */
	protected double roughness = 1.0;
	public void setRoughness(double roughness) { this.roughness = roughness; }

	/**
	 * The index of refraction of this material. Used when calculating Snell's Law.
	 */
	protected double refractiveIndex;
	public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }
	
	public CookTorrance() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "CookTorrance " + diffuseColor + " " + specularColor + " " + roughness + " end";
	}

	/**
	 * Evaluate the intensity for a given intersection using the CookTorrance shading model.
	 *
	 * @param outIntensity The color returned towards the source of the incoming ray.
	 * @param scene The scene in which the surface exists.
	 * @param ray The ray which intersected the surface.
	 * @param record The intersection record of where the ray intersected the surface.
	 * @param depth The recursion depth.
	 */
	@Override
	public void shade(Colord outIntensity, Scene scene, Ray ray, IntersectionRecord record, int depth) {
		// TODO#A7 Fill in this function.
		// 1) Loop through each light in the scene.
		// 2) If the intersection point is shadowed, skip the calculation for the light.
		//	  See Shader.java for a useful shadowing function.
		// 3) Compute the incoming direction by subtracting
		//    the intersection point from the light's position.
		// 4) Compute the color of the point using the CookTorrance shading model. Add this value
		//    to the output.
		ArrayList<Light> lights = (ArrayList)scene.getLights();
		Vector3d normal = record.normal.clone().normalize();
		Vector3d viewDirection = ray.direction.clone().negate().normalize();
		
		
		for(Light light : lights) {
			
			//***Notice **********Not sure if the lightDirection should be negated 
			Vector3d lightDirection = light.getDirection(record.location).clone().negate().normalize();
			Vector3d halfVector = lightDirection.clone().add(viewDirection).normalize();
			
		    Ray shadowray = new Ray(record.location, lightDirection);
			
			if(!isShadowed(scene, light, record,shadowray)) {
				
				double vdoth = viewDirection.dot(halfVector);
				double ndoth = normal.dot(halfVector);
				double ndotv = normal.dot(viewDirection);
				double ndotl = normal.dot(lightDirection);
				
 				double fresTerm = fresnel(halfVector, viewDirection, refractiveIndex);
 				
 				double micropart1 = 1.0 / (Math.pow(roughness, 2) * Math.pow(ndoth, 4));
				double micropart2 = Math.exp((Math.pow(ndoth, 2) - 1) / (Math.pow(roughness, 2) * Math.pow(ndoth, 2)));
				double microD = micropart1 * micropart2;
				
				double geoA = Math.min(1, Math.min(2 * ndoth * ndotv / vdoth, 2 * ndoth * ndotl / vdoth));
				
				double distance = light.getRSq(record.location);
						
				Vector3d Color1 = (specularColor.clone().mul(fresTerm / Math.PI).mul(microD * geoA /(ndotv * ndotl)).add(diffuseColor));
				Vector3d Color2 = light.intensity.clone().mul(Math.max(ndotl, 0)).div(Math.pow(distance, 2));
				
				//Not include the last term of ambient intensity;
				Colord color = new Colord(Color1.mul(Color2));
				outIntensity.set(color);
				
						
			}
			
		}
	
		
	    
		
        
    }
}
