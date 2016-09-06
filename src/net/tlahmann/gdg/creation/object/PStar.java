package net.tlahmann.gdg.creation.object;

import net.tlahmann.gdg.creation.Patterizer;
import processing.core.PApplet;
import processing.core.PVector;

public class PStar {
	private Patterizer parent;
	private PVector[] points;
	private PVector center;

	public PStar(Patterizer p, float[] pos, float radius, float rotation, float ends) {
		parent = p;

		center = new PVector(pos[0], pos[1], 0);
		points = new PVector[(int) Math.ceil(ends)];
		for (int i = 0; i < ends; i++) {
			float d = PApplet.map(i, 0, ends, 0, PApplet.TWO_PI) - PApplet.HALF_PI;
			points[i] = new PVector(center.x + PApplet.cos(d + rotation) * radius,
					center.y + PApplet.sin(d + rotation) * radius);
		}
	}

	public void display() {
		parent.stroke(parent.gui.colors[0]);
		parent.strokeWeight(parent.gui.thickness.y);
		if (!parent.gui.outline) {
			parent.strokeCap(PApplet.SQUARE);
			for (int i = 0; i < points.length; i++) {
				parent.line(center.x, center.y, points[i].x, points[i].y);
			}
		} else {
			parent.strokeCap(PApplet.ROUND);
			int i;
			for (i = 0; i < points.length - 1; i++) {
				parent.line(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
			}
			parent.line(points[i].x, points[i].y, points[0].x, points[0].y);
		}
	}
}
