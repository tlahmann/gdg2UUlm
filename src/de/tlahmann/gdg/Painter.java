package de.tlahmann.gdg;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Painter extends PApplet {

	private final int WIDTH = 800;
	private final int HEIGHT = 800;
	
	ArrayList<Particle> particles;
	Region regions;
	long lastSpawn = 0;

	JSONArray file;
	int colorModel = 1;
	int backgroundColor;
	int[] particleColors;
	int fillRegion;

	public void setup() {
		file = loadJSONArray("./src/de/tlahmann/gdg/data/colors.json");

		JSONObject JColors = file.getJSONObject(colorModel);

		JSONObject colorBG = JColors.getJSONObject("background");
		JSONArray colorP = JColors.getJSONArray("particles");
		backgroundColor = color(colorBG.getInt("r"), colorBG.getInt("g"), colorBG.getInt("b"), colorBG.getInt("a"));
		fillRegion = JColors.getInt("fill");
		particleColors = new int[colorP.size()];
		for (int i = 0; i < colorP.size(); i++) {
			particleColors[i] = color(colorP.getJSONObject(i).getInt("r"), colorP.getJSONObject(i).getInt("g"),
					colorP.getJSONObject(i).getInt("b"), colorP.getJSONObject(i).getInt("a"));
		}

		init();
	}

	void init() {
		particles = new ArrayList<Particle>();
		for (int i = 0; i < 10; i++) {
			newParticle(0, 0);
		}
		regions = new Region(this);
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(backgroundColor);

		// run the Regions before the points to be drawn properly
		if (particles.size() > 1) {
			runRegions();
		}

		removeDead();
		runParticles();
	}

	public void mouseDragged() {
		if (lastSpawn + 10 > millis()) {
			return;
		}
		newParticle(mouseX, mouseY);
		lastSpawn = millis();
	}

	public void mouseClicked() {
		newParticle(mouseX, mouseY);
	}

	public void keyPressed() {
		if (key == 'r' || key == 'R') {
			init();
		}
	}

	void newParticle(int x, int y) {
		particles.add(new Particle(this, x, y, particleColors[(int) random(0, particleColors.length)], fillRegion));
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
