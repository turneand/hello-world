package com.apjt.buildit.crawler.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class BaseElement {
	private final String url;
	private final Set<BaseElement> parents = Collections.synchronizedSet(new HashSet<>());

	public BaseElement(final String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void addParent(final BaseElement parent) {
		this.parents.add(parent);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.url);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}

		final BaseElement other = (BaseElement) obj;
		return Objects.equals(this.url, other.url);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [url=" + this.url + "]";
	}	
}
