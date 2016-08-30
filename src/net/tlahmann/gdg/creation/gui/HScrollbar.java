package net.tlahmann.gdg.creation.gui;

import net.tlahmann.gdg.creation.Patterizer;
import processing.core.PApplet;

public class HScrollbar {

	Patterizer parent;
	int swidth, sheight; // width and height of bar
	float xpos, ypos; // x and y position of bar
	float spos, newspos; // x position of slider
	float sposMin; // max and min values of slider
	public float sposMax;
	int loose; // how loose/heavy
	boolean over; // is the mouse over the slider?
	boolean locked;
	float ratio;
	public String text;

	public HScrollbar(Patterizer p, float xp, float yp, int sw, int sh, int l) {
		parent = p;
		swidth = sw;
		sheight = sh;
		int widthtoheight = sw - sh;
		ratio = (float) sw / (float) widthtoheight;
		xpos = xp;
		ypos = yp - sheight / 2;
		spos = xpos + swidth / 2 - sheight / 2;
		newspos = spos;
		sposMin = xpos;
		sposMax = swidth - sheight;
		loose = l;
	}

	public void update() {
		if (overEvent()) {
			over = true;
		} else {
			over = false;
		}
		if (parent.mousePressed && over) {
			locked = true;
		}
		if (!parent.mousePressed) {
			locked = false;
		}
		if (locked) {
			newspos = constrain(parent.mouseX - sheight / 2, sposMin, sposMax);
		}
		if (PApplet.abs(newspos - spos) > 1) {
			spos = spos + (newspos - spos) / loose;
		}
	}

	float constrain(float val, float minv, float maxv) {
		return PApplet.min(PApplet.max(val, minv), maxv);
	}

	boolean overEvent() {
		if (parent.mouseX > xpos && parent.mouseX < xpos + swidth && parent.mouseY > ypos
				&& parent.mouseY < ypos + sheight) {
			return true;
		} else {
			return false;
		}
	}

	public void display() {
		parent.line(xpos, ypos + sheight / 2, swidth, ypos + sheight / 2);
		if (over || locked) {
			parent.fill(0, 0, 0);
		} else {
			parent.fill(102, 102, 102);
		}
		parent.rect(spos, ypos, sheight, sheight);
		
		parent.fill(255);
		parent.textFont(parent.f14);
		parent.text(text, xpos + swidth, ypos + sheight );
	}

	public float getPos() {
		// Convert spos to be values between
		// 0 and the total width of the scrollbar
		return spos * ratio;
	}
}
