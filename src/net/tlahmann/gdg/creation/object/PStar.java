package net.tlahmann.gdg.creation.object;

import processing.core.PApplet;
import processing.core.PVector;

public class PStar {
	private PApplet parent;
	private PVector[] points;
	private PVector center;

	public PStar(PApplet p, float[] pos, int rad, int ends) {
		parent = p;

		center = new PVector(pos[0], pos[1], 0);
		points = new PVector[ends];
		int radius = rad;
		for (int i = 0; i < ends; i++) {
			float d = PApplet.map(i, 0, ends, 0, PApplet.TWO_PI) - PApplet.HALF_PI;
			points[i] = new PVector(center.x + PApplet.cos(d) * radius, center.y + PApplet.sin(d) * radius);
		}
	}

	public void display() {
		parent.stroke(255);
		parent.strokeCap(PApplet.SQUARE);
		parent.strokeWeight(3);
		for (int i = 0; i < points.length; i++) {
			parent.line(center.x, center.y, points[i].x, points[i].y);
		}
	}
}
