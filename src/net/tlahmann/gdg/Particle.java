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
	final float MINMASS = 2f;
	final float MAXMASS = 20f;
	final float MAXTTL = 255;

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
			float dist = PVector.dist(this.location, p.location);
			if (dist < this.MASS + p.MASS) {
				float newVelX1 = (this.velocity.x * (this.MASS - p.MASS) + (2 * p.MASS * p.velocity.x))
						/ (this.MASS + p.MASS);
				float newVelY1 = (this.velocity.y * (this.MASS - p.MASS) + (2 * p.MASS * p.velocity.y))
						/ (this.MASS + p.MASS);
				float newVelX2 = (p.velocity.x * (p.MASS - this.MASS) + (2 * this.MASS * this.velocity.x))
						/ (this.MASS + p.MASS);
				float newVelY2 = (p.velocity.y * (p.MASS - this.MASS) + (2 * this.MASS * this.velocity.y))
						/ (this.MASS + p.MASS);
				this.velocity.mult(0);
				this.velocity.add(new PVector(newVelX1, newVelY1));
				p.velocity.mult(0);
				p.velocity.add(new PVector(newVelX2, newVelY2));
			}
			if (dist < (this.MASS + p.MASS) * 0.90f) {
//				unstuck(p.location, dist - (this.MASS + p.MASS));
			}
		}
	}

	// Bounce off edges of window
	public void checkEdges() {
		if (location.y >= parent.height - MASS && velocity.y > 0) {
			velocity.y *= -0.9f;
		} else if (location.y <= 0 + MASS && velocity.y < 0) {
			velocity.y *= -0.9f;
		} else if (location.x >= parent.width - MASS && velocity.x > 0) {
			velocity.x *= -0.9f;
		} else if (location.x <= 0 + MASS && velocity.x < 0) {
			velocity.x *= -0.9f;
		}
	}

	void unstuck(PVector o, float v) {
//		new PVector(-o.y,o.x,o.z).normalize().mult(v);
//		float angle = PVector.angleBetween(this.velocity, p.velocity);
//		float cosa = PApplet.cos(angle);
//		float sina = PApplet.sin(angle);
//		float px1 = cosa * this.velocity.x + sina * this.velocity.y;
		// cosa * this.velocity.x + sina * this.velocity.y;
		this.location.add(new PVector(-o.y,o.x,o.z).normalize().mult(v));
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
