package com.apjt.buildit.crawler.parser;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apjt.buildit.crawler.entity.OtherElement;
import com.apjt.buildit.crawler.entity.PageElement;
import com.apjt.buildit.crawler.entity.BaseElement;

public class ParserImpl implements Parser {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private int timeoutMillis = 5000;
	private Consumer<PageElement> newPageElementConsumer;

	private final ConcurrentHashMap<String, BaseElement> elementCache = new ConcurrentHashMap<>();

	public void setNewPageElementConsumer(final Consumer<PageElement> newPageElementConsumer) {
		this.newPageElementConsumer = newPageElementConsumer;
	}

	public void setTimeoutMillis(final int timeoutMillis) {
		this.logger.info("Setting timeoutMillis to [{}]", timeoutMillis);
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	public void parse(final PageElement page) {
		// NOTE: Using Jsoup as parsing html pages is difficult due to the potential for "invalid" tags
		//       considered regular expressions but I know how bad some of the html is out there.
		//       I do NOT know jsoup (first result that came up on a google search), so if for a "real"
		//       implementation I would rather spend a bit more time investigating these libraries,
		//       alternatives, licensing issues, etc.  Also, would prefer a stream-based one rather than
		//       requiring the parsing of the full page into memory at once - may be possible, but as
		//       stated I do not know jsoup yet...

		try {
			this.logger.debug("Attempting to parse the page {}", page);
			final Document document = Jsoup.parse(new URL(page.getUrl()), this.timeoutMillis);

	        for (Element linkElement : document.select("a[href]")) {
	        	final AtomicBoolean isNewPageElement = new AtomicBoolean(false);
	        	String url = linkElement.absUrl("href");
	        	final int hashIndex = url.indexOf('#');
	        	url = hashIndex != -1 ? url.substring(0, hashIndex) : url;

	        	if (url.length() > 0) {
		        	final BaseElement child = this.elementCache.compute(url, (key, value) -> {
		        		if (value == null) {
		        			if (isInternal(linkElement, "href")) {
		        				value = new PageElement(key);
		        				// trade-off, slightly breaks functional style by having external state, but we need
		        				// to know externally whether this was a new page or not...
		        				isNewPageElement.set(true);
		        			} else {
		        				value = new OtherElement(key, "a");
		        			}
		        		}
	
		        		value.addParent(page);
		        		return value;
		        	});
	
		        	page.addChild(child);

		        	if (isNewPageElement.get()) {
						this.newPageElementConsumer.accept((PageElement) child);
					}
	        	}
	        }

	        for (Element elementWithSrc : document.select("[src]")) {
	        	page.addChild(this.elementCache.compute(elementWithSrc.absUrl("src"), (key, value) -> {
	        		if (value == null) {
	        			value = new OtherElement(key, elementWithSrc.tagName());
	        		}

	        		value.addParent(page);
	        		return value;
	        	}));
	        }

	        page.setHttpStatusCode(Integer.valueOf(200));
		} catch (final HttpStatusException e) {
			this.logger.warn("Processing page {} failed", page, e);
			page.setHttpStatusCode(e.getStatusCode());
		} catch (final Exception e) {
			this.logger.warn("Processing page {} failed", page, e);
			page.setHttpStatusCode(Integer.valueOf(500));
		}
	}

	/**
	 * Utility method that attempts to determine if a particular element represents an internal link, within the scope
	 * of the base url.
	 */
	private boolean isInternal(final Element element,
							   final String attributeName) {
		// TODO - a bit primitive, need to also handle situations where the url has the full path to the current server...		
		return !Objects.equals(element.attr(attributeName), element.absUrl(attributeName));
	}
}
