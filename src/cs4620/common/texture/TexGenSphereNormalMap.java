package cs4620.common.texture;

import egl.math.Color;
import egl.math.Colord;
import egl.math.Matrix3;
import egl.math.Matrix3d;
import egl.math.Vector2i;
import egl.math.Vector3;
import egl.math.Vector3d;

public class TexGenSphereNormalMap extends ACTextureGenerator {
	// 0.5f means that the discs are tangent to each other
	// For greater values discs intersect
	// For smaller values there is a "planar" area between the discs
	private float bumpRadius;
	// The number of rows and columns
	// There should be one disc in each row and column
	private int resolution;
	
	public TexGenSphereNormalMap() {
		this.bumpRadius = 0.5f;
		this.resolution = 10;
		this.setSize(new Vector2i(256));
	}
	
	public void setBumpRadius(float bumpRadius) {
		this.bumpRadius = bumpRadius;
	}
	
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	
	@Override
	public void getColor(float u, float v, Color outColor) {
		// TODO A4
		double cellSize = 1.0/resolution;
		double actualBumpRadius = bumpRadius * cellSize;
		
		double localU = u % cellSize; // (-cellSize/2, cellSize/2)
		double localV = v % cellSize;
		
		if (localU > 0.5f * cellSize) {
			localU -= cellSize;
		}
		if (localV > 0.5f * cellSize) {
			localV -= cellSize;
		}
		
		Colord colord = new Colord(0.5, 0.5, 1);
		
		if(Math.sqrt(Math.pow(localU, 2) + Math.pow(localV, 2)) > actualBumpRadius){
			outColor.set(colord);
			return;
		}
		
		double centerU = u - localU;
		double centerV = v - localV;
		
		Vector3d centerNormal = getNormal(centerU, centerV);
		
		Vector3d thisNormal = getNormal(u, v);
		Vector3d tangent = new Vector3d(thisNormal.z, 0, -thisNormal.x).normalize();
		Vector3d bitangent = tangent.clone().cross(thisNormal).normalize();
		
		Matrix3d mTBN = new Matrix3d(tangent, bitangent, thisNormal);
		
		Vector3d finalNormal = centerNormal.clone();
		mTBN.mul(finalNormal);
		colord.set(finalNormal.x * 0.5 + 0.5, finalNormal.y * 0.5 + 0.5, finalNormal.z * 0.5 + 0.5);

		outColor.set(colord);
	}
	
	private Vector3d getNormal(double u, double v) {
		double radius = 1;
		double theta = 2  * Math.PI * u;
		double phi = Math.PI * (1 - v);
		double _z = -(Math.cos(theta) * Math.sin(phi) * radius);
		double _x = -(Math.sin(theta) * Math.sin(phi) * radius);
		double _y = (Math.cos(phi) * radius);
		
		return new Vector3d(_x, _y, _z).normalize();
	}
}
