package com.apjt.buildit.crawler;

import org.junit.ClassRule;
import org.junit.Test;

public class LauncherTest {
	@ClassRule
	public static final JettyTestRule JETTY_SERVER = new JettyTestRule();

	@Test
	public void test() {
		// TODO - need to expand the tests around this ...  
		//        main reason didn't is due to time constraints, and that the "toSiteString" functionality
		//        that is the main bulk of the code should be replaced by something else...

		// TODO - this doesn't actually test anything other than no errors occur, so not a great test

		Launcher.main(new String[] { JETTY_SERVER.getHostPort() + "/index.html" });
		//Launcher.main(new String[] { "https://en.wikipedia.org/wiki/English_Wikipedia" });
	}
}
