package de.tlahmann.gdg;

import java.util.ArrayList;

import megamu.mesh.MPolygon;
import megamu.mesh.Voronoi;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Painter extends PApplet {

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
			particles.add(new Particle(this));
		}
		createVoronoi();
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(colors[0][0], colors[0][1], colors[0][2], colors[0][3]);

		// particles.add(new Particle());

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

		// draw Points
		for (Particle p : particles) {
			// Gravity is scaled by mass here!
			PVector gravity = new PVector(1f * p.MASS, 0);
			// Apply gravity
			p.applyForce(gravity);

			// Update and display
			p.run();
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
		PApplet.main(new String[] { Painter.class.getName() });
	}
}
