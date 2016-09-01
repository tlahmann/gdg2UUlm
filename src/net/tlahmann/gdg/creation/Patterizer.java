package net.tlahmann.gdg.creation;

import java.util.List;

import controlP5.ColorPicker;
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

	public PFont f08, f20;
	float length, rotation;
	public Tuple<Float, Float> offset;
	public Tuple<Integer, Integer> numberOfArms;
	public Tuple<Integer, Integer> thickness;
	public Tuple<Integer, Integer> colors;
	ControlP5 cp5;
	float guiTimeout = 0;

	List<PStar> stars;
	List<PCircle> circles;

	public void setup() {
		// Create font
		f20 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 20);
		f08 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 8);

		length = 50.0f;
		rotation = 0.0f;
		offset = new Tuple<Float, Float>(74.0f, 84.0f);
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
		for (int j = 2; j < height + 100; j += offset.x) {
			int k = (evodd = !evodd) ? 42 : 0;
			for (int i = 0; i < width + 100; i += offset.y) {
				stars.add(new PStar(this, new float[] { i + k, j }, length, rotation, numberOfArms.x));
				circles.add(new PCircle(this, new float[] { i + k, j }, 14 + j * (5.0f / 80.0f)));
			}
		}
	}

	public void initGui() {
		cp5 = new ControlP5(this);
		ControlFont font = new ControlFont(f08);

		int guiElementHeight = 15;
		int xpos = 110;

		// horizontal sliders for x offset
		cp5.addSlider("arm length").setPosition(20, xpos).setRange(10, 500).setValue(length)
				.setSize(200, guiElementHeight).setColorBackground(color(50, 50, 50, 255))
				.setColorForeground(color(255, 255, 255, 255)).setColorActive(color(230, 230, 230, 255))
				.setColorValueLabel(color(128, 128, 128, 255)).setFont(font).setId(0);

		// horizontal sliders for x offset
		cp5.addSlider("rotation").setPosition(20, xpos += 2 * guiElementHeight).setRange(0, PI).setValue(offset.x)
				.setSize(200, guiElementHeight).setColorBackground(color(50, 50, 50, 255))
				.setColorForeground(color(255, 255, 255, 255)).setColorActive(color(230, 230, 230, 255))
				.setColorValueLabel(color(128, 128, 128, 255)).setFont(font).setId(1);

		// horizontal sliders for x offset
		cp5.addSlider("x offset").setPosition(20, xpos += 2 * guiElementHeight).setRange(1, width).setValue(offset.x)
				.setSize(200, guiElementHeight).setColorBackground(color(50, 50, 50, 255))
				.setColorForeground(color(255, 255, 255, 255)).setColorActive(color(230, 230, 230, 255))
				.setColorValueLabel(color(128, 128, 128, 255)).setFont(font).setId(2);

		// horizontal sliders for y offset
		cp5.addSlider("y offset").setPosition(20, xpos += 2 * guiElementHeight).setRange(1, height).setValue(offset.y)
				.setSize(200, guiElementHeight).setColorBackground(color(50, 50, 50, 255))
				.setColorForeground(color(255, 255, 255, 255)).setColorActive(color(230, 230, 230, 255))
				.setColorValueLabel(color(128, 128, 128, 255)).setFont(font).setId(3);

		// horizontal sliders for number of arms
		// don't forget to set the ID!
		cp5.addSlider("Tickness").setPosition(20, xpos += 2 * guiElementHeight).setRange(1, thickness.y)
				.setValue(thickness.x).setSize(200, guiElementHeight).setNumberOfTickMarks(thickness.y)
				.setColorBackground(color(50, 50, 50, 255)).setColorForeground(color(255, 255, 255, 255))
				.setColorActive(color(230, 230, 230, 255)).setColorValueLabel(color(128, 128, 128, 255)).setFont(font)
				.setId(4);

		// horizontal sliders for number of arms
		cp5.addSlider("Number of Arms").setPosition(20, xpos += 2 * guiElementHeight).setRange(1, numberOfArms.y)
				.setValue(numberOfArms.x).setSize(200, guiElementHeight).setNumberOfTickMarks(numberOfArms.y)
				.setColorBackground(color(50, 50, 50, 255)).setColorForeground(color(255, 255, 255, 255))
				.setColorActive(color(230, 230, 230, 255)).setColorValueLabel(color(128, 128, 128, 255)).setFont(font)
				.setId(5);

		cp5.addButton("reset").setValue(0).setPosition(20, 470).setSize(100, guiElementHeight).setFont(font).setId(8);
		cp5.addButton("save screenshot").setValue(0).setPosition(140, 470).setSize(100, guiElementHeight).setFont(font)
				.setId(9);

	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void draw() {
		background(colors.y);

		for (PStar s : stars)
			s.display();
		for (PCircle c : circles)
			c.run();
		if (mouseX > 0 && mouseX < 300 && mouseY > 0 && mouseY < 600) {
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
		rect(0, 0, 300, 600);

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
			length = theEvent.getController().getValue();
			init();
			break;
		case (1):
			rotation = theEvent.getController().getValue();
			init();
			break;
		case (2):
			offset.x = theEvent.getController().getValue();
			init();
			break;
		case (3):
			offset.y = theEvent.getController().getValue();
			init();
			break;
		case (4):
			thickness.x = (int) theEvent.getController().getValue();
			init();
			break;
		case (5):
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

	void screenshot() {
		saveFrame("line-######.png");
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { Patterizer.class.getName() });
	}

}
