package net.tlahmann.gdg.creation.gui;

import net.tlahmann.gdg.creation.Patterizer;
import processing.core.PApplet;

public class Handle {

	Patterizer parent;
	int x, y;
	int boxx, boxy;
	int stretch;
	int size;
	boolean over;
	boolean press;
	boolean locked = false;
	boolean otherslocked = false;
	Handle[] others;

	public Handle(Patterizer p, int ix, int iy, int il, int is, Handle[] o) {
		parent = p;
		x = ix;
		y = iy;
		stretch = il;
		size = is;
		boxx = x + stretch - size / 2;
		boxy = y - size / 2;
		others = o;
	}

	public void update() {
		boxx = x + stretch;
		boxy = y - size / 2;

		for (int i = 0; i < others.length; i++) {
			if (others[i].locked == true) {
				otherslocked = true;
				break;
			} else {
				otherslocked = false;
			}
		}

		if (otherslocked == false) {
			overEvent();
			pressEvent();
		}

		if (press) {
			stretch = lock(parent.mouseX - parent.width / 2 - size / 2, 0, parent.width / 2 - size - 1);
		}
	}

	void overEvent() {
		if (overRect(boxx, boxy, size, size)) {
			over = true;
		} else {
			over = false;
		}
	}

	void pressEvent() {
		if (over && parent.mousePressed || locked) {
			press = true;
			locked = true;
		} else {
			press = false;
		}
	}

	public void releaseEvent() {
		locked = false;
	}

	public void display() {
		parent.line(x, y, x + stretch, y);
		parent.fill(255);
		parent.stroke(0);
		parent.rect(boxx, boxy, size, size);
		if (over || press) {
			parent.line(boxx, boxy, boxx + size, boxy + size);
			parent.line(boxx, boxy + size, boxx + size, boxy);
		}

	}

	boolean overRect(int x, int y, int width, int height) {
		if (parent.mouseX >= x && parent.mouseX <= x + width && parent.mouseY >= y && parent.mouseY <= y + height) {
			return true;
		} else {
			return false;
		}
	}

	int lock(int val, int minv, int maxv) { 
	  return  PApplet.min(PApplet.max(val, minv), maxv); 
	}
}