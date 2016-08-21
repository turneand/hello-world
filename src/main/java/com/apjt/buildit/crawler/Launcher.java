package com.apjt.buildit.crawler;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apjt.buildit.crawler.entity.BaseElement;
import com.apjt.buildit.crawler.entity.OtherElement;
import com.apjt.buildit.crawler.entity.PageElement;
import com.apjt.buildit.crawler.parser.Parser;
import com.apjt.buildit.crawler.parser.ParserImpl;

public class Launcher {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Parser parser;
	private int maxPages;
	private int currentPage = 0;

	public static void main(final String[] args) {
		// TODO - validate input...
		final String baseUrl = args[0];

		final ParserImpl parser = new ParserImpl();
		final Launcher launcher = new Launcher();		
		launcher.maxPages = 100;
		launcher.parser = parser;

		parser.setTimeoutMillis(5000);
		parser.setNewPageElementConsumer(launcher::parse);

		final PageElement basePage = launcher.parse(new PageElement(baseUrl));		

		// TODO - use a nicer graphing tool than this simple output
		final StringBuilder builder = new StringBuilder(1024);
		launcher.toSiteString(builder, basePage, 0, new HashSet<>());
		launcher.logger.info("\nProgram output follows:\n");
		launcher.logger.info("{}", builder);
	}

	private PageElement parse(final PageElement page) {
		// TODO - should handle maxDepth rather than maxPages
		if (this.currentPage++ > this.maxPages) {
			this.logger.warn("Skipping page {} as already processed {} pages", page, this.maxPages);
		} else {
			// TODO - make asynchronous and add handling for waiting until completion,
			//        something like CompletableFuture.runAsync
			this.parser.parse(page);
		}

		return page;
	}

	// TODO - recursive method to generate a String for entity graph
	private void toSiteString(final StringBuilder builder,
							  final BaseElement element,
					   	      final int depth,
					          final Set<PageElement> printedPages) {
		builder.append('\n');
		IntStream.range(0, depth).forEach((i) -> { builder.append("   "); });

		if (element instanceof PageElement) {
			final PageElement page = (PageElement) element;

			builder.append("INTERNAL - ");

			if (page.getHttpStatusCode() == null) {
				builder.append("SKIPPED - ").append(element.getUrl());
			} else if (!Integer.valueOf(200).equals(page.getHttpStatusCode())) {
				builder.append("ERROR[").append(page.getHttpStatusCode()).append("] - ").append(element.getUrl());
			} else if (printedPages.contains(page)) {
				builder.append("PREVIOUSLY PRINTED - ").append(element.getUrl());
			} else {
				builder.append(element.getUrl());
				printedPages.add(page);
				page.getChildren().forEach((child) -> { toSiteString(builder, child, depth + 1, printedPages); });				
			}
		} else if (element instanceof OtherElement) {
			builder.append(((OtherElement) element).getType()).append(" - ").append(element.getUrl());
		} else {
			throw new IllegalArgumentException("Unsupported element: " + element);
		}
	}
}
