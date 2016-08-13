package net.tlahmann.gdg;

import java.util.ArrayList;

import megamu.mesh.MPolygon;
import megamu.mesh.Voronoi;
import processing.core.PApplet;

public class Region {
	PApplet parent;
	Voronoi myVoronoi;

	Region(PApplet p) {
		parent = p;
	}

	public void run(ArrayList<Particle> ps) {
		update(ps);
		display(ps);
	}

	public void update(ArrayList<Particle> particles) {
		float[][] points = new float[particles.size()][2];
		for (int i = 0; i < particles.size(); i++) {
			points[i][0] = particles.get(i).location.x;
			points[i][1] = particles.get(i).location.y;
		}
		myVoronoi = new Voronoi(points);
	}

	public void display(ArrayList<Particle> particles) {
		// getRegions
		parent.strokeWeight(1);
		parent.stroke(0);
		MPolygon[] myRegions = myVoronoi.getRegions();
		for (int i = 0; i < myRegions.length; i++) {
			// an array of points
			Particle p = particles.get(i);
			int c = p.fillRegion ? p.color : 255;
			parent.fill(c);
			myRegions[i].draw(parent); // draw this shape
		}
	}
}
