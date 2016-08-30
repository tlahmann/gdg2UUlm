package net.tlahmann.gdg.creation.object;

import processing.core.PApplet;
import processing.core.PVector;

public class PLine {
	private PApplet parent;
	private PVector start;
	private PVector end;
	private float orientation;

	public PLine(PApplet p) {
		parent = p;

		start = new PVector(parent.width/2, parent.height/2);
		orientation = parent.random(-PApplet.PI, PApplet.PI);
		end = PVector.random2D();
	}

	public void display() {
		parent.stroke(0);
		parent.strokeWeight(5);
		parent.line(start.x, start.y, end.x, end.y);
	}
}
