package net.tlahmann.gdg.creation;

import java.util.List;

import net.tlahmann.gdg.creation.object.PCircle;
import net.tlahmann.gdg.creation.object.PStar;

import java.util.ArrayList;
import java.util.Calendar;

import processing.core.PApplet;

public class Patterizer extends PApplet {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 720;

	List<PStar> stars;
	List<PCircle> circles;

	public Gui gui;

	public void setup() {
		gui = new Gui();
		PApplet.runSketch(new String[] { "Graphical User Interface" }, gui);

		frameRate(60);
		init();
	}

	private void init() {
		stars = new ArrayList<PStar>();
		circles = new ArrayList<PCircle>();
		boolean evodd = true;
		for (int j = (int) gui.originY.y; j < height + 100; j += gui.distanceY.y) {
			float k = (evodd = !evodd) ? gui.offset : 0;
			for (int i = (int) gui.originX.y; i < width + 100; i += gui.distanceX.y) {
				stars.add(new PStar(this, new float[] { i + k, j }, gui.radius.y, gui.rotation.y, gui.elements.y));
				// circles.add(new PCircle(this, new float[] { i + k, j }, 14 +
				// j * (5.0f / 80.0f)));
			}
		}
		gui.changes = false;
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		if (gui.changes) {
			init();
		}
		if (gui.screenshot) {
			screenshot();
		}

		background(gui.colors[4]);

		for (PStar s : stars)
			s.display();
		for (PCircle c : circles)
			c.run();
	}

	void screenshot() {
		saveFrame("Patterizer-" + timestamp() + ".png");
		gui.screenshot = false;
	}

	String timestamp() {
		Calendar now = Calendar.getInstance();
		return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Patterizer.class.getName() });
	}

}
