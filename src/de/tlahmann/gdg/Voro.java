package de.tlahmann.gdg;

import java.util.ArrayList;

import megamu.mesh.MPolygon;
import megamu.mesh.Voronoi;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Voro extends PApplet {

	private final int WIDTH = 800;
	private final int HEIGHT = 800;
	// Arraylist and Array um die Punkte zu speichen
	ArrayList<Particle> particles;
	Voronoi myVoronoi;
	JSONArray values;
	int[][] colors;

	public void setup() {
		colors = new int[2][4];
		values = loadJSONArray("./src/de/tlahmann/gdg/data/colors.json");

		for (int i = 0; i < values.size(); i++) {

			JSONObject colorModel = values.getJSONObject(i);

			int id = colorModel.getInt("id");
			JSONObject colorBG = colorModel.getJSONObject("background");
			JSONObject colorP = colorModel.getJSONObject("particles");
			colors[0][0] = colorBG.getInt("r");
			colors[0][1] = colorBG.getInt("g");
			colors[0][2] = colorBG.getInt("b");
			colors[0][3] = colorBG.getInt("a");
			
			colors[1][0] = colorP.getInt("r");
			colors[1][1] = colorP.getInt("g");
			colors[1][2] = colorP.getInt("b");
			colors[1][3] = colorP.getInt("a");
		}

		particles = new ArrayList<Particle>();
		for (int i = 0; i < 2; i++) {
			particles.add(new Particle());
		}
		createVoronoi();
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(colors[0][0], colors[0][1], colors[0][2], colors[0][3]);

//		particles.add(new Particle());
		
		// Wenn mindestens 1 Punkt vorhanden ist wird gezeichnet
		if (particles.size() > 1) {

			// getRegions
			strokeWeight(2);
			stroke(random(10,100), 150);
			MPolygon[] myRegions = myVoronoi.getRegions();
			for (int i = 0; i < myRegions.length; i++) {
				// an array of points
				myRegions[i].draw(this); // draw this shape
			}
		}

		// draw Points
		for (Particle p : particles) {
			// Gravity is scaled by mass here!
			PVector gravity = new PVector(0, 1f * p.mass);
			// Apply gravity
			p.applyForce(gravity);

			// Update and display
			p.update();
			p.display();
			p.checkEdges();
		}
		calculateParticles();
		createVoronoi();
	}

	void calculateParticles() {
		for (int i = particles.size() - 1; i >= 0; i--) {
			if (particles.get(i).isDead()) {
				particles.remove(i);
			}
		}
	}

	void createVoronoi() {
		float[][] points = new float[particles.size()][2];
		for (int i = 0; i < particles.size(); i++) {
			points[i][0] = particles.get(i).location.x;
			points[i][1] = particles.get(i).location.y;
		}
		myVoronoi = new Voronoi(points);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Voro.class.getName() });
	}

	public class Particle {
		// location, velocity, and acceleration
		public PVector location;
		PVector velocity;
		PVector acceleration;
		PVector orientation;
		public float lifespan;

		// Mass is tied to size
		public float mass;

		final float MINIMALMASS = 0.5f;
		final float MAXIMALMASS = 3;
		final float MINIMALTTL = 10;
		final float MAXIMALTTL = 255;
		
		final float MINIMALVELOCITY = 0.5f;
		
		
		public Particle() {
			mass = random(MINIMALMASS, MAXIMALMASS);
			location = new PVector(random(0, width), random(0,height));
			velocity = new PVector(0, 0);
			acceleration = new PVector(0, 0);
			orientation = new PVector(random(0, 360), 0);
			lifespan = random(MINIMALTTL, MAXIMALTTL);
		}
		
		public Particle(float m, float x, float y, float a, float t) {
			mass = m;
			location = new PVector(x, y);
			velocity = new PVector(0, 0);
			acceleration = new PVector(0, 0);
			orientation = new PVector(random(0, 360), 0);
			lifespan = a;
		}

		// Newton's 2nd law: F = M * A
		// or A = F / M
		public void applyForce(PVector force) {
			// Divide by mass
			PVector f = PVector.div(force, mass);
			// Accumulate all forces in acceleration
			acceleration.add(f);
		}

		public void update() {
			// Velocity changes according to acceleration
			velocity.add(acceleration);
			// Location changes by velocity
			location.add(velocity);
			// We must clear acceleration each frame
			acceleration.mult(0);
			lifespan--;
		}

		// Draw Mover
		public void display() {
			stroke(colors[1][0], colors[1][1], colors[1][2], lifespan);
			strokeWeight(mass * 5);
			point(location.x, location.y);
		}

		// Bounce off edges of window
		public void checkEdges() {
			if (location.y > height || location.y < 0) {
				velocity.y *= -0.9; // A little dampening when hitting the
									// bottom
			} else if (location.x > width || location.x < 0) {
				velocity.x *= -0.9; // A little dampening when hitting the
									// bottom
			}
		}
		
		// Is the particle still useful?
		  boolean isDead() {
		    if (lifespan < 0.0 || abs(velocity.y) < MINIMALVELOCITY) {
		      return true;
		    } else {
		      return false;
		    }
		  }
	}
}
