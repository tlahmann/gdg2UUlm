package net.tlahmann.gdg.art;

import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Painter extends PApplet {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

	ArrayList<Particle> particles;
	ArrayList<Particle> deadParticles;
	Region regions;
	long lastSpawn = 0;

	int cx, cy;
	float secondsRadius;

	static JSONArray file;
	static int colorModel = 0;
	int backgroundColor;
	static float[][] particleColors;
	static int fillRegion;

	static AudioPlayer player;

	public void setup() {
		file = loadJSONArray("./src/net/tlahmann/gdg/data/colors.json");

		JSONObject JColors = file.getJSONObject(colorModel);

		JSONObject colorBG = JColors.getJSONObject("background");
		JSONArray colorP = JColors.getJSONArray("particles");
		backgroundColor = color(colorBG.getInt("r"), colorBG.getInt("g"), colorBG.getInt("b"), colorBG.getInt("a"));
		particleColors = new float[colorP.size()][5];
		for (int i = 0; i < colorP.size(); i++) {
			particleColors[i][0] = colorP.getJSONObject(i).getInt("r");
			particleColors[i][1] = colorP.getJSONObject(i).getInt("g");
			particleColors[i][2] = colorP.getJSONObject(i).getInt("b");
			particleColors[i][3] = colorP.getJSONObject(i).getInt("a");
			particleColors[i][4] = JColors.getInt("fill");
		}

		player = new Minim(this).loadFile("./src/net/tlahmann/gdg/data/song.mp3");
		// player.play();

		init();
	}

	void init() {
		particles = new ArrayList<Particle>();
		deadParticles = new ArrayList<Particle>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println(i * width / 2 + " :: "+ j * height / 2 + "");
				newParticle(new float[] { i * width / 2, j * height / 2 }, 0.0f, -1);
			}
		}

		regions = new Region(this);

		int radius = min(width, height) / 2;
		secondsRadius = radius * 0.72f;
		cx = width / 2;
		cy = height / 2;
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
		// spawner();
	}

	void spawner() {
		if (lastSpawn + 5000 < millis()) {
			float x = random(0, WIDTH);
			float y = random(0, HEIGHT);
			newParticle(new float[] { x, y });
			lastSpawn = millis();
		}
		float s = map(millis(), 0, 1000, 0, TWO_PI) - HALF_PI;
		stroke(0);
		strokeWeight(1);
		newParticle(new float[] { cx + cos(s) * secondsRadius, cy + sin(s) * secondsRadius });
	}

	public void mouseDragged() {
		if (lastSpawn + 10 > millis()) {
			return;
		}
		newParticle(new float[] { mouseX, mouseY });
		lastSpawn = millis();
	}

	public void mouseClicked() {
		newParticle(new float[] { mouseX, mouseY });
	}

	public void keyPressed() {
		if (key == 'r' || key == 'R') {
			init();
		}
	}

	void newParticle(float[] l) {
		int i = (int) random(0, particleColors.length);
		particles.add(new Particle(this, l, particleColors[i], 0.5f, 300));
	}

	void removeDead() {
		particles.removeAll(deadParticles);
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
