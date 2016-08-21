package com.apjt.buildit.crawler.parser;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.apjt.buildit.crawler.JettyTestRule;
import com.apjt.buildit.crawler.entity.OtherElement;
import com.apjt.buildit.crawler.entity.PageElement;
import com.apjt.buildit.crawler.entity.BaseElement;

/**
 * Test case for the {@link ParserImpl} class.
 */
public class ParserImplTest {
	@ClassRule
	public static final JettyTestRule JETTY_SERVER = new JettyTestRule();

	private List<PageElement> newInternalPages;
	private ParserImpl parser;

	@Before
	public void setUp() {
		this.newInternalPages = new ArrayList<>();
		this.parser = new ParserImpl();
		this.parser.setTimeoutMillis(5000);
		this.parser.setNewPageElementConsumer(this.newInternalPages::add);
	}

	@Test
	public void testSingleImage() {
		final Map<String, BaseElement> urls = parse("/singleImage.html", Integer.valueOf(200));
		final String expectedUrl = JETTY_SERVER.getHostPort() + "/image1.jpg";
		assertThat(urls.keySet(), containsInAnyOrder(expectedUrl));
		assertThat(urls.get(expectedUrl), allOf(instanceOf(OtherElement.class), hasProperty("type", equalTo("img"))));
		assertThat(this.newInternalPages, empty());
	}

	@Test
	public void testDuplicateImages() {
		final Map<String, BaseElement> urls = parse("/duplicateImages.html", Integer.valueOf(200));
		final String expectedUrl = JETTY_SERVER.getHostPort() + "/image1.jpg";
		assertThat(urls.keySet(), containsInAnyOrder(expectedUrl));
		assertThat(urls.get(expectedUrl), allOf(instanceOf(OtherElement.class), hasProperty("type", equalTo("img"))));
		assertThat(this.newInternalPages, empty());
	}

	@Test
	public void testSingleInternalLink() {
		final Map<String, BaseElement> urls = parse("/singleInternalLink.html", Integer.valueOf(200));
		final String expectedUrl = JETTY_SERVER.getHostPort() + "/file1.html";
		assertThat(urls.keySet(), containsInAnyOrder(expectedUrl));
		assertThat(urls.get(expectedUrl), instanceOf(PageElement.class));
		assertThat(this.newInternalPages, hasSize(1));
		assertThat(this.newInternalPages.get(0).getUrl(), equalTo(expectedUrl));
	}

	@Test
	public void testSingleExternalLink() {
		final Map<String, BaseElement> urls = parse("/singleExternalLink.html", Integer.valueOf(200));
		assertThat(urls.keySet(), containsInAnyOrder("http://www.google.co.uk"));
		assertThat(urls.get("http://www.google.co.uk"), allOf(instanceOf(OtherElement.class), hasProperty("type", equalTo("a"))));
		assertThat(this.newInternalPages, empty());
	}

	@Test
	public void testComplex() {
		final Map<String, BaseElement> urls = parse("/index.html", Integer.valueOf(200));
		assertThat(urls.keySet(), containsInAnyOrder(JETTY_SERVER.getHostPort() + "/image1.jpg", JETTY_SERVER.getHostPort() + "/image2.jpg", JETTY_SERVER.getHostPort() + "/image3.jpg", JETTY_SERVER.getHostPort() + "/file1.html", JETTY_SERVER.getHostPort() + "/nested/file2.html", JETTY_SERVER.getHostPort() + "/file3.html"));
		assertThat(this.newInternalPages, hasSize(3));

		// TODO - expand test to validate content...
	}

	@Test
	public void testMissingPage() {
		final Map<String, BaseElement> urls = parse("/missingPage.html", Integer.valueOf(404));
		assertThat(urls.keySet(), empty());
	}

	private Map<String, BaseElement> parse(final String urlSuffix,
										  final Integer expectedStatusCode) {
		final PageElement page = new PageElement(JETTY_SERVER.getHostPort() + urlSuffix);
		this.parser.parse(page);

		assertThat(page.getHttpStatusCode(), equalTo(expectedStatusCode));
		return page.getChildren().stream().collect(Collectors.toMap(BaseElement::getUrl, Function.identity()));
	}
}
