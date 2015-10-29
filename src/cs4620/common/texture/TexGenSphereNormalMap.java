package cs4620.common.texture;

import egl.math.Color;
import egl.math.Colord;
import egl.math.Matrix3;
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
		float cellSize = 1.0f/resolution;
		float actualBumpRadius = bumpRadius * cellSize;
		
		float localU = u % cellSize - 0.5f * cellSize; // (-cellSize/2, cellSize/2)
		float localV = v % cellSize - 0.5f * cellSize;
		
		float finalX = 0.5f;
		float finalY = 0.5f;
		
		Colord colord = new Colord(finalX, finalY, 1);
		
		if(Math.sqrt(Math.pow(localU, 2) + Math.pow(localV, 2)) > actualBumpRadius){
			outColor.set(colord);
			return;
		}
		
		float centerU = u - localU;
		float centerV = v - localV;
		
		Vector3 centerNormal = getNormal(centerU, centerV);
		
		Vector3 thisNormal = getNormal(u, v);
		Vector3 tangent = new Vector3(thisNormal.x, 0f, -thisNormal.z).normalize();
		Vector3 bitangent = thisNormal.clone().cross(tangent).normalize();
		
		Matrix3 mTBN = new Matrix3(tangent, bitangent, thisNormal);
		
		Vector3 finalNormal = centerNormal.clone();
		mTBN.mul(finalNormal);
		colord.set(finalNormal.x, finalNormal.y, finalNormal.z);
		System.out.println(colord);
		outColor.set(colord);
	}
	
	private Vector3 getNormal(float u, float v) {
		float radius = 1;
		float theta = 2f * (float)Math.PI * u;
		float phi = (float)Math.PI * v;

		float _x = (float)(Math.cos(theta) * Math.sin(phi) * radius);
		float _z = (float)(Math.sin(theta) * Math.sin(phi) * radius);
		float _y = (float)(-Math.cos(phi) * radius);
		
		return new Vector3(_x, _y, _z).normalize();
	}
}
