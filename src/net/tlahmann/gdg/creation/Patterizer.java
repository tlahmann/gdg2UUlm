package net.tlahmann.gdg.creation;

import java.util.List;

import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import net.tlahmann.gdg.creation.helper.Tuple;
import net.tlahmann.gdg.creation.object.PCircle;
import net.tlahmann.gdg.creation.object.PStar;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;

public class Patterizer extends PApplet {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 720;

	List<PStar> stars;
	List<PCircle> circles;

	public Gui gui;

	public void setup() {
		gui = new Gui();
		PApplet.runSketch(new String[] {"Graphical User Interface"}, gui);

		frameRate(60);
		init();
	}

	private void init() {
		stars = new ArrayList<PStar>();
		circles = new ArrayList<PCircle>();
		boolean evodd = true;
		for (int j = 2; j < height + 100; j += gui.offset.x) {
			int k = (evodd = !evodd) ? 42 : 0;
			for (int i = 0; i < width + 100; i += gui.offset.y) {
				stars.add(new PStar(this, new float[] { i + k, j }, gui.length, gui.rotation, gui.numberOfArms.x));
				circles.add(new PCircle(this, new float[] { i + k, j }, 14 + j * (5.0f / 80.0f)));
			}
		}
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(gui.colors.y);

		for (PStar s : stars)
			s.display();
		for (PCircle c : circles)
			c.run();
		// if (mouseX > 0 && mouseX < 300 && mouseY > 0 && mouseY < 600) {
		// drawGui();
		// gui.guiTimeout = 60;
		// } else {
		// if (gui.guiTimeout > 0) {
		// drawGui();
		// gui.guiTimeout--;
		// } else {
		// cp5.hide();
		// }
		// }
	}

	void drawGui() {

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

	void screenshot() {
		saveFrame("line-######.png");
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Patterizer.class.getName() });
	}

}
