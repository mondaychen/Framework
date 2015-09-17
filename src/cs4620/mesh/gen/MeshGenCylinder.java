package cs4620.mesh.gen;


import cs4620.mesh.MeshData;
import egl.NativeMem;

/**
 * Generates A Cylinder Mesh
 * @author Cristian (Original)
 * @author Jimmy (Revised 8/25/2015)
 */
public class MeshGenCylinder extends MeshGenerator {
	@Override
	
	public void generate(MeshData outData, MeshGenOptions opt) {
		// Calculate Vertex And Index Count

		int n = opt.divisionsLongitude;
		outData.vertexCount = n * 4+2;
		outData.indexCount = (n * 2 + (n-2) * 2)* 3;

		// Create Storage Spaces
		outData.positions = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.uvs = NativeMem.createFloatBuffer(outData.vertexCount * 2);
		outData.normals = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.indices = NativeMem.createIntBuffer(outData.indexCount);
		
		// Add Positions For 3 Faces
		double pi=Math.PI;
		double angle=2*pi/n;
		

		float y=1.0f;
		
		for(int i=0;i<n;i++)
		{
			
			outData.positions.put(new float[]{(float)Math.sin(angle*i),y,-(float)Math.cos(angle*i)});
		}
	
		for(int i=0;i<n;i++)
		{
	
			outData.positions.put(new float[]{(float)Math.sin(angle*i),-y,-(float)Math.cos(angle*i)});
		}
	
		for(int i=0;i<n;i++)
		{
		
			outData.positions.put(new float[]{(float)Math.sin(angle*i),y,-(float)Math.cos(angle*i)});
		}
		
		for(int i=0;i<n;i++)
		{
	
			outData.positions.put(new float[]{(float)Math.sin(angle*i),-y,-(float)Math.cos(angle*i)});
		}
	    
		//Add positions for the seam
		outData.positions.put(new float[]{0,y,-1});
		outData.positions.put(new float[]{0,-y,-1});
		
		// Add Normals For  Faces
		for(int i = 0;i < n;i++) { outData.normals.put(0); outData.normals.put(1); outData.normals.put(0); }
		for(int i = 0;i < n;i++) { outData.normals.put(0); outData.normals.put(-1); outData.normals.put(0); }
		for(int i = 0;i < 2*n;i++) { outData.normals.put((float)Math.sin(angle*i)); outData.normals.put(0); outData.normals.put(-(float)Math.cos(angle*i)); }

		outData.normals.put(0); outData.normals.put(0); outData.normals.put(-1);
		outData.normals.put(0); outData.normals.put(0); outData.normals.put(-1);
		
		// Add UV Coordinates
     
		//the coordinates of upper face
	
		for(int i = 0; i < n; i++)
		{
			float[] uvs = { 0.75f+0.25f*(float)Math.sin(angle*i),0.75f+0.25f*(float)Math.cos(angle*i) };
			 outData.uvs.put(uvs);
		}
		
		//the coordinates of down face
		for(int i = 0; i < n; i++) 
		{
			float[] uvs1 = { 0.25f+0.25f*(float)Math.sin(angle*i),0.75f+0.25f*(float)Math.cos(angle*i)};
			outData.uvs.put(uvs1);
		}
		
		//the coordinates of the perimeter
		
		for(int i = 0; i < n; i++) 
		{
			float[] uvs2={((n-i)*1.0f/n),0.5f};
			outData.uvs.put(uvs2);
		}

	
		for(int i = 0; i < n; i++) 
		{
			float[] uvs3={((n-i)*1.0f/n),0};
			outData.uvs.put(uvs3);
		}
	
		//the coordinates of the seam
		float[] uvs4 = { 0,0.5f};
	    outData.uvs.put(uvs4);
	    float[] uvs5 = { 0,0};
	    outData.uvs.put(uvs5);
	    
		// Add Indices
	    // Add Indices to the upper face
	    for(int f = 0;f < n-2;f++) {

			outData.indices.put(0);
			outData.indices.put(f+2);
			outData.indices.put(f+ 1);
			
		}
	    
       //Add Indices to the down face
		 for(int f = 0;f < n-2;f++) {

				outData.indices.put(n);
				outData.indices.put(n+f+1);
				outData.indices.put(n+f+2);
				
			}
		
		 //Add Indices to the perimeter
		   for(int f = 0;f < n-1;f++) {

				outData.indices.put(f+2*n+1);
				outData.indices.put(f+3*n);
				outData.indices.put(f+2*n);
				outData.indices.put(f+2*n+1);
				outData.indices.put(f+3*n+1);
				outData.indices.put(f+3*n);
				
			}
		   
		   //Add Indices to the seam
		   outData.indices.put(4*n);
		   outData.indices.put(4*n-1);
			outData.indices.put(3*n-1);
			outData.indices.put(4*n);
			outData.indices.put(4*n+1);
			outData.indices.put(4*n-1);
	
		
		   
	    
	  }  
		
		// #SOLUTION END
	}

