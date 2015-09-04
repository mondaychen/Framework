package cs4620.mesh.gen;

import cs4620.mesh.MeshData;
import egl.NativeMem;
import egl.math.Matrix4;
import egl.math.Vector3;

/**
 * Generates A Torus Mesh
 * @author Cristian
 *
 */
public class MeshGenTorus extends MeshGenerator {
	@Override
	public void generate(MeshData outData, MeshGenOptions opt) {
		// TODO#A1 SOLUTION START
		// Calculate Vertex And Index Count
		
		   int n=12,r=8;
		   outData.vertexCount = n * r+n+1+r;
		   outData.indexCount = (r * n* 2)* 3;

		// Create Storage Spaces
		outData.positions = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.uvs = NativeMem.createFloatBuffer(outData.vertexCount * 2);
		outData.normals = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.indices = NativeMem.createIntBuffer(outData.indexCount);
		
		// Add Positions For 6 Faces
		double pi=Math.PI;
		double angle=2*pi/n;
		double angleofr=2*pi/r;
        
		float y=0.375f;
		float d=0.625f;
		
		// layer of r

		for(int k=0;k<r;k++)
		{
			float radius=(float) (d-y*Math.sin(angleofr*k));
			for(int i=0;i<n;i++)
			{
				
				outData.positions.put(new float[]{radius*(float)Math.sin(angle*i),y*(float)Math.cos(angleofr*k),-radius*(float)Math.cos(angle*i)});
			}	

		}
		//
		for(int i=0;i<n;i++)
		{
			outData.positions.put(new float[]{d*(float)Math.sin(angle*i),y,-d*(float)Math.cos(angle*i)});

		}
		outData.positions.put(new float[]{0,y,-d});
		
		//
		for(int i=0;i<r;i++)
		{
			outData.positions.put(new float[]{0,y*(float)Math.cos(angleofr*i),-d+y*(float)Math.sin(angleofr*i)});

		}
	
		
		
		// Add Normals For 2nr Faces
		
		//for layer of r
		
		for(int k=0;k<r;k++)
		{
			float radius=(float) (d-y*Math.sin(angleofr*k));
			for(int i=0;i<n;i++)
			{
				float x0=d*(float)Math.sin(angle*i);
				float z0=-d*(float)Math.cos(angle*i);
				
				outData.normals.put(radius*(float)Math.sin(angle*i)-x0);
				outData.normals.put(y*(float)Math.cos(angleofr*k));
				outData.normals.put(-radius*(float)Math.cos(angle*i)-z0);

			}	

		}
	    
		//for seam;
		for(int i=0;i<n+1;i++)
		{
			outData.normals.put(0);
			outData.normals.put(1);
			outData.normals.put(0);
	
		}
	
		
		//
		for(int k=0;k<r;k++)
		{
			float radius=(float) (d-y*Math.sin(angleofr*k));
			
			float z0=-d;
				
				outData.normals.put(0);
				outData.normals.put(y*(float)Math.cos(angleofr*k));
				outData.normals.put(-radius-z0);

		}

		
		
		// Add UV Coordinates
		for(int k=0;k<r;k++)
		{
			for(int i = 0; i < n; i++)
			{
				float[] uvs = {i*1.0f/n,1-k*1.0f/r};
				 outData.uvs.put(uvs);
			}
		}
		
		
		
	
		for(int i = 0; i < n; i++)
		{
			float[] uvs2 = {i*1.0f/n,0};
			 outData.uvs.put(uvs2);
		}
		
		
		
		
		
		
		//
		
		float[] uvs3 = {1,0};
		 outData.uvs.put(uvs3);
		
		 
		 
		//seam
		for(int i = 0; i < r; i++)
		{
			float[] uvs4 = {1.0f,1.0f-1.0f*i/r};
			 outData.uvs.put(uvs4);
		}
		
		
	

		
		
		
		
		
		
		
		
		
		
		
		
		
		// Add Indices
		
		
        for(int k=0;k<r-1;k++)
        {
        	for(int i=0;i<n-1;i++)
        	{
        		outData.indices.put(n*k+i);
				outData.indices.put(n*(k+1)+i);
				outData.indices.put(n*(k+1)+1+i);
				outData.indices.put(n*k+i);
				outData.indices.put(n*(k+1)+1+i);
				outData.indices.put(n*k+i+1);
        	}
        }
        
        for(int k=0;k<r-1;k++)
        {
        	outData.indices.put((k+1)*n-1);
			outData.indices.put((k+2)*n-1);
			outData.indices.put(n*r+n+1+1+k);
			outData.indices.put((k+1)*n-1);
			outData.indices.put(n*r+n+1+1+k);
			outData.indices.put(n*r+n+1+k);
        }
        
        
        
        
        
        for(int i=0;i<n-1;i++)
        {
        	outData.indices.put(i+n*r);
			outData.indices.put(i+1+n*r);
			outData.indices.put(n*(r-1)+i);
			outData.indices.put(i+1+n*r);
			outData.indices.put(n*(r-1)+i+1);
			outData.indices.put(n*(r-1)+i);
        	
        }
        
        
        
       
        
        
        outData.indices.put(n*r+n);
        outData.indices.put(r*n-1);
    	outData.indices.put(n*r+n-1);
    	
    	outData.indices.put(n*r+n);
		outData.indices.put(n*r+n+r);
		outData.indices.put(r*n-1);
	

        
        

		
		
		// #SOLUTION END
	}
}
