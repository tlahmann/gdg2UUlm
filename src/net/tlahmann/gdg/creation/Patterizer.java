package net.tlahmann.gdg.creation;

import java.util.List;

import net.tlahmann.gdg.creation.object.PCircle;
import net.tlahmann.gdg.creation.object.PStar;

import java.util.ArrayList;

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
		for (int j = 2; j < height + 100; j += gui.distanceX.y) {
			int k = (int) ((evodd = !evodd) ? gui.distanceY.y/2 : 0);
			for (int i = 0; i < width + 100; i += gui.distanceY.y) {
				stars.add(new PStar(this, new float[] { i + k, j }, gui.radius.y, gui.rotation.y, gui.elements.y));
//				circles.add(new PCircle(this, new float[] { i + k, j }, 14 + j * (5.0f / 80.0f)));
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

		background(gui.colors[4]);

		for (PStar s : stars)
			s.display();
		for (PCircle c : circles)
			c.run();
	}

	void screenshot() {
		saveFrame("line-######.png");
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Patterizer.class.getName() });
	}

}
