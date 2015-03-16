package com.company.selenium_jmeter.example;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jmeter.MethodsForTestsAbstract;
import junit.framework.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * JMeter doesn't support @Rule, so taking test result and name needs to be done the "dirty" way, for taking screenshot on fail.
 * Name of the test needs to be set at the beginning of each test.
 * At the end of each test 'testResult' should be set to 'true' to mark that test as passed.
 */

public class TseTest extends MethodsForTestsAbstract {
    private static Logger LOGGER = LoggerFactory.getLogger(TseTest.class);
	/**
	 * Test that should fail
	 */
	@Test
	public void testToFail() {
		testName = getMethodName();

		driver.get("http://www.useragentstring.com/");
        LOGGER.debug("Text Present : {}",phantomWebDriver.waitForElementPresentById("uas_textfeld"));
        LOGGER.debug("Text : {}", phantomWebDriver.getValueById("uas_textfeld"));
        assertFalse(phantomWebDriver.getValueById("uas_textfeld").isEmpty());
        testResult = true;
	}

	/**
	 * Test that should pass
	 */
	@Test
	public void testToPass() {
		testName = getMethodName();

		driver.get("http://www.useragentstring.com/");
        LOGGER.debug("Text Present : {}",phantomWebDriver.waitForElementPresentById("uas_textfeld"));
        LOGGER.debug("Text : {}", phantomWebDriver.getValueById("uas_textfeld"));
        phantomWebDriver.waitForElementPresentById("uas_textfeld");
        assertTrue(!phantomWebDriver.getValueById("uas_textfeld").isEmpty());
		testResult = true;
	}
}
