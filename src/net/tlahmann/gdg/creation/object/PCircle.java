package net.tlahmann.gdg.creation.object;

import processing.core.PApplet;
import processing.core.PVector;

public class PCircle {
	private PApplet parent;
	private float radius;
	private PVector center;
	private boolean shrink = true;

	public PCircle(PApplet p, float[] pos, float rad) {
		parent = p;

		center = new PVector(pos[0], pos[1], 0);
		radius = rad;
	}

	public void run() {
		update();
		display();
	}

	void update() {
		if (shrink)
			radius--;
		else
			radius++;
		if (radius < 14 || radius > 100)
			shrink = !shrink;
	}

	void display() {
		parent.noStroke();
		parent.fill(255);
		parent.ellipse(center.x, center.y, radius, radius);
	}
}
