package com.project.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;

	private int totalPaginas;
	private int numElementosPagina;
	private int paginaActual;

	private List<PageItem> paginas;

	// Getters

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	// End Getters

	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();

		numElementosPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() + 1;

		int startPage, endPage;
		if (totalPaginas <= numElementosPagina) {
			startPage = 1;
			endPage = totalPaginas;
		} else {
			if (paginaActual <= numElementosPagina / 2) {
				startPage = 1;
				endPage = numElementosPagina;
			} else if (paginaActual >= totalPaginas - numElementosPagina / 2) {
				startPage = totalPaginas - numElementosPagina + 1;
				endPage = numElementosPagina;
			} else {
				startPage = paginaActual - numElementosPagina / 2;
				endPage = numElementosPagina;
			}
		}

		for (int i = 0; i < endPage; i++) {
			paginas.add(new PageItem(startPage + i, paginaActual == startPage + i));
		}
	}
}
