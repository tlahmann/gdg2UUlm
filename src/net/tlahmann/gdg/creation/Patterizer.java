package net.tlahmann.gdg.creation;

import java.util.List;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Slider;
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
	Slider[] sliders;
	float guiTimeout = 0;

	List<PStar> stars;
	List<PCircle> circles;

	public void setup() {
		// Create font
		f20 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 20);
		f14 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 14);

		numberOfArms = new Tuple<Integer, Integer>(6, 12);
		thickness = new Tuple<Integer, Integer>(2, 12);

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
		sliders = new Slider[2];

		// horizontal sliders for number of arms
		// don't forget to set the ID!
		sliders[0] = cp5.addSlider("thickness").setLabel("Tickness").setPosition(20, 110).setRange(1, thickness.y)
				.setValue(thickness.x).setSize(200, 10).setNumberOfTickMarks(thickness.y)
				.setColorBackground(color(50, 50, 50, 255)).setColorForeground(color(255, 255, 255, 255))
				.setColorActive(color(230, 230, 230, 255)).setColorValueLabel(color(128, 128, 128, 255)).setId(0);

		// horizontal sliders for number of arms
		sliders[1] = cp5.addSlider("arms").setLabel("Number of Arms").setPosition(20, 150).setRange(1, numberOfArms.y)
				.setValue(numberOfArms.x).setSize(200, 10).setNumberOfTickMarks(numberOfArms.y)
				.setColorBackground(color(50, 50, 50, 255)).setColorForeground(color(255, 255, 255, 255))
				.setColorActive(color(230, 230, 230, 255)).setColorValueLabel(color(128, 128, 128, 255)).setId(1);
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(0);

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
