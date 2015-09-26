package cs4620.common.event;

import cs4620.common.SceneObject;

public class SceneTransformationEvent extends SceneEvent {
	public final SceneObject object;
	
	public SceneTransformationEvent(SceneObject o) {
		super(SceneDataType.Object);
		object = o;
	}
}
