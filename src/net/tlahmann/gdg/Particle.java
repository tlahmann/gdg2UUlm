package net.tlahmann.gdg;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
	Painter parent;

	// location, velocity, and acceleration
	PVector location;
	PVector velocity;
	PVector acceleration;
	float lifespan;
	float[] color;

	final float MASS;
	final float MINMASS = 1f;
	final float MAXMASS = 1.5f;
	final float MAXTTL = 255;

	final float MINVELOCITY = 0.2f;
	final float INITVELOCITY = 0.6f;

	/**
	 * Particle element constructor
	 * 
	 * @param p parent object
	 * @param loc location of the particle; {0,0} for random
	 * @param c color: {r, g, b, a, fillregion}; {0} for b/w
	 * @param v velocity; (-1) for default
	 * @param t lifespawn in frames; (-1) for infinite
	 */
	public Particle(Painter p, float[] loc, float[] c, float v, int t) {
		parent = p;

		location = new PVector(loc[0] == 0 ? parent.random(0, parent.width) : loc[0],
				loc[1] == 0 ? parent.random(0, parent.height) : loc[1]);

		color = c == new float[] { 0 } ? new float[] { 0, 0, 0, 255, 0 } : c.clone();

		float orientation = PApplet.radians(parent.random(0, 360));
		velocity = new PVector((v == -1 ? INITVELOCITY : v) * PApplet.sin(orientation),
				(v == -1 ? INITVELOCITY : v) * PApplet.cos(orientation));

		lifespan = t == -1 ? -1 : t != 0 ? MAXTTL / t : 1;

		final float SIZEMULTIPLICATOR = 5;
		MASS = parent.random(MINMASS, MAXMASS) * SIZEMULTIPLICATOR;
		acceleration = new PVector(0, 0);
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
		color[3] -= lifespan;
		if (isDead()) {
			parent.deadParticles.add(this);
		}
	}

	// Draw particle
	public void display() {
		if (color[4] == 1)
			return;
		parent.stroke(color[0], color[1], color[2], color[3]);
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
		if ((lifespan != -1 && color[3] < 0.0) || PApplet.abs(velocity.mag()) < MINVELOCITY) {
			return true;
		} else {
			return false;
		}
	}
}
