package cs4620.demos;

import org.lwjgl.opengl.GL11;

import blister.GameScreen;
import blister.GameTime;
import egl.GL.BufferUsageHint;
import egl.GL.GLType;
import egl.GLBuffer;
import egl.GLProgram;
import egl.GLUniform;
import egl.RasterizerState;
import egl.math.Matrix4;
import egl.math.Vector3;
import egl.math.Vector4;

public class SierpinskiScreen extends GameScreen {

	GLBuffer vb, ibTriangles, ibLines;
	int indexCountTriangles, indexCountLines;
	
	GLProgram program;
	
	@Override
	public int getNext() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setNext(int next) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPrevious() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setPrevious(int previous) {
		// TODO Auto-generated method stub

	}

	@Override
	public void build() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(GameTime gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntry(GameTime gameTime) {
		program = new GLProgram(false);
		program.quickCreateResource("Grid", "cs4620/gl/Grid.vert", "cs4620/gl/Grid.frag", null);
        
		// Set triangle vertices positions
		float [] vertexPositions = {
				-0.5f,                  0.0f,      // vertex 0
				 0.5f,                  0.0f,      // vertex 1
				 0.0f,  (float)Math.sqrt(3)/2      // vertex 2
			};
		vb = GLBuffer.createAsVertex(vertexPositions, 2, BufferUsageHint.StaticDraw);
				
		// Set triangle line indices
		int [] linesIndices = {
				0, 1,
				1, 2,
				2, 0
			};
		ibLines = GLBuffer.createAsIndex(linesIndices, BufferUsageHint.StaticDraw);
		indexCountLines = linesIndices.length;
		
		RasterizerState.CULL_NONE.set();
		GL11.glClearDepth(1.0);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void onExit(GameTime gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameTime gameTime) {
		// TODO Auto-generated method stub

	}

	public void sierpinski(GLBuffer lines, Matrix4 tr, int k) {
		if (k == 0) {
			GLUniform.setST(program.getUniform("VP"), tr, false);
			
			// Draw the triangle
			ibLines.bind();
			GL11.glDrawElements(GL11.GL_LINES, indexCountLines, GLType.UnsignedInt, 0);
			ibLines.unbind();
		} else {
			Matrix4 next;

			//draw the up triangle
			next = new Matrix4(tr);
			next.mulAfter(Matrix4.createScale(new Vector3(0.5f, 0.5f, 0.5f)));
			next.mulAfter(Matrix4.createTranslation(new Vector3(0.0f, 0.5f / (float)Math.sqrt(3.0f), 0.0f)));
			sierpinski(lines, next, k-1);
			
			//draw the right triangle
			next = new Matrix4(tr);
			next.mulAfter(Matrix4.createScale(new Vector3(0.5f, 0.5f, 0.5f)));
			next.mulAfter(Matrix4.createTranslation(new Vector3(0.25f, -0.25f / (float)Math.sqrt(3.0f), 0.0f)));
			sierpinski(lines, next, k-1);
			
			//draw the left triangle
			next = new Matrix4(tr);
			next.mulAfter(Matrix4.createScale(new Vector3(0.5f, 0.5f, 0.5f)));
			next.mulAfter(Matrix4.createTranslation(new Vector3(-0.25f, -0.25f / (float)Math.sqrt(3.0f), 0.0f)));
			sierpinski(lines, next, k-1);
		}
	}
	
	@Override
	public void draw(GameTime gameTime) {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		program.use();
		
		// White color
		GLUniform.set(program.getUniform("uGridColor"), new Vector4(1, 1, 1, 1));
		
		// Use box vertices
		vb.useAsAttrib(program.getAttribute("vPos"));
		
		// Transformation
		Matrix4 tr = new Matrix4();
	    tr.mulAfter(Matrix4.createTranslation(new Vector3(0.0f, -(float)Math.sqrt(3)/6, 0.0f)));

	    // Draw Sierpinski triangles
		sierpinski(ibLines, tr, 10);		
		
		GLProgram.unuse();
	}

}
