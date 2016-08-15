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
	final float MAXTTL = 255;
	final float FRICTIONLOSS = 0.9f;

	float minVelocity;
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

		final float SIZEMULTIPLICATOR = 5;
		MASS = parent.random(MINMASS, MAXMASS) * SIZEMULTIPLICATOR;

		location = new PVector(loc[0] == 0 ? parent.random(0 + MASS * 2, parent.width - MASS * 2) : loc[0],
				loc[1] == 0 ? parent.random(0 + MASS * 2, parent.height - MASS * 2) : loc[1]);

		color = c == new float[] { 0 } ? new float[] { 0, 0, 0, 255, 0 } : c.clone();

		float orientation = PApplet.radians(parent.random(0, 360));
		velocity = new PVector((v == -1 ? INITVELOCITY : v) * PApplet.sin(orientation),
				(v == -1 ? INITVELOCITY : v) * PApplet.cos(orientation));

		lifespan = t == -1 ? -1 : t != 0 ? (MAXTTL / t) * parent.random(0.95f, 1.05f) : 1;

		acceleration = new PVector(0, 0);
		minVelocity = velocity.mag() / 5;
	}

	public void run() {
		update();
		display();
		checkCollision();
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
		parent.strokeWeight(MASS * 2);
		parent.point(location.x, location.y);
	}

	public void checkCollision() {
		for (Particle p : parent.particles) {
			if (p == this)
				continue;
			double dist = PVector.dist(this.location, p.location);
			if (dist < this.MASS + p.MASS) {
				float angle = PVector.angleBetween(this.velocity, p.velocity);
				float cosa = PApplet.cos(angle);
				float sina = PApplet.sin(angle);
				float px1 = cosa * this.velocity.x + sina * this.velocity.y;
				float py1 = cosa * this.velocity.y - sina * this.velocity.x;
				float px2 = cosa * p.velocity.x + sina * p.velocity.y;
				float py2 = cosa * p.velocity.y - sina * p.velocity.x;
				this.velocity = new PVector(px2, py2, 0).mult(FRICTIONLOSS);
				p.velocity = new PVector(px1, py1, 0).mult(FRICTIONLOSS);
			}

		}
	}

	// Bounce off edges of window
	public void checkEdges() {
		if (location.y >= parent.height - MASS && velocity.y > 0) {
			velocity.y *= -FRICTIONLOSS;
		} else if (location.y <= 0 + MASS && velocity.y < 0) {
			velocity.y *= -FRICTIONLOSS;
		} else if (location.x >= parent.width - MASS && velocity.x > 0) {
			velocity.x *= -FRICTIONLOSS;
		} else if (location.x <= 0 + MASS && velocity.x < 0) {
			velocity.x *= -FRICTIONLOSS;
		}
	}

	// Is the particle still useful?
	boolean isDead() {
		if ((lifespan != -1 && color[3] < 0.0) || PApplet.abs(velocity.mag()) < minVelocity) {
			return true;
		} else {
			return false;
		}
	}
}
