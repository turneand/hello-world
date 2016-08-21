package com.apjt.buildit.crawler.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PageElement extends BaseElement {
	private final Set<BaseElement> children = Collections.synchronizedSet(new HashSet<>());
	private Integer httpStatusCode;

	public PageElement(String url) {
		super(url);
	}

	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(Integer httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public void addChild(final BaseElement child) {
		this.children.add(child);
	}

	public Collection<BaseElement> getChildren() {
		synchronized (this.children) {
			return new ArrayList<>(this.children);
		}
	}
}
