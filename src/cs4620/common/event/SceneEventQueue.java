package cs4620.common.event;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import cs4620.common.Scene;

public class SceneEventQueue {
	public final Scene scene;
	public final LinkedBlockingQueue<SceneEvent> queue = new LinkedBlockingQueue<>();
	
	public SceneEventQueue(Scene s) {
		scene = s;
	}

	public void getEvents(ArrayList<SceneEvent> a) {
		queue.drainTo(a);
	}
	public void addEvent(SceneEvent e) {
		queue.offer(e);
	}
}
