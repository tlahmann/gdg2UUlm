package de.tlahmann.gdg;

import java.util.ArrayList;

import megamu.mesh.MPolygon;
import megamu.mesh.Voronoi;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Painter extends PApplet {

	private final int WIDTH = 800;
	private final int HEIGHT = 800;
	// Arraylist and Array um die Punkte zu speichen
	ArrayList<Particle> particles;
	Region regions;
	long lastSpawn = 0;

	JSONArray values;
	int cModel = 0;
	int colorModel = 0;
	int[] colors;

	public void setup() {
		values = loadJSONArray("./src/de/tlahmann/gdg/data/colors.json");
		colors = new int[2];
		JSONObject JColors = values.getJSONObject(colorModel);

		JSONObject colorBG = JColors.getJSONObject("background");
		JSONObject colorP = JColors.getJSONObject("particles");
		colors[0] = color(colorBG.getInt("r"), colorBG.getInt("g"), colorBG.getInt("b"), colorBG.getInt("a"));
		colors[1] = color(colorP.getInt("r"), colorP.getInt("g"), colorP.getInt("b"), colorP.getInt("a"));

		particles = new ArrayList<Particle>();
		for (int i = 0; i < 10; i++) {
			particles.add(new Particle(this));
		}
		regions = new Region(this);
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(colors[0]);

		// run the Regions before the points to be drawn properly
		if (particles.size() > 1) {
			runRegions();
		}
		
		removeDead();
		runParticles();
		// createVoronoi();
	}

	public void mouseDragged() {
		if (lastSpawn + 10 > millis()) {
			return;
		}
		particles.add(new Particle(this, mouseX, mouseY, colors[1]));
		lastSpawn = millis();
	}

	void removeDead() {
		// Cycle through the ArrayList backwards, because we are deleting while
		// iterating
		for (int i = particles.size() - 1; i >= 0; i--) {
			if (particles.get(i).isDead()) {
				particles.remove(i);
			}
		}
	}

	void runParticles() {
		for (Particle p : particles) {
			// Gravity is scaled by mass here!
			// PVector gravity = new PVector(1f * p.MASS, 0);
			// Apply gravity
			// p.applyForce(gravity);

			// Update and display
			p.run();
		}
	}

	void runRegions() {
		regions.run(particles);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Painter.class.getName() });
	}
}
