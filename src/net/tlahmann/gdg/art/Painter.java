package net.tlahmann.gdg.art;

import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import net.tlahmann.gdg.art.object.Particle;
import net.tlahmann.gdg.art.object.Region;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Painter extends PApplet {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

	public ArrayList<Particle> particles;
	public ArrayList<Particle> deadParticles;
	Region regions;
	long lastSpawn = 0;

	int cx, cy;
	float secondsRadius;

	static JSONArray file;
	static int colorModel = 0;
	public int backgroundColor;
	static float[][] particleColors;
	static int fillRegion;

	static AudioPlayer player;

	public void setup() {
		importColors();

		player = new Minim(this).loadFile("./src/net/tlahmann/gdg/data/song.mp3");
		// player.play();

		frameRate(60);

		init();
	}

	void importColors() {
		file = loadJSONArray("./src/net/tlahmann/gdg/data/colorBrewer.json");

		JSONObject JColors = file.getJSONObject(colorModel);

		println(JColors.getString("name"));
		String colorBG = JColors.getString("background");
		JSONArray colorPs = JColors.getJSONArray("particles");
		backgroundColor = unhex(colorBG);
		particleColors = new float[colorPs.size()][5];
		for (int i = 0; i < colorPs.size(); i++) {
			int colorP = unhex(colorPs.getString(i));
			particleColors[i][0] = colorP >> 16 & 0xFF;
			particleColors[i][1] = colorP >> 8 & 0xFF;
			particleColors[i][2] = colorP & 0xFF;
			particleColors[i][3] = 255;
			particleColors[i][4] = JColors.getInt("fill");
		}
	}

	void init() {
		particles = new ArrayList<Particle>();
		deadParticles = new ArrayList<Particle>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
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
		if (lastSpawn + 500 < millis()) {
			for (int i = 0; i < 50; i++) {
				float x = random(0, WIDTH);
				float y = random(0, HEIGHT);

				newParticle(new float[] { x, y }, 0.2f, 30);
			}
			lastSpawn = millis();
		}
		float s = map(millis(), 0, 1000, 0, TWO_PI) - HALF_PI;
		stroke(0);
		strokeWeight(1);
		newParticle(new float[] { cx + cos(s) * secondsRadius, cy + sin(s) * secondsRadius }, 0.1f, 200);
	}

	public void mouseDragged() {
		if (lastSpawn + 10 > millis()) {
			return;
		}
		newParticle(new float[] { mouseX, mouseY }, 0.2f, 300);
		lastSpawn = millis();
	}

	public void mouseClicked() {
		newParticle(new float[] { mouseX, mouseY }, 0.2f, 300);
	}

	public void keyPressed() {
		if (key == 'r' || key == 'R') {
			init();
		} else if (key == 'c' || key == 'C') {
			colorModel++;
			colorModel = (colorModel % 35 + 35) % 35;
			importColors();
			for (Particle p : particles) {
				// Update and display
				int i = (int) random(0, particleColors.length);
				p.color = particleColors[i];
			}
		} else if (key == 'x' || key == 'X') {
			colorModel--;
			colorModel = (colorModel % 35 + 35) % 35;
			importColors();
			for (Particle p : particles) {
				// Update and display
				int i = (int) random(0, particleColors.length);
				p.color = particleColors[i];
			}
		}
	}

	void newParticle(float[] l, float v, int t) {
		int i = (int) random(0, particleColors.length);
		particles.add(new Particle(this, l, particleColors[i], v, t));
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

	public int calculateColor(float x, float y) {
		float dist = dist(x, y, 0, 0);
		return lerpColor((int) color(particleColors[0][0], particleColors[0][1], particleColors[0][2]),
				(int) color(particleColors[particleColors.length - 1][0], particleColors[particleColors.length - 1][1],
						particleColors[particleColors.length - 1][2]),
				dist / sqrt(sq(WIDTH) + sq(HEIGHT)));
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Painter.class.getName() });
	}
}
