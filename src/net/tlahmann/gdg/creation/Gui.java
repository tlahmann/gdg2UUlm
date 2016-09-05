package net.tlahmann.gdg.creation;

import controlP5.Button;
import controlP5.ControlElement;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.Slider;
import net.tlahmann.gdg.creation.helper.Tuple;
import processing.core.PApplet;
import processing.core.PFont;

public class Gui extends PApplet {

	public float length = 50.0f;
	public float rotation = 0.0f;
	public Tuple<Float, Float> offset = new Tuple<Float, Float>(74.f, 84.f);
	public Tuple<Float, Float> numberOfArms = new Tuple<Float, Float>(6.f, 12.f);
	public Tuple<Float, Float> thickness = new Tuple<Float, Float>(2.f, 12.f);
	public Tuple<Integer, Integer> colors = new Tuple<Integer, Integer>(color(255, 255, 255, 255), color(0, 0, 0, 255));

	private PFont f20;
	ControlFont font;
	private ControlP5 cp5;

	public void setup() {
		frameRate(24);

		// Create fonts
		f20 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 20);
		PFont f08 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 8);
		font = new ControlFont(f08);

		cp5 = new ControlP5(this);

		int guiElementHeight = 15;
		int xpos = 110;

		Slider s;// pseudo useful object to hold sliders temporarily
		String[] names = new String[] { "arm length", "rotation", "x offset", "y offset", "tickness", "arms" };
		Tuple[] ranges = new Tuple[] { new Tuple<Float, Float>(10.f, 500.f), new Tuple<Float, Float>(0.f, PI),
				new Tuple<Float, Float>(1.f, 1000.f), new Tuple<Float, Float>(1.f, 1000.f),
				new Tuple<Float, Float>(1.f, thickness.y), new Tuple<Float, Float>(1.f, numberOfArms.y) };
		float[] values = new float[] { length, rotation, offset.x, offset.y, thickness.x, numberOfArms.x };

		for (int i = 0; i < 6; i++) {
			s = cp5.addSlider(names[i]);
			s.setPosition(20, xpos + i * 2 * guiElementHeight);
			s.setRange((float) ranges[i].x, (float) ranges[i].y);
			s.setValue(values[i]);
			s.setSize(200, guiElementHeight);
			styleControl(s);
			s.setId(i);
			if (i == 4) {
				s.setNumberOfTickMarks((int) Math.ceil(thickness.y));
			} else if (i == 5) {
				s.setNumberOfTickMarks((int) Math.ceil(numberOfArms.y));
			}
		}

		Button b;
		b = cp5.addButton("reset");
		b.setValue(0);
		b.setPosition(20, 470);
		b.setSize(100, guiElementHeight);
		styleControl(b);
		b.setId(8);

		b = cp5.addButton("save screenshot");
		b.setValue(0);
		b.setPosition(140, 470);
		b.setSize(100, guiElementHeight);
		styleControl(b);
		b.setId(9);
	}

	void styleControl(Controller<?> c) {
		c.setColorBackground(color(50, 50, 50, 255)); // light grey
		c.setColorForeground(color(255, 255, 255, 255)); // white
		c.setColorActive(color(230, 230, 230, 255)); // gark grey
		c.setColorValueLabel(color(128, 128, 128, 255)); // grey
		c.setFont(font);
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
			// init();
			break;
		case (1):
			rotation = theEvent.getController().getValue();
			// init();
			break;
		case (2):
			offset.x = theEvent.getController().getValue();
			// init();
			break;
		case (3):
			offset.y = theEvent.getController().getValue();
			// init();
			break;
		case (4):
			thickness.x = theEvent.getController().getValue();
			// init();
			break;
		case (5):
			numberOfArms.x = theEvent.getController().getValue();
			// init();
			break;
		}
	}
}
