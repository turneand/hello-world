package com.apjt.buildit.crawler;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * A JUnit <code>TestRule</code> that creates an embedded Jetty instance
 * for the lifetime of a specific test class allowing us to better test any
 * required HTTP connections as required by the crawler.
 */
public class JettyTestRule extends TestWatcher {
	private Server server;

	public String getHostPort() {
		return "http://localhost:" + ((ServerConnector) this.server.getConnectors()[0]).getLocalPort();
	}

	@Override
	protected void starting(final Description description) {
		final ResourceHandler handler = new ResourceHandler();
		handler.setBaseResource(Resource.newClassPathResource("com/apjt/buildit/crawler/websamples"));

		this.server = new Server(0);
		this.server.setHandler(handler);

		try {
			this.server.start();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void finished(final Description description) {
		if (this.server != null) {
			try {
				this.server.stop();
				this.server = null;
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
