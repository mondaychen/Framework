package cs4620.common.texture;

import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector2i;
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
		
		int row = (int)Math.round(u/cellSize);
		int col = (int)Math.round(v/cellSize);
		
		float localU = u % cellSize - 0.5f * cellSize; // (-cellSize/2, cellSize/2)
		float localV = v % cellSize - 0.5f * cellSize;
		
		float finalX = 0.5f;
		float finalY = 0.5f;
		
		if(Math.sqrt(Math.pow(localU, 2) + Math.pow(localV, 2)) < actualBumpRadius){
			finalX -= Math.sin(localU * Math.PI);
			finalY -= Math.sin(localV * Math.PI);
		}
		
		Colord colord = new Colord(finalX, finalY, 1);
		outColor.set(colord);
	}
}
