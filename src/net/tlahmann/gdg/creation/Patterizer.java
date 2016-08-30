package net.tlahmann.gdg.creation;

import java.util.List;

import net.tlahmann.gdg.creation.gui.HScrollbar;
import net.tlahmann.gdg.creation.object.PCircle;
import net.tlahmann.gdg.creation.object.PStar;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;

public class Patterizer extends PApplet {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 720;

	public PFont f14, f24;
	private int maxNumberOfArms;
	public int numberOfArms;
	float guiTimeout = 0;

	List<PStar> stars;
	List<PCircle> circles;

	public void setup() {
		// Create the font
		f24 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 24);
		f14 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 14);

		maxNumberOfArms = 12;

		// noLoop();
		frameRate(60);
		initGui();
		init();
	}

	HScrollbar hs1;

	private void init() {
		numberOfArms = round(map(hs1.getPos(), 0, hs1.sposMax, 0, maxNumberOfArms - 1));

		stars = new ArrayList<PStar>();
		circles = new ArrayList<PCircle>();
		boolean evodd = true;
		for (int j = 2; j < height + 100; j += 74) {
			int k = (evodd = !evodd) ? 42 : 0;
			for (int i = 0; i < width + 100; i += 84) {
				stars.add(new PStar(this, new float[] { i + k, j }, 50, numberOfArms));
				// circles.add(new PCircle(this, new float[] { i + k, j }, 14 +
				// j * (5.0f / 80.0f)));
			}
		}
	}

	public void initGui() {
		hs1 = new HScrollbar(this, 35, 60, 200, 12, 1);
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(0);
		stroke(0);
		for (PStar s : stars)
			s.display();
		// for (PCircle c : circles)
		// c.run();
		if (mouseX > 0 && mouseX < 300 && mouseY > 0 && mouseY < 400) {
			drawGui();
			guiTimeout = 60;
		} else {
			if (guiTimeout > 0) {
				drawGui();
				guiTimeout--;
			}
		}
	}

	void drawGui() {
		// gui containers
		strokeWeight(3);
		stroke(255);
		fill(0);
		rect(0, 0, 300, 400);

		// gui elements
		strokeWeight(1);
		fill(255);
		textFont(f24);

		textAlign(LEFT);
		text("Arms", 20, 44);
		rect(20, 60 - 12 / 2, 12, 12);
		rect(203, 60 - 12 / 2, 12, 12);

		hs1.update();
		hs1.display();
		if (numberOfArms != round(map(hs1.getPos(), 0, hs1.sposMax, 0, maxNumberOfArms - 1))) {
			init();
		}
	}

	public void mouseDragged() {

	}

	public void mousePressed() {
		// loop();
	}

	public void mouseReleased() {
		// noLoop();
	}

	public void keyPressed() {
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Patterizer.class.getName() });
	}

}
