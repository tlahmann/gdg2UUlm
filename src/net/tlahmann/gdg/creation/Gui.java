package net.tlahmann.gdg.creation;

import java.util.ArrayList;
import java.util.List;

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

/**
 * <h1>Gui</h1>
 * <p>
 * The graphical user interface provides a way to change the look of the created
 * pattern within a patterizer.
 * </p>
 * 
 * @author Tobias Lahmann
 * @version 1.0
 * @since 2016-10-05
 * 
 * @see processing.core.PApplet
 * @see net.tlahmann.gdg.creation.Patterizer
 * @see controlP5.ControlP5
 */
public class Gui extends PApplet {

	/*
	 * public variables representing the shape of one PShape most often the
	 * variable is represented by a vector of the following array:
	 * PVector(minimal value, current or initial value, maximal value)
	 */
	public PVector radius = new PVector(5, 50, 1000);
	public PVector radiusChange = new PVector(0, 0, 10);
	public PVector rotation = new PVector(0, 0, 360);
	public PVector originX = new PVector(0, 0, 1024);
	public PVector originY = new PVector(0, 0, 720);
	public PVector distanceX = new PVector(10, 100, 1024);
	public PVector distanceY = new PVector(10, 100, 720);
	public boolean offset = false;

	public PVector thickness = new PVector(1, 1, 10);
	public PVector elements = new PVector(1, 4, 50);

	public boolean outline = false;

	// fixed colors. It is not intended to change these at runtime to focus on
	// the pattern and shape rather than color
	public int[] colors = new int[] { color(255, 255, 255, 255), color(230, 230, 230, 255), color(128, 128, 128, 255),
			color(50, 50, 50, 255), color(0, 0, 0, 255) };

	/*
	 * public variable to react to changes within the gui. This manner was
	 * chosen to avoid exceptions regarding the length of the pshape. If changes
	 * have been made while the main thread is drawing the look of the elements
	 * would not change but the patterizer would crash.
	 */
	public boolean changes = false;
	// Public variables to determine the request of a screenshot
	public boolean screenshot = false;

	// list of control elements
	private List<Controller<?>> controllers = new ArrayList<Controller<?>>();

	private PFont f20;
	private ControlFont cf10;
	private ControlP5 cp5;

	/**
	 * <p>
	 * Overwriting setup method from PApplet in Processing. Defines framerate,
	 * initializes fonts and initializes the control elements.
	 * </p>
	 * 
	 * @since 2016-10-05
	 */
	public void setup() {
		// 30 fps was chosen to react in a timely manner to interaction with the
		// user
		frameRate(30);

		// Create fonts
		f20 = createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 20);
		cf10 = new ControlFont(createFont("./src/net/tlahmann/gdg/data/OpenSans-Regular.ttf", 10));

		initControls();
	}

	/**
	 * <p>
	 * Initializes the control elements.
	 * </p>
	 * 
	 * @since 2016-10-05
	 */
	private void initControls() {
		cp5 = new ControlP5(this);

		int guiElementHeight = 15;

		Button b;

		b = cp5.addButton("Outline");
		b.setValue(0);
		b.setPosition(20, 60);
		b.setSize(120, 40);
		styleControl(b);
		b.setId(21);
		controllers.add(b);

		b = cp5.addButton("Inline");
		b.setValue(0);
		b.setPosition(160, 60);
		b.setSize(120, 40);
		styleControl(b);
		b.setId(22);
		controllers.add(b);

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
		controllers.add(s2d);

		int xpos = 303;

		String[] names = new String[] { "radius", "radius change", "rotation", "x distance", "y distance", "tickness",
				"elements" };
		PVector[] properties = new PVector[] { radius, radiusChange, rotation, distanceX, distanceY, thickness,
				elements };

		Slider s;
		for (int i = 0; i < names.length; i++) {
			s = cp5.addSlider(names[i]);
			s.setPosition(20, xpos + i * 2 * guiElementHeight);
			s.setRange((float) properties[i].x, (float) properties[i].z);
			s.setValue((float) properties[i].y);
			s.setSize(200, guiElementHeight);
			styleControl(s);
			s.setNumberOfTickMarks((int) Math.ceil((float) properties[i].z - (float) properties[i].x + 1));
			s.showTickMarks(false);
			s.setId(i);
			controllers.add(s);
		}

		b = cp5.addButton("offsetted");
		b.setValue(0);
		b.setPosition(20, xpos + names.length * 2 * guiElementHeight);
		b.setSize(120, guiElementHeight);
		styleControl(b);
		b.setId(7);
		controllers.add(b);

		b = cp5.addButton("reset");
		b.setValue(0);
		b.setPosition(20, 580 - guiElementHeight);
		b.setSize(120, guiElementHeight);
		styleControl(b);
		b.setId(61);
		controllers.add(b);

		b = cp5.addButton("save screenshot");
		b.setValue(0);
		b.setPosition(160, 580 - guiElementHeight);
		b.setSize(120, guiElementHeight);
		styleControl(b);
		b.setId(62);
		controllers.add(b);
	}

	/**
	 * <p>
	 * Set consistent style to the control elements.
	 * </p>
	 * 
	 * @since 2016-10-05
	 */
	private void styleControl(Controller<?> c) {
		c.setColorBackground(colors[3]); // dark grey
		c.setColorForeground(colors[0]); // white
		c.setColorActive(colors[1]); // light grey
		c.setColorValueLabel(colors[2]); // grey
		c.setFont(cf10);
	}

	/**
	 * <p>
	 * Overwriting settings method from PApplet in Processing. Necessary because
	 * when not using the PDE, size() can only be used inside settings().
	 * </p>
	 * 
	 * @since 2016-10-05
	 * 
	 * @see processing.core.PApplet
	 */
	public void settings() {
		size(300, 600);
	}

	/**
	 * <p>
	 * Overwriting draw method from PApplet in Processing. The draw method is
	 * displaying the defined control elements.
	 * </p>
	 * 
	 * @since 2016-10-05
	 * 
	 * @see processing.core.PApplet
	 */
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

	/**
	 *  function controlEvent will be invoked with every value change
	 *  in any registered controller
	 *  
	 * @param ev calling event to handle
	 */
	public void controlEvent(ControlEvent ev) {
		switch (ev.getId()) {
		case (0):
			radius.y = ev.getController().getValue();
			break;
		case (1):
			radiusChange.y = ev.getController().getValue();
			break;
		case (2):
			rotation.y = radians(ev.getController().getValue());
			break;
		case (3):
			distanceX.y = ev.getController().getValue();
			break;
		case (4):
			distanceY.y = ev.getController().getValue();
			break;
		case (5):
			thickness.y = ev.getController().getValue();
			break;
		case (6):
			elements.y = ev.getController().getValue();
			break;
		case (7):
			if (offset) {
				offset = false;
			} else {
				offset = true;
			}
			break;
		case (10):
			originX.y = ev.getController().getArrayValue()[0];
			originY.y = ev.getController().getArrayValue()[1];
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

	/**
	 *  Resets the variables to its initial state.
	 *  
	 * @param ev calling event to handle
	 */
	private void reset() {
		JSONArray file = loadJSONArray("./src/net/tlahmann/gdg/data/patterizer-reset.json");

		JSONObject reset = file.getJSONObject(0);

		controllers.get(2).setArrayValue(new float[] { reset.getJSONObject("origin").getFloat("x"),
				reset.getJSONObject("origin").getFloat("y") });
		controllers.get(3).setValue(reset.getFloat("radius"));
		controllers.get(4).setValue(reset.getFloat("radiusChange"));
		controllers.get(5).setValue(reset.getFloat("rotation"));
		controllers.get(6).setValue(reset.getJSONObject("distance").getFloat("x"));
		controllers.get(7).setValue(reset.getJSONObject("distance").getFloat("y"));
		controllers.get(8).setValue(reset.getFloat("thickness"));
		controllers.get(9).setValue(reset.getFloat("elements"));
		offset = reset.getInt("offset") == 1;
		outline = reset.getInt("outline") == 1;
	}
}
