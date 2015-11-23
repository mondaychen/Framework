package cs4620.ray2.shader;

import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Light;
import cs4620.ray2.Ray;
import cs4620.ray2.Scene;
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
	public void shade(Colord outIntensity, Scene scene, Ray ray, IntersectionRecord record, int depth) {
		
		Vector3d incoming = new Vector3d();
		Vector3d outgoing = new Vector3d();
		outgoing.set(ray.origin).sub(record.location).normalize();

		Colord color = new Colord();
		Ray shadowRay = new Ray();
		
		outIntensity.setZero();
		for(Light light : scene.getLights()) {
			if(!isShadowed(scene, light, record, shadowRay)) {
				incoming.set(light.getDirection(record.location)).normalize();
				
				double dotProd = record.normal.dot(incoming);
				if (dotProd <= 0)
					continue;
				else {
					Vector3d halfVec = new Vector3d();
					halfVec.set(incoming).add(outgoing).normalize();
					
					double halfDotNormal = Math.max(0.0, halfVec.dot(record.normal));
					double factor = Math.pow(halfDotNormal, exponent);
					double rSq = light.getRSq(record.location);
					
					color.set((texture == null) ? diffuseColor :
						texture.getTexColor(record.texCoords))
						 .mul(dotProd)
						 .addMultiple(factor, specularColor)
						 .mul(light.intensity)
						 .div(rSq);
					
					outIntensity.add(color);
				}
			}
		}
	}

}