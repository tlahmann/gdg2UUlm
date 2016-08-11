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
	long lastSpawn = 0;
	Voronoi myVoronoi;
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
		createVoronoi();
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(colors[0]);
		// Wenn mindestens 1 Punkt vorhanden ist wird gezeichnet
		if (particles.size() > 1) {

			// getRegions
			strokeWeight(2);
			stroke(random(10, 100), 150);
			MPolygon[] myRegions = myVoronoi.getRegions();
			for (int i = 0; i < myRegions.length; i++) {
				// an array of points
				myRegions[i].draw(this); // draw this shape
			}
		}

		removeDead();
		runParticles();
		createVoronoi();
	}

	public void mouseDragged() {
		if (lastSpawn + 10 > millis()) {
			return;
		}
		particles.add(new Particle(this, mouseX, mouseY, colors[1]));
		lastSpawn = millis();
	}

	void runParticles(){
		for (Particle p : particles) {
			// Gravity is scaled by mass here!
			// PVector gravity = new PVector(1f * p.MASS, 0);
			// Apply gravity
			// p.applyForce(gravity);

			// Update and display
			p.run();
		}	
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

	void createVoronoi() {
		float[][] points = new float[particles.size()][2];
		for (int i = 0; i < particles.size(); i++) {
			points[i][0] = particles.get(i).location.x;
			points[i][1] = particles.get(i).location.y;
		}
		myVoronoi = new Voronoi(points);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Painter.class.getName() });
	}
}
