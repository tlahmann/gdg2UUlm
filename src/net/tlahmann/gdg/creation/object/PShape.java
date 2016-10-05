package net.tlahmann.gdg.creation.object;

import net.tlahmann.gdg.creation.Patterizer;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * <h1>PShape</h1>
 * <p>
 * The <b>PShape</b> class represents an elements. Element parameters can be
 * modified within the <b>gui</b> class.
 * </p>
 * 
 * @author Tobias Lahmann
 * @version 1.0
 * @since 2016-10-05
 * 
 * @see net.tlahmann.gdg.creation.Gui
 */
public class PShape {
	/*
	 * private variables not to be modified
	 */
	private Patterizer parent;
	private PVector[] points;
	private PVector center;

	/**
	 * <p>
	 * Constructor used to create one PShape of a specific form and position.
	 * </p>
	 * 
	 * @since 2016-10-05
	 * 
	 * @param p the parent element of type Patterizer. Used to hold the instance
	 *            to be drawn on
	 * @param pos the position of the center point
	 * @param radius the expansion of the PShape
	 * @param rotation used to rotate around the center
	 * @param ends number of end points in this PShape
	 */
	public PShape(Patterizer p, float[] pos, float radius, float rotation, float ends) {
		parent = p;

		center = new PVector(pos[0], pos[1], 0);
		points = new PVector[(int) Math.ceil(ends)];
		for (int i = 0; i < ends; i++) {
			float d = PApplet.map(i, 0, ends, 0, PApplet.TWO_PI) - PApplet.HALF_PI;
			points[i] = new PVector(center.x + PApplet.cos(d + rotation) * radius,
					center.y + PApplet.sin(d + rotation) * radius);
		}
	}

	/**
	 * <p>
	 * The display method is used to draw the PShape onto a canvas provided by
	 * its parent. The parent needs to extend the <b>PApplet</b> class.
	 * </p>
	 * 
	 * @since 2016-10-05
	 */
	public void display() {
		parent.stroke(parent.gui.colors[0]);
		parent.strokeWeight(parent.gui.thickness.y);
		if (!parent.gui.outline) {
			parent.strokeCap(PApplet.SQUARE);
			for (int i = 0; i < points.length; i++) {
				parent.line(center.x, center.y, points[i].x, points[i].y);
			}
		} else {
			parent.strokeCap(PApplet.ROUND);
			int i;
			for (i = 0; i < points.length - 1; i++) {
				parent.line(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
			}
			parent.line(points[i].x, points[i].y, points[0].x, points[0].y);
		}
	}
}
