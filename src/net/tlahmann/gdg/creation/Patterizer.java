package net.tlahmann.gdg.creation;

import java.util.List;

import net.tlahmann.gdg.creation.object.PShape;

import java.util.ArrayList;
import java.util.Calendar;

import processing.core.PApplet;

public class Patterizer extends PApplet {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 720;

	List<PShape> stars;

	public Gui gui;

	public void setup() {
		gui = new Gui();
		PApplet.runSketch(new String[] { "Graphical User Interface" }, gui);

		frameRate(60);
		init();
	}

	private void init() {
		stars = new ArrayList<PShape>();
		boolean evodd = true;
		int l = 0;
		for (int j = (int) gui.originY.y; j < height + 100; j += gui.distanceY.y, l++) {
			float k = (evodd = !evodd) && gui.offset ? gui.distanceX.y / 2 : 0;
			for (int i = (int) gui.originX.y; i < width + 100; i += gui.distanceX.y) {
				stars.add(new PShape(this, 
						new float[] { i + k, j },
						gui.radius.y * ((gui.radiusChange.z - l * gui.radiusChange.y)/gui.radiusChange.z),
						gui.rotation.y, 
						gui.elements.y));
			}
			System.out.println((l * gui.radiusChange.y)/gui.radiusChange.z);
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

		for (PShape s : stars)
			s.display();
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
