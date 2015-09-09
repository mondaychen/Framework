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

		int m = opt.divisionsLatitude;
		int n = opt.divisionsLongitude;
		float r = opt.innerRadius;
		outData.vertexCount = n * m + n + 1 + m;
		outData.indexCount = (m * n * 2) * 3;

		// Create Storage Spaces
		outData.positions = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.uvs = NativeMem.createFloatBuffer(outData.vertexCount * 2);
		outData.normals = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.indices = NativeMem.createIntBuffer(outData.indexCount);

		double pi=Math.PI;
		double angle=2*pi/n;
		double angleofr=2*pi/m;
        
		float y = (1.0f - r) / 2;
		float d = (1.0f + r) / 2;
		
		// layer of m

		for(int k=0;k<m;k++)
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
		for(int i=0;i<m;i++)
		{
			outData.positions.put(new float[]{0,y*(float)Math.cos(angleofr*i),-d+y*(float)Math.sin(angleofr*i)});

		}
	
		
		
		// Add Normals For 2nr Faces
		
		//for layer of m
		
		for(int k=0;k<m;k++)
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
		for(int k=0;k<m;k++)
		{
			float radius=(float) (d-y*Math.sin(angleofr*k));
			
			float z0=-d;
				
				outData.normals.put(0);
				outData.normals.put(y*(float)Math.cos(angleofr*k));
				outData.normals.put(-radius-z0);

		}

		
		
		// Add UV Coordinates
		for(int k=0;k<m;k++)
		{
			for(int i = 0; i < n; i++)
			{
				float[] uvs = {i*1.0f/n,1-k*1.0f/m};
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
		for(int i = 0; i < m; i++)
		{
			float[] uvs4 = {1.0f,1.0f-1.0f*i/m};
			 outData.uvs.put(uvs4);
		}

		
		// Add Indices
		
		
        for(int k=0;k<m-1;k++)
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
        
        for(int k=0;k<m-1;k++)
        {
        	outData.indices.put((k+1)*n-1);
			outData.indices.put((k+2)*n-1);
			outData.indices.put(n*m+n+1+1+k);
			outData.indices.put((k+1)*n-1);
			outData.indices.put(n*m+n+1+1+k);
			outData.indices.put(n*m+n+1+k);
        }
        
        
        
        
        
        for(int i=0;i<n-1;i++)
        {
        	outData.indices.put(i+n*m);
			outData.indices.put(i+1+n*m);
			outData.indices.put(n*(m-1)+i);
			outData.indices.put(i+1+n*m);
			outData.indices.put(n*(m-1)+i+1);
			outData.indices.put(n*(m-1)+i);
        	
        }
        
        
        
       
        
        
        outData.indices.put(n*m+n);
        outData.indices.put(m*n-1);
    	outData.indices.put(n*m+n-1);
    	
    	outData.indices.put(n*m+n);
		outData.indices.put(n*m+n+m);
		outData.indices.put(m*n-1);
	

        
        

		
		
		// #SOLUTION END
	}
}
