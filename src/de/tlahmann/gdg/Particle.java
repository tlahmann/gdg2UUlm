package de.tlahmann.gdg;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
	PApplet parent;

	// location, velocity, and acceleration
	PVector location;
	PVector velocity;
	PVector acceleration;
	PVector orientation;
	float lifespan;
	int[] colors;


	final float MASS;
	final float MINMASS = 0.5f;
	final float MAXMASS = 3;
	final float MINTTL = 10;
	final float MAXTTL = 255;

	final float MINVELOCITY = 0.5f;

	public Particle(PApplet p) {
		parent = p;

		MASS = parent.random(MINMASS, MAXMASS);
		location = new PVector(parent.random(0, parent.width), parent.random(0, parent.height));
		velocity = new PVector(0, 0);
		acceleration = new PVector(0, 0);
		orientation = new PVector(parent.random(0, 360), 0);
		lifespan = parent.random(MINTTL, MAXTTL);
		colors = new int[] { (int) parent.random(255), (int) parent.random(255), (int) parent.random(255) };
	}

	public void run() {
		update();
		display();
		checkEdges();
	}

	// Newton's 2nd law: F = M * A
	// or A = F / M
	public void applyForce(PVector force) {
		// Divide by mass
		PVector f = PVector.div(force, MASS);
		// Accumulate all forces in acceleration
		acceleration.add(f);
	}

	public void update() {
		// Velocity changes according to acceleration
		velocity.add(acceleration);
		float foo = velocity.x * parent.sin(orientation.x);
		float bar = velocity.x * parent.cos(orientation.x);
		velocity = new PVector(foo, bar);
		// Location changes by velocity
		location.add(velocity);
		// We must clear acceleration each frame
		acceleration.mult(0);
		// lifespan--;
	}

	// Draw Mover
	public void display() {
		parent.stroke(colors[0], colors[1], colors[2], lifespan);
		parent.strokeWeight(MASS * 5);
		parent.point(location.x, location.y);
	}

	// Bounce off edges of window
	public void checkEdges() {
		if (location.y > parent.height || location.y < 0) {
			orientation.x += 180;
			velocity.y *= 0.9; // A little dampening when hitting the
								// bottom
		} else if (location.x > parent.width || location.x < 0) {
			orientation.x += 180;
			velocity.x *= 0.9; // A little dampening when hitting the
								// bottom
		}
	}

	// Is the particle still useful?
	boolean isDead() {
		if (lifespan < 0.0) {
			// || abs(velocity.y) < MINIMALVELOCITY) {
			return true;
		} else {
			return false;
		}
	}
}
