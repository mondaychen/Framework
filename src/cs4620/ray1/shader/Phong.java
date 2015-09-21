package cs4620.ray1.shader;

import cs4620.ray1.IntersectionRecord;
import cs4620.ray1.Light;
import cs4620.ray1.Ray;
import cs4620.ray1.Scene;
import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector3d;

/**
 * A Phong material.
 *
 * @author ags, pramook
 */
public class Phong extends Shader {

	/** The color of the diffuse reflection. */
	protected final Colord diffuseColor = new Colord(Color.White);
	public void setDiffuseColor(Colord diffuseColor) { this.diffuseColor.set(diffuseColor); }

	/** The color of the specular reflection. */
	protected final Colord specularColor = new Colord(Color.White);
	public void setSpecularColor(Colord specularColor) { this.specularColor.set(specularColor); }

	/** The exponent controlling the sharpness of the specular reflection. */
	protected double exponent = 1.0;
	public void setExponent(double exponent) { this.exponent = exponent; }

	public Phong() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "phong " + diffuseColor + " " + specularColor + " " + exponent + " end";
	}

	/**
	 * Evaluate the intensity for a given intersection using the Phong shading model.
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
		// 4) Compute the color of the point using the Phong shading model. Add this value
		//    to the output.
		Vector3d viewDirection = new Vector3d();
		Vector3d lightDirection = new Vector3d();
		Ray shadowRay = new Ray();
		viewDirection.set(ray.origin).sub(record.location).normalize();

		outIntensity.setZero();
		for (Light light: scene.getLights()) {
			if (!this.isShadowed(scene, light, record, shadowRay)) {
				lightDirection.set(light.position).sub(record.location).normalize();
				Vector3d halfVector = viewDirection.clone().add(lightDirection).normalize();
				double lightDotNormal = Math.max(lightDirection.dot(record.normal), 0);
				double halfDotNormal = Math.max(halfVector.dot(record.normal), 0);
				Colord color = new Colord();
				color.addMultiple(lightDotNormal, diffuseColor)
						.addMultiple(Math.pow(halfDotNormal, exponent), specularColor)
						.mul(light.intensity)
						.div(light.position.distSq(record.location));
				outIntensity.add(color);
			}
		}
	}

}