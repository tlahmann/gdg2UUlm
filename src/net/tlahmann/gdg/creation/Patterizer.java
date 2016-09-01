package net.tlahmann.gdg.creation;

import java.util.List;

import controlP5.ControlEvent;
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

	public PFont f14, f20;
	public Tuple<Integer, Integer> numberOfArms;
	public Tuple<Integer, Integer> thickness;
	ControlP5 cp5;
	public Tuple<Integer, Integer> colors;
	float guiTimeout = 0;

	List<PStar> stars;
	List<PCircle> circles;

	public void setup() {
		// Create font
		f20 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 20);
		f14 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 14);

		numberOfArms = new Tuple<Integer, Integer>(6, 12);
		thickness = new Tuple<Integer, Integer>(2, 12);
		colors = new Tuple<Integer, Integer>(color(255, 255, 255, 255), color(0, 0, 0, 255));

		// noLoop();
		frameRate(60);
		initGui();
		init();
	}

	private void init() {
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
		cp5 = new ControlP5(this);

		// horizontal sliders for number of arms
		// don't forget to set the ID!
		cp5.addSlider("Tickness").setPosition(20, 110).setRange(1, thickness.y).setValue(thickness.x).setSize(200, 15)
				.setNumberOfTickMarks(thickness.y).setColorBackground(color(50, 50, 50, 255))
				.setColorForeground(color(255, 255, 255, 255)).setColorActive(color(230, 230, 230, 255))
				.setColorValueLabel(color(128, 128, 128, 255)).setId(0);

		// horizontal sliders for number of arms
		cp5.addSlider("Number of Arms").setPosition(20, 150).setRange(1, numberOfArms.y).setValue(numberOfArms.x)
				.setSize(200, 15).setNumberOfTickMarks(numberOfArms.y).setColorBackground(color(50, 50, 50, 255))
				.setColorForeground(color(255, 255, 255, 255)).setColorActive(color(230, 230, 230, 255))
				.setColorValueLabel(color(128, 128, 128, 255)).setId(1);

		// create a toggle and change the default look to a (on/off) switch look
		// cp5.addToggle("B/W <-> W/B").setPosition(20, 190).setSize(30,
		// 15).setValue(true).setMode(ControlP5.SWITCH)
		// .setColorBackground(color(50, 50, 50,
		// 255)).setColorForeground(color(255, 255, 255, 255))
		// .setColorActive(color(230, 230, 230,
		// 255)).setColorValueLabel(color(128, 128, 128, 255)).setId(2);

		cp5.addColorPicker("foreground").setPosition(20, 240).setWidth(100).setSize(100, 19).setColorValue(colors.x)
				.setId(2);

		cp5.addColorPicker("background").setPosition(20, 300).setWidth(100).setSize(100, 19).setColorValue(colors.x)
				.setId(3);

	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(colors.y);

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
			} else {
				cp5.hide();
			}
		}
	}

	void drawGui() {
		// gui container
		strokeWeight(3);
		stroke(255);
		fill(0);
		rect(0, 0, 300, 400);

		// gui elements
		strokeWeight(1);
		fill(255);
		textAlign(LEFT);

		textFont(f20);
		text("Objects", 20, 44);
		textFont(f20);
		text("Appearance", 20, 94);

		// draw control p5 items
		cp5.show();
	}

	// function controlEvent will be invoked with every value change
	// in any registered controller
	public void controlEvent(ControlEvent theEvent) {
		switch (theEvent.getId()) {
		case (0):
			thickness.x = (int) theEvent.getController().getValue();
			// init();
			break;
		case (1):
			numberOfArms.x = (int) theEvent.getController().getValue();
			init();
			break;
		case (2):
			colors.x = color(theEvent.getArrayValue(0), theEvent.getArrayValue(1), theEvent.getArrayValue(2),
					theEvent.getArrayValue(3));
			break;
		case (3):
			colors.y = color(theEvent.getArrayValue(0), theEvent.getArrayValue(1), theEvent.getArrayValue(2),
					theEvent.getArrayValue(3));
			break;
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
