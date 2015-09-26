package cs4620.gl;

import cs4620.common.SceneLight;
import cs4620.common.SceneObject;

public class RenderLight extends RenderObject {
	/**
	 * Reference to scene counterpart (specialized)
	 */
	public final SceneLight sceneLight;
	
	public RenderLight(SceneObject o) {
		super(o);
		sceneLight = (SceneLight)sceneObject;
	}
}
