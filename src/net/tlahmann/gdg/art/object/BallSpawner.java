package net.tlahmann.gdg.art.object;

import java.util.ArrayList;

import net.tlahmann.gdg.art.Painter3D;
import processing.core.PVector;

public class BallSpawner {
	Painter3D canvas;
	ArrayList<Ball> particles;
	PVector origin;

	public BallSpawner(Painter3D p, PVector location) {
		canvas = p;
		origin = location.copy();
		particles = new ArrayList<Ball>();
	}

	public void addParticle() {
		particles.add(new Ball(canvas, origin));
	}

	public void run() {
		// iterate backwards because if we remove one we get exceptions if we go
		// forward
		for (int i = particles.size() - 1; i >= 0; i--) {
			Ball p = particles.get(i);
			p.run();
//			for (Ball b : particles) {
//				if (p != b)
//					p.gravity(b);
//			}
			if (p.isDead()) {
				particles.remove(i);
			}
		}
	}
}
