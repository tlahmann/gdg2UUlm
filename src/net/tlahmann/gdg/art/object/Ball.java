package net.tlahmann.gdg.art.object;

import net.tlahmann.gdg.art.Painter3D;
import processing.core.PApplet;
import processing.core.PVector;

public class Ball {
	Painter3D canvas;
	PVector location;
	PVector velocity;
	PVector acceleration;
	private final float MASS;
	float lifespan;

	Ball(Painter3D p, PVector l) {
		canvas = p;
		acceleration = new PVector(canvas.random(-0.05f, 0.05f), canvas.random(-0.05f, 0.05f),
				canvas.random(-0.05f, 0.05f));
		velocity = new PVector(canvas.random(-1, 1), canvas.random(-2, 1), canvas.random(-2, 1));
		location = l.copy();
		lifespan = 255.0f;
		MASS = canvas.random(0.2f, 2);
	}

	void run() {
		update();
		display();
	}

	// Method to update location
	void update() {
		velocity.add(acceleration);
		location.add(velocity);
		lifespan -= 5.0;
	}

	// Method to display
	void display() {
		canvas.stroke(255, lifespan);
		canvas.fill(255, lifespan);
		canvas.pushMatrix();
		canvas.translate(location.x, location.y, location.z);
		canvas.sphere(MASS);
		canvas.popMatrix();
		canvas.line(canvas.width / 2, canvas.height / 2, 0, location.x, location.y, location.z);
	}

	// Is the particle still useful?
	boolean isDead() {
		if (lifespan < 0.0) {
			return true;
		} else {
			return false;
		}
	}

	void gravity(Ball other) {
		// for (Ball p : canvas.particles) {
		// if (p == this)
		// continue;
		double dist = PVector.dist(this.location, other.location);
		if (dist > this.MASS + other.MASS) {
			final double G = 6.67408 * Math.pow(10, -11);
			double force = G * this.MASS * other.MASS / Math.pow(dist, 2);
			float angle = PVector.angleBetween(this.velocity, other.velocity);
			// this.acceleration.
			System.out.println("foo");
			// float cosa = PApplet.cos(angle);
			// float sina = PApplet.sin(angle);
			// float px1 = cosa * this.velocity.x + sina * this.velocity.y;
			// float py1 = cosa * this.velocity.y - sina * this.velocity.x;
			// float px2 = cosa * other.velocity.x + sina * other.velocity.y;
			// float py2 = cosa * other.velocity.y - sina * other.velocity.x;
			// this.velocity = new PVector(px2, py2, 0).mult(FRICTIONLOSS);
			// p.velocity = new PVector(px1, py1, 0).mult(FRICTIONLOSS);
		}

		// }
	}
}
