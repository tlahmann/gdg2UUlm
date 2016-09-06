package net.tlahmann.gdg.creation;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.Slider;
import controlP5.Slider2D;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Gui extends PApplet {

	public PVector radius = new PVector(5, 50, 1000);
	public PVector rotation = new PVector(0, 0, 360);
	public PVector originX = new PVector(0, 0, 1024);
	public PVector originY = new PVector(0, 0, 720);
	public PVector distanceX = new PVector(10, 100, 1024);
	public PVector distanceY = new PVector(10, 100, 720);
	public boolean offset = false;

	public PVector thickness = new PVector(1, 1, 50);
	public PVector elements = new PVector(1, 4, 50);

	public boolean outline = false;

	public int[] colors = new int[] { color(255, 255, 255, 255), color(230, 230, 230, 255), color(128, 128, 128, 255),
			color(50, 50, 50, 255), color(0, 0, 0, 255) };

	public boolean changes = false;
	public boolean screenshot = false;

	private PFont f20;
	ControlFont f08;
	private ControlP5 cp5;

	public void setup() {
		frameRate(24);

		// Create fonts
		f20 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 20);
		f08 = new ControlFont(createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 10));

		cp5 = new ControlP5(this);

		int guiElementHeight = 15;

		Button b;

		b = cp5.addButton("Outline");
		b.setValue(0);
		b.setPosition(20, 60);
		b.setSize(120, 40);
		styleControl(b);
		b.setId(21);

		b = cp5.addButton("Inline");
		b.setValue(0);
		b.setPosition(160, 60);
		b.setSize(120, 40);
		styleControl(b);
		b.setId(22);

		Slider2D s2d;
		s2d = cp5.addSlider2D("origin");
		s2d.setPosition(20, 160);
		s2d.setSize(200, 113);
		s2d.setMinX(originX.x);
		s2d.setMaxX(originX.z);
		s2d.setMinY(originY.x);
		s2d.setMaxY(originY.z);
		s2d.setArrayValue(new float[] { originX.y, originY.y });
		s2d.setId(10);
		styleControl(s2d);

		int xpos = 303;

		String[] names = new String[] { "radius", "rotation", "x distance", "y distance", "tickness", "elements" };
		PVector[] properties = new PVector[] { radius, rotation, distanceX, distanceY, thickness, elements };

		Slider s;
		for (int i = 0; i < 6; i++) {
			s = cp5.addSlider(names[i]);
			s.setPosition(20, xpos + i * 2 * guiElementHeight);
			s.setRange((float) properties[i].x, (float) properties[i].z);
			s.setValue((float) properties[i].y);
			s.setSize(200, guiElementHeight);
			styleControl(s);
			s.setNumberOfTickMarks((int) Math.ceil((float) properties[i].z - (float) properties[i].x + 1));
			s.showTickMarks(false);
			s.setId(i);
		}

		b = cp5.addButton("offsetted");
		b.setValue(0);
		b.setPosition(20, xpos + 6 * 2 * guiElementHeight);
		b.setSize(120, guiElementHeight);
		styleControl(b);
		b.setId(6);

		b = cp5.addButton("reset");
		b.setValue(0);
		b.setPosition(20, 580 - guiElementHeight);
		b.setSize(120, guiElementHeight);
		styleControl(b);
		b.setId(61);

		b = cp5.addButton("save screenshot");
		b.setValue(0);
		b.setPosition(160, 580 - guiElementHeight);
		b.setSize(120, guiElementHeight);
		styleControl(b);
		b.setId(62);
	}

	void styleControl(Controller<?> c) {
		c.setColorBackground(colors[3]); // dark grey
		c.setColorForeground(colors[0]); // white
		c.setColorActive(colors[1]); // light grey
		c.setColorValueLabel(colors[2]); // grey
		c.setFont(f08);
	}

	public void settings() {
		size(300, 600);
	}

	public void draw() {
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
		text("Appearance", 20, 144);

		// draw control p5 items
		cp5.show();
	}

	// function controlEvent will be invoked with every value change
	// in any registered controller
	public void controlEvent(ControlEvent theEvent) {
		switch (theEvent.getId()) {
		case (0):
			radius.y = theEvent.getController().getValue();
			break;
		case (1):
			rotation.y = radians(theEvent.getController().getValue());
			break;
		case (2):
			distanceX.y = theEvent.getController().getValue();
			break;
		case (3):
			distanceY.y = theEvent.getController().getValue();

			break;
		case (4):
			thickness.y = theEvent.getController().getValue();
			break;
		case (5):
			elements.y = theEvent.getController().getValue();
			break;
		case (6):
			if (offset) {
				offset = false;
			} else {
				offset = true;
			}
			break;
		case (10):
			originX.y = theEvent.getController().getArrayValue()[0];
			originY.y = theEvent.getController().getArrayValue()[1];
			break;
		case (21):
			outline = true;
			break;
		case (22):
			outline = false;
			break;
		case (61):
			reset();
			break;
		case (62):
			screenshot = true;
			break;
		}
		changes = true;
	}

	void reset() {
		JSONArray file = loadJSONArray("./src/net/tlahmann/gdg/data/patterizer-reset.json");

		JSONObject reset = file.getJSONObject(0);

		radius.y = reset.getFloat("radius");
		rotation.y = reset.getFloat("rotation");
		originX.y = reset.getJSONObject("origin").getFloat("x");
		originY.y = reset.getJSONObject("origin").getFloat("y");
		distanceX.y = reset.getJSONObject("distance").getFloat("x");
		distanceY.y = reset.getJSONObject("distance").getFloat("y");
		offset = reset.getInt("offset") == 1 ? distanceX.y / 2 : 0;

		thickness.y = reset.getFloat("thickness");
		elements.y = reset.getFloat("elements");

		outline = reset.getInt("outline") == 1;
	}
}
