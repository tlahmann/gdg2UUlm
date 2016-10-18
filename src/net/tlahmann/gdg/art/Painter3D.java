package net.tlahmann.gdg.art;

import net.tlahmann.gdg.art.object.BallSpawner;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PVector;

public class Painter3D extends PApplet {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 720;

	BallSpawner ps;
	PeasyCam cam;

	public void setup() {
		ps = new BallSpawner(this, new PVector(width / 2, height / 2));

		cam = new PeasyCam(this, (double) width / 2.d, (double) height / 2.d, 0.d, 1000);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(500);
		frameRate(24);
	}

	public void settings() {
		size(WIDTH, HEIGHT, P3D);
	}

	public void draw() {
		background(0);
		ps.addParticle();
		ps.run();
		// rotateX(-.5f);
		// rotateY(-.5f);
		// fill(255, 0, 0);
		// box(30);
		// pushMatrix();
		// translate(0, 0, 20);
		// fill(0, 0, 255);
		// box(5);
		// popMatrix();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Painter3D.class.getName() });
	}
}
