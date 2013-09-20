package com.company.selenium_jmeter.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class MethodsForTestsAbstract {

	public WebDriver						driver;

	protected boolean						testResult					= false;
	protected String						testName						= "no_name";

	private static final String	user_agent					= getProperty("user_agent",
																											"Mozilla/5.0 (Windows; U; MSIE 9.0; WIndows NT 9.0; en-US))");
	private static final String	selenium_server			= getProperty("selenium_server", "http://localhost:4444/wd/hub");
	private static final String	screnshot_folder		= getProperty("screnshot_folder",
																											"target/surefire-reports/screenshots/");
	private static final File		phantomjs_exe_file	= getPhantomJs();

	/**
	 * Returns property value. If property is not found in properties file sets the property value to
	 * propertyDefaultValue.
	 * 
	 * @param propertyName
	 * @param propertyDefaultValue
	 * @return
	 */
	public static String getProperty(String propertyName, String propertyDefaultValue) {
		Properties prop = new Properties();
		try {
			prop.load(MethodsForTestsAbstract.class.getResourceAsStream("/test.properties"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String out = prop.getProperty(propertyName, propertyDefaultValue);
		System.out.println("\t" + propertyName + " = " + out);
		return out.equals("empty") ? "" : out;
	}

	/**
	 * Will copy phantomjs.exe from resources to temp.
	 * 
	 * @return
	 */
	public static File getPhantomJs() {
		InputStream input = MethodsForTestsAbstract.class.getResourceAsStream("/phantomjs-windows/phantomjs.exe");
		OutputStream output;
		File phantomjs_exe = new File(System.getenv("TEMP") + "/phantomjs.exe");
		try {
			output = new FileOutputStream(phantomjs_exe);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = input.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}
			input.close();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return phantomjs_exe;
	}

	/**
	 * Checks whether phantomjs.exe file exists. Will be done before each class.
	 * 
	 * @throws IOException
	 */
	@BeforeClass
	public static void superSetUp() throws IOException {
		if (!phantomjs_exe_file.exists()) {
			throw new FileNotFoundException("Execution stopped! Could not find phantomjs.exe, check path and try again ("
					+ phantomjs_exe_file.getAbsolutePath() + ")!");
		}

		/*
		 * We can't start automatically selenium-server before each class, as in JMeter it would be called for every thread
		 * and loop, we need to start in manually.
		 */
		/*
		 * Runtime.getRuntime().exec(new String[] { "cmd", "/C", "start-selenium-server.bat" }, null, new
		 * File("src\\test\\resources\\selenium-server")); waitNsec(5);
		 */
	}

	/**
	 * Method to start driver before each test. Will be done before each test.
	 * 
	 * @throws MalformedURLException
	 */
	@Before
	public void setUp() throws MalformedURLException {

		/*
		 * Done based on: http://rationaleemotions.wordpress.com/2012/01/23/setting-up-grid2-and-working-with-it/
		 * http://www.anzaan.com/2012/07/integrating-selenium-with-jmeter-for-application-load-testing/
		 * http://code.google.com/p/selenium/wiki/Grid2
		 */

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setBrowserName(DesiredCapabilities.phantomjs().getBrowserName());
		caps.setCapability("phantomjs.binary.path", phantomjs_exe_file.getAbsolutePath());
		caps.setCapability("phantomjs.page.settings.userAgent", user_agent);

		driver = new RemoteWebDriver(new URL(selenium_server), caps);

		// JMeter doesn't support @Rule, so taking test result and name needs to be done the "dirty" way, for taking
		// screenshot on fail.
		testResult = false;
		testName = "no_name";
	}

	/**
	 * Method to close driver after each test. Will be done after each test.
	 */
	@After
	public void tearDown() {
		// take screenshot in case of failed test
		if (!testResult) {
			takeScreenshot(this.getClass().getSimpleName() + "_" + testName + "_" + ((RemoteWebDriver) driver).getSessionId());
		}

		driver.close();
		driver.quit();
	}

	/**
	 * Will be done after each class.
	 * 
	 * @throws IOException
	 */
	@AfterClass()
	public static void superTearDown() throws IOException {
		/*
		 * Runtime.getRuntime().exec("wmic path win32_process Where \"CommandLine Like '%selenium-server%'\"  Call Terminate"
		 * );
		 */
	}

	/**
	 * Get the method name
	 * 
	 * @return method name
	 */
	public String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	/**
	 * To make a screenshot, will save it in 'screnshot_folder' as name.png.
	 * 
	 * @param name
	 */
	private void takeScreenshot(String name) {
		try {
			new File(screnshot_folder).mkdirs();
			/*
			 * here is the reason why i start the driver before each test rather than before a class, as to take a screenshot
			 * from gui-less driver the driver needs to be augmented. There is no reason for the driver to stay in enhanced
			 * state after the test, as the enhanced driver occupies more memory, so I close it after each test, no mater
			 * whether passed or failed.
			 */
			driver = new Augmenter().augment(driver);
			FileOutputStream out = new FileOutputStream(screnshot_folder + name + ".png");
			out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
			out.close();
			System.out.println("Screenshot saved (" + System.getProperty("user.dir") + "\\"
					+ screnshot_folder.replace("/", "\\") + name + ".png).");
		} catch (Exception e) {
			System.err.println("Failed to save screenshot (" + name + ".png).");
			// No need to crash the tests if the screenshot fails
		}
	}

	/**
	 * Waits n seconds
	 * 
	 * @param n
	 */
	public static void waitNsec(int n) {
		long t0, t1;
		t0 = System.currentTimeMillis();
		do {
			t1 = System.currentTimeMillis();
		} while (t1 - t0 < (n * 1000));
	}
}
