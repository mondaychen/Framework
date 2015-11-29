package cs4621.demos;

import blister.GameScreen;
import blister.GameTime;
import egl.*;
import egl.math.Matrix4;
import egl.math.Vector4;
import org.lwjgl.opengl.GL11;

/**
 * Created by Monday on 15/11/29.
 */
public class FinalProject extends GameScreen {
	GLBuffer vb, ib;
	int indexCount;

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
		program.quickCreateResource("sky_sea", "cs4621/demos/shaders/sky_sea.vert", "cs4621/demos/shaders/sky_sea.frag", null);

		// Set box vertex positions
		float [] vertexPositions = {
				-0.5f, -0.5f,      // vertex 0
				0.5f, -0.5f,      // vertex 1
				0.5f,  0.5f,      // vertex 2
				-0.5f,  0.5f       // vertex 3
		};
		vb = GLBuffer.createAsVertex(vertexPositions, 2, GL.BufferUsageHint.StaticDraw);

		// Set box triangle indices
		int [] trianglesIndices = {
				0, 1, 2,
				0, 2, 3
		};
		ib = GLBuffer.createAsIndex(trianglesIndices, GL.BufferUsageHint.StaticDraw);
		indexCount = trianglesIndices.length;

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

		// Set view projection to identity matrix
		GLUniform.setST(program.getUniform("VP"), new Matrix4(), false);
		// White color
//		GLUniform.set(program.getUniform("uGridColor"), new Vector4(1, 1, 1, 1));

		// Use box vertices and indices
		vb.useAsAttrib(program.getAttribute("vPos"));
		ib.bind();
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL.GLType.UnsignedInt, 0);
		ib.unbind();

		GLProgram.unuse();
	}

}
