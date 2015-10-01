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

public class TwoBoxesScreen extends GameScreen {

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
        
		// Set box vertex positions
		float [] vertexPositions = {
			-0.5f, -0.5f,      // vertex 0
			 0.5f, -0.5f,      // vertex 1
			 0.5f,  0.5f,      // vertex 2
			-0.5f,  0.5f       // vertex 3
		};
		vb = GLBuffer.createAsVertex(vertexPositions, 2, BufferUsageHint.StaticDraw);
		
		// Set box triangle indices
		int [] trianglesIndices = {
			0, 1, 2,
			0, 2, 3
		};
		ibTriangles = GLBuffer.createAsIndex(trianglesIndices, BufferUsageHint.StaticDraw);
		indexCountTriangles = trianglesIndices.length;
		
		// Set box line indices
		int [] linesIndices = {
			0, 1,
			1, 2,
			2, 3,
			3, 0
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
		Matrix4 tr = Matrix4.createScale(0.4f); 
	    tr.mulAfter(Matrix4.createTranslation(new Vector3(-0.25f, 0.0f, 0.0f)));
	    GLUniform.setST(program.getUniform("VP"), tr, false);
	    
	    // Drawing
		ibTriangles.bind();
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCountTriangles, GLType.UnsignedInt, 0);
		ibTriangles.unbind();
		
		// Transformation
		tr = Matrix4.createScale(0.4f); 
	    tr.mulAfter(Matrix4.createTranslation(new Vector3(0.25f, 0.0f, 0.0f)));
	    GLUniform.setST(program.getUniform("VP"), tr, false);
	    
		// Drawing
		ibLines.bind();
		GL11.glDrawElements(GL11.GL_LINES, indexCountLines, GLType.UnsignedInt, 0);
		ibLines.unbind();
		
		GLProgram.unuse();
	}

}
