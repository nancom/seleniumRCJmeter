package com.company.selenium_jmeter.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

/*
 * JMeter doesn't support @Rule, so taking test result and name needs to be done the "dirty" way, for taking screenshot on fail.
 * Name of the test needs to be set at the beginning of each test.
 * At the end of each test 'testResult' should be set to 'true' to mark that test as passed.
 */

public class TseTest extends MethodsForTestsAbstract {

	/**
	 * Test that should fail
	 */
	@Test
	public void testToFail() {
		testName = getMethodName();

		driver.get("http://www.useragentstring.com/");
		assertTrue(driver.findElement(By.id("uas_textfeld")).getText().isEmpty());

		testResult = true;
	}

	/**
	 * Test that should pass
	 */
	@Test
	public void testToPass() {
		testName = getMethodName();

		driver.get("http://www.useragentstring.com/");
		assertTrue(!driver.findElement(By.id("uas_textfeld")).getText().isEmpty());

		testResult = true;
	}
}
