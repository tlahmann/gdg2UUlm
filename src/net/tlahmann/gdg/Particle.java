package net.tlahmann.gdg;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
	PApplet parent;

	// location, velocity, and acceleration
	PVector location;
	PVector velocity;
	PVector acceleration;
	float lifespan;
	int[] color;
	boolean fillRegion;

	final float MASS;
	final float MINMASS = 1f;
	final float MAXMASS = 1.5f;
	final float MINTTL = 200;
	final float MAXTTL = 255;

	final float MINVELOCITY = 0.5f;
	final float INITVELOCITY = 1.0f;

	Particle(PApplet p) {
		parent = p;

		final float SIZEMULTIPLICATOR = 5;
		MASS = parent.random(MINMASS, MAXMASS) * SIZEMULTIPLICATOR;
		location = new PVector(parent.random(0, parent.width), parent.random(0, parent.height));
		acceleration = new PVector(0, 0);
		float orientation = PApplet.radians(parent.random(0, 360));
		velocity = new PVector(INITVELOCITY * PApplet.sin(orientation), INITVELOCITY * PApplet.cos(orientation));
		lifespan = parent.random(MINTTL, MAXTTL);
	}

	public Particle(PApplet p, float x, float y, int[] c, int f) {
		this(p);

		color = c;
		fillRegion = (f == 1);
		if (x > 0 && y > 0)
			location = new PVector(x, y);
	}

	public void run() {
		update();
		display();
		checkEdges();
	}

	public void update() {
		// Velocity changes according to acceleration
		velocity.add(acceleration);
		// Location changes by velocity
		location.add(velocity);
		acceleration.mult(0);
		lifespan--;
		// color = parent.color(color[0], color[1], color[2], lifespan--);
		// System.out.println(color);
	}

	// Draw particle
	public void display() {
		if (fillRegion)
			return;
		parent.stroke(color[0], color[1], color[2], lifespan--);
		parent.strokeWeight(MASS);
		parent.point(location.x, location.y);
	}

	// Bounce off edges of window
	public void checkEdges() {
		if (location.y >= parent.height - MASS || location.y <= 0 + MASS) {
			velocity.y *= -0.9f;
		} else if (location.x >= parent.width - MASS || location.x <= 0 + MASS) {
			velocity.x *= -0.9f;
		}
	}

	// Is the particle still useful?
	boolean isDead() {
		if (lifespan < 0.0 || PApplet.abs(velocity.mag()) < MINVELOCITY) {
			return true;
		} else {
			return false;
		}
	}
}
