package com.project.app.util.paginator;

public class PageItem {

	private int n;
	private boolean actual;

	public int getN() {
		return n;
	}

	public boolean isActual() {
		return actual;
	}

	public PageItem(int n, boolean actual) {
		super();
		this.n = n;
		this.actual = actual;
	}
}
