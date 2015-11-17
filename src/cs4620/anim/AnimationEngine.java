package cs4620.anim;

import java.util.HashMap;

import cs4620.common.Scene;
import cs4620.common.SceneObject;
import cs4620.common.event.SceneTransformationEvent;
import egl.math.Matrix4;
import egl.math.Vector3;
import egl.math.Matrix3;
import egl.math.Quat;

/**
 * A Component Resting Upon Scene That Gives
 * Animation Capabilities
 * @author Cristian
 *
 */
public class AnimationEngine {
	/**
	 * The First Frame In The Global Timeline
	 */
	private int frameStart = 0;
	/**
	 * The Last Frame In The Global Timeline
	 */
	private int frameEnd = 100;
	/**
	 * The Current Frame In The Global Timeline
	 */
	private int curFrame = 0;
	/**
	 * Scene Reference
	 */
	private final Scene scene;
	/**
	 * Animation Timelines That Map To Object Names
	 */
	public final HashMap<String, AnimTimeline> timelines = new HashMap<>();

	/**
	 * An Animation Engine That Works Only On A Certain Scene
	 * @param s The Working Scene
	 */
	public AnimationEngine(Scene s) {
		scene = s;
	}
	
	/**
	 * Set The First And Last Frame Of The Global Timeline
	 * @param start First Frame
	 * @param end Last Frame (Must Be Greater Than The First
	 */
	public void setTimelineBounds(int start, int end) {
		// Make Sure Our End Is Greater Than Our Start
		if(end < start) {
			int buf = end;
			end = start;
			start = buf;
		}
		
		frameStart = start;
		frameEnd = end;
		moveToFrame(curFrame);
	}
	/**
	 * Add An Animating Object
	 * @param oName Object Name
	 * @param o Object
	 */
	public void addObject(String oName, SceneObject o) {
		timelines.put(oName, new AnimTimeline(o));
	}
	/**
	 * Remove An Animating Object
	 * @param oName Object Name
	 */
	public void removeObject(String oName) {
		timelines.remove(oName);
	}

	/**
	 * Set The Frame Pointer To A Desired Frame (Will Be Bounded By The Global Timeline)
	 * @param f Desired Frame
	 */
	public void moveToFrame(int f) {
		if(f < frameStart) f = frameStart;
		else if(f > frameEnd) f = frameEnd;
		curFrame = f;
	}
	/**
	 * Looping Forwards Play
	 * @param n Number Of Frames To Move Forwards
	 */
	public void advance(int n) {
		curFrame += n;
		if(curFrame > frameEnd) curFrame = frameStart + (curFrame - frameEnd - 1);
	}
	/**
	 * Looping Backwards Play
	 * @param n Number Of Frames To Move Backwards
	 */
	public void rewind(int n) {
		curFrame -= n;
		if(curFrame < frameStart) curFrame = frameEnd - (frameStart - curFrame - 1);
	}

	public int getCurrentFrame() {
		return curFrame;
	}
	public int getFirstFrame() {
		return frameStart;
	}
	public int getLastFrame() {
		return frameEnd;
	}
	public int getNumFrames() {
		return frameEnd - frameStart + 1;
	}

	/**
	 * Adds A Keyframe For An Object At The Current Frame
	 * Using The Object's Transformation - (CONVENIENCE METHOD)
	 * @param oName Object Name
	 */
	public void addKeyframe(String oName) {
		AnimTimeline tl = timelines.get(oName);
		if(tl == null) return;
		tl.addKeyFrame(getCurrentFrame(), tl.object.transformation);
	}
	/**
	 * Removes A Keyframe For An Object At The Current Frame
	 * Using The Object's Transformation - (CONVENIENCE METHOD)
	 * @param oName Object Name
	 */
	public void removeKeyframe(String oName) {
		AnimTimeline tl = timelines.get(oName);
		if(tl == null) return;
		tl.removeKeyFrame(getCurrentFrame(), tl.object.transformation);
	}
	
	/**
	 * Loops Through All The Animating Objects And Updates Their Transformations To
	 * The Current Frame - For Each Updated Transformation, An Event Has To Be 
	 * Sent Through The Scene Notifying Everyone Of The Change
	 */

	// TODO A6 - Animation

	public void updateTransformations() {
		// Loop Through All The Timelines
		// And Update Transformations Accordingly
		// (You WILL Need To Use this.scene)
		for (AnimTimeline timeline: timelines.values()) {
			SceneObject object = timeline.object;
			
			// get pair of surrounding frames
			// (function in AnimTimeline)
			AnimKeyframe[] outPair = {new AnimKeyframe(curFrame), new AnimKeyframe(curFrame)};
			timeline.getSurroundingFrames(curFrame, outPair);
			Matrix4 m0 = outPair[0].transformation;
			Matrix4 m1 = outPair[1].transformation;
			
			// get interpolation ratio
			float ratio = getRatio(outPair[0].frame, outPair[1].frame, curFrame);
			
			// interpolate translations linearly
			Vector3 translation = linearlyInterpolate(m0.getTrans(), m1.getTrans(), ratio);
			
			// polar decompose axis matrices
			Matrix3 rotation0 = new Matrix3();
			Matrix3 scale0 = new Matrix3();
			m0.getAxes().polar_decomp(rotation0, scale0);

			Matrix3 rotation1 = new Matrix3();
			Matrix3 scale1 = new Matrix3();
			m1.getAxes().polar_decomp(rotation1, scale1);
			
			// slerp rotation matrix and linearly interpolate scales
			Quat quat0 = new Quat(rotation0);
			Quat quat1 = new Quat(rotation1);
			Matrix3 rotation = new Matrix3();
			Quat.slerp(quat0, quat1, ratio).toRotationMatrix(rotation);
			Matrix3 scale = new Matrix3();
			scale.interpolate(scale0, scale1, ratio);
			
			// combine interpolated R,S,and T
			Matrix4 transformation = new Matrix4(rotation.mulBefore(scale));
			transformation.m[12] = translation.x;
			transformation.m[13] = translation.y;
			transformation.m[14] = translation.z;
			
			// Naive approach
//			object.transformation.set(linearlyInterpolate(outPair[0].transformation,
//					outPair[1].transformation, ratio));
			object.transformation.set(transformation);
			 
			scene.sendEvent(new SceneTransformationEvent(object));
		}
			
	}

	private static Vector3 linearlyInterpolate(Vector3 v1, Vector3 v2, float ratio) {
		return new Vector3(_LI(v1.x, v2.x, ratio), _LI(v1.y, v2.y, ratio), _LI(v1.z, v2.z, ratio));
	}
	
	private static Matrix4 linearlyInterpolate(Matrix4 m1, Matrix4 m2, float ratio) {
		Matrix4 result = new Matrix4();
		for(int i = 0; i < result.m.length; i++) {
			result.m[i] = (m2.m[i] - m1.m[i]) * ratio + m1.m[i];
		}
		return result;
	}
	
	private static float _LI(float n1, float n2, float ratio) {
		return n1 * (1-ratio) + n2 * ratio;
	}

	public static float getRatio(int min, int max, int cur) {
		if(min == max) return 0f;
		float total = max - min;
		float diff = cur - min;
		return diff / total;
	}
}
