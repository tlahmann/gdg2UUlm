package net.tlahmann.gdg.creation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.tlahmann.gdg.creation.object.PShape;
import processing.core.PApplet;

/**
 * <h1>Patterizer</h1>
 * <p>
 * The <b>patterizer</b> class represents a simple window filled with elements.
 * The elements are specified within the <b>PShape</b> class. Element parameters
 * can be modified within the <b>gui</b> class.
 * </p>
 * <p>
 * The intention for this class is to generate patterns to be used by the
 * studentd of the course 'Grundlagen der Gestaltung 2' - 'Fundamentals of
 * Design 2' at the University of Ulm.
 * </p>
 * 
 * @author Tobias Lahmann
 * @version 1.0
 * @since 2016-10-05
 * 
 * @see processing.core.PApplet
 * @see net.tlahmann.gdg.creation.Gui
 * @see net.tlahmann.gdg.creation.object.PShape
 *
 */
public class Patterizer extends PApplet {

	/*
	 * private variables not to be modified
	 */
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 720;
	private static final int FRAMERATE = 60;

	private List<PShape> elements;

	public Gui gui;

	/**
	 * <p>
	 * Overwriting setup method from PApplet in Processing. Creates gui window
	 * and defines framerate.
	 * </p>
	 * 
	 * @since 2016-10-05
	 * 
	 * @see processing.core.PApplet
	 * @see net.tlahmann.gdg.creation.Gui
	 */
	public void setup() {
		gui = new Gui();
		// runSketch creates a new thread and runs it in another window.
		PApplet.runSketch(new String[] { "Graphical User Interface" }, gui);

		frameRate(FRAMERATE);
		init();
	}

	/**
	 * <p>
	 * Initialization method used to create the defined <b>PShapes</b> and store
	 * them within an ArrayList.
	 * </p>
	 * <p>
	 * The method is accessing the gui class instance to determine the
	 * appearance of the PShape. When the user changes parameters within the gui
	 * the initialization gets triggered again.
	 * </p>
	 * 
	 * @since 2016-10-05
	 * 
	 * @see net.tlahmann.gdg.creation.Gui
	 * @see net.tlahmann.gdg.creation.object.PShape
	 */
	private void init() {
		elements = new ArrayList<PShape>();
		boolean evodd = true;
		int l = 0;
		for (int j = (int) gui.originY.y; j < height + 100; j += gui.distanceY.y, l++) {
			float k = (evodd = !evodd) && gui.offset ? gui.distanceX.y / 2 : 0;
			for (int i = (int) gui.originX.y; i < width + 100; i += gui.distanceX.y) {
				elements.add(new PShape(this, new float[] { i + k, j },
						gui.radius.y * ((gui.radiusChange.z - l * gui.radiusChange.y) / gui.radiusChange.z),
						gui.rotation.y, gui.elements.y));
			}
		}
		gui.changes = false;
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
		size(WIDTH, HEIGHT);
	}

	/**
	 * <p>
	 * Overwriting draw method from PApplet in Processing. The draw method is
	 * checking for changes made within the gui and button clicks demanding a
	 * screenshot.
	 * </p>
	 * <p>
	 * Drawing with the specified framerate the PShapes get updated immediately.
	 * </p>
	 * 
	 * @since 2016-10-05
	 * 
	 * @see processing.core.PApplet
	 */
	public void draw() {
		if (gui.changes) {
			init();
		}
		if (gui.screenshot) {
			screenshot();
		}

		background(gui.colors[4]);

		for (PShape s : elements) {
			s.display();
		}
	}

	/**
	 * <p>
	 * Screenshot method gets called when the user requests a screenshot of the
	 * current setup of the scene. The screenshot is saved within the project
	 * folder as <i>"Patterizer-CURRENT_TIMESTAMP.png"</i>
	 * </p>
	 * 
	 * @since 2016-10-05
	 */
	private void screenshot() {
		saveFrame("Patterizer-" + timestamp() + ".png");
		gui.screenshot = false;
	}

	/**
	 * <p>
	 * The timestamp method returns the current system time in the format
	 * <i>YYYYMMDD_HHMMSS</i>.
	 * </p>
	 * Is solely used by the screenshot method.
	 * 
	 * @return String: the timestamp representing the current systemtime.
	 * 
	 * @since 2016-10-05
	 */
	private String timestamp() {
		Calendar now = Calendar.getInstance();
		return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now);
	}

	/**
	 * <p>
	 * Main method to create instance of the PApplet and start showing.
	 * </p>
	 * 
	 * @param args Method arguments, unused
	 * 
	 * @since 2016-10-05
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] { Patterizer.class.getName() });
	}

}
