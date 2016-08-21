package com.apjt.buildit.crawler.entity;

// TODO - determine whether this is sufficient or whether we'd want to introduce
//        more types for each supported element - or even collapse into a single type.
public class OtherElement extends BaseElement {
	private final String type;
	
	public OtherElement(final String url,
						final String type) {
		super(url);
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
