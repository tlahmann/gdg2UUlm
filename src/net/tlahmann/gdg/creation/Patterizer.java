package net.tlahmann.gdg.creation;

import java.util.List;

import net.tlahmann.gdg.creation.gui.HScrollbar;
import net.tlahmann.gdg.creation.helper.Tuple;
import net.tlahmann.gdg.creation.object.PCircle;
import net.tlahmann.gdg.creation.object.PStar;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;

public class Patterizer extends PApplet {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 720;

	public PFont f14, f20;
	public Tuple<Integer, Integer> numberOfArms;
	public Tuple<Integer, Integer> thickness;
	float guiTimeout = 0;

	List<PStar> stars;
	List<PCircle> circles;

	public void setup() {
		// Create the font
		f20 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 20);
		f14 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 14);

		numberOfArms = new Tuple<Integer, Integer>(0, 12);
		thickness = new Tuple<Integer, Integer>(0, 12);

		// noLoop();
		frameRate(60);
		initGui();
		init();
	}

	HScrollbar hs1, hs2;

	private void init() {
		thickness.x = round(map(hs1.getPos(), 0, hs1.sposMax, 0, thickness.y - 1));
		hs1.text = thickness.x.toString();
		numberOfArms.x = round(map(hs2.getPos(), 0, hs2.sposMax, 0, numberOfArms.y - 1));
		hs2.text = numberOfArms.x.toString();

		stars = new ArrayList<PStar>();
		circles = new ArrayList<PCircle>();
		boolean evodd = true;
		for (int j = 2; j < height + 100; j += 74) {
			int k = (evodd = !evodd) ? 42 : 0;
			for (int i = 0; i < width + 100; i += 84) {
				stars.add(new PStar(this, new float[] { i + k, j }, 50, numberOfArms.x));
				// circles.add(new PCircle(this, new float[] { i + k, j }, 14 +
				// j * (5.0f / 80.0f)));
			}
		}
	}

	public void initGui() {
		hs1 = new HScrollbar(this, 20, 60, 200, 12, 1);
		hs2 = new HScrollbar(this, 20, 110, 200, 12, 1);
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
		
		textAlign(LEFT);

		textFont(f20);
		text("Thickness", 20, 44);
		hs1.update();
		hs1.display();
		if (thickness.x != round(map(hs1.getPos(), 0, hs1.sposMax, 0, thickness.y - 1))) {
			init();
		}

		textFont(f20);
		text("Arms", 20, 94);
		hs2.update();
		hs2.display();
		if (numberOfArms.x != round(map(hs2.getPos(), 0, hs2.sposMax, 0, numberOfArms.y - 1))) {
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
