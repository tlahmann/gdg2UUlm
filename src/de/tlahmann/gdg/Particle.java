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
	int color;

	final float SIZEMULTIPLICATOR = 5;

	final float MASS;
	final float MINMASS = 1f;
	final float MAXMASS = 3;
	final float MINTTL = 100;
	final float MAXTTL = 255;

	final float MINVELOCITY = 0.1f;

	Particle(PApplet p) {
		parent = p;

		MASS = parent.random(MINMASS, MAXMASS);
		location = new PVector(parent.random(0, parent.width), parent.random(0, parent.height));
		velocity = new PVector(5f, 0);
		acceleration = new PVector(0, 0);
		orientation = new PVector(PApplet.radians(0), 0);
		lifespan = parent.random(MINTTL, MAXTTL);
	}

	public Particle(PApplet p, float x, float y, int c) {
		this(p);

		color = c;
		if (x > 0 && y > 0)
			location = new PVector(x, y);
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
		// Location changes by velocity
		PVector foo = new PVector(velocity.x * PApplet.sin(orientation.x), velocity.x * PApplet.cos(orientation.x));
		location.add(foo);
		// We must clear acceleration each frame
		acceleration.mult(0);
		// lifespan--;
	}

	// Draw Mover
	public void display() {
		parent.stroke(color, lifespan);
		parent.strokeWeight(MASS * SIZEMULTIPLICATOR);
		parent.point(location.x, location.y);
	}

	// Bounce off edges of window
	public void checkEdges() {
		if (location.y >= parent.height - MASS) {
			// hit bottom
			float angle = PVector.angleBetween(orientation, new PVector(1*PApplet.PI/2,0,0));
			orientation.x += 2*(PApplet.PI-angle);
			velocity.x *= 0.9f;
		} else if (location.y <= 0 + MASS) {
			// hit top
			float angle = PVector.angleBetween(orientation, new PVector(3*PApplet.PI/2,0,0));
			orientation.x += 2*(PApplet.PI-angle);
			velocity.x *= 0.9f;
		} else if (location.x >= parent.width - MASS) {
			// hit right
			float angle = PVector.angleBetween(orientation, new PVector(0*PApplet.PI/2,0,0));
			orientation.x += 2*(PApplet.PI-angle);
			velocity.x *= 0.9f;
		} else if (location.x <= 0 + MASS) {
			// hit left
			float angle = PVector.angleBetween(orientation, new PVector(2*PApplet.PI/2,0,0));
			orientation.x += 2*(PApplet.PI-angle);
			velocity.x *= 0.9f;
		}
	}

	// Is the particle still useful?
	boolean isDead() {
		if (lifespan < 0.0 || PApplet.abs(velocity.x) < MINVELOCITY) {
			return true;
		} else {
			return false;
		}
	}
}
