package com.automation.base;

import com.automation.utils.ConfigReader;
import com.automation.utils.ExtentReportManager;
import com.automation.utils.ScreenshotUtils;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Base test class containing driver setup, teardown and common utilities
 */
public class BaseTest {

    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Get current driver instance
     */
    public static AppiumDriver getDriver() {
        return driver.get();
    }

    /**
     * Get current ExtentTest instance
     */
    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    @BeforeSuite
    public void suiteSetup() {
        ExtentReportManager.initializeReport();
    }

    @BeforeMethod
    public void setUp(ITestResult result) throws MalformedURLException {
        // Initialize ExtentTest for current test method
        String testName = result.getMethod().getMethodName();
        ExtentTest test = ExtentReportManager.createTest(testName);
        extentTest.set(test);

        // Setup Appium driver
        setupDriver();

        getExtentTest().info("Test started: " + testName);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver(), result.getMethod().getMethodName());
            getExtentTest().fail("Test Failed").addScreenCaptureFromPath(screenshotPath);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            getExtentTest().pass("Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            getExtentTest().skip("Test Skipped");
        }

        // Quit driver
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }

        extentTest.remove();
    }

    @AfterSuite
    public void suiteTearDown() {
        ExtentReportManager.flushReport();
    }

    /**
     * Setup Appium driver with capabilities
     */
    private void setupDriver() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();

        // Read configuration from properties file
        ConfigReader config = ConfigReader.getInstance();

        // Set desired capabilities
        options.setPlatformName(config.getProperty("platform.name"));
        options.setPlatformVersion(config.getProperty("platform.version"));
        options.setDeviceName(config.getProperty("device.name"));
        options.setApp(config.getProperty("app.path"));
        options.setAppPackage(config.getProperty("app.package"));
        options.setAppActivity(config.getProperty("app.activity"));
        options.setAutomationName(config.getProperty("automation.name"));
        options.setNoReset(Boolean.parseBoolean(config.getProperty("no.reset")));
        options.setFullReset(Boolean.parseBoolean(config.getProperty("full.reset")));
        options.setCapability("unicodeKeyboard", true);
        options.setCapability("resetKeyboard", true);

        // Additional options for better stability
        options.setNewCommandTimeout(Duration.ofSeconds(60));
        options.setAppWaitDuration(Duration.ofSeconds(30));
        options.setCapability("appium:ensureWebviewsHavePages", true);
        options.setCapability("appium:nativeWebScreenshot", true);
        options.setCapability("appium:connectHardwareKeyboard", true);

        // Initialize driver
        String appiumServerUrl = config.getProperty("appium.server.url");
        AndroidDriver androidDriver = new AndroidDriver(new URL(appiumServerUrl), options);

        // Set implicit wait
        androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.set(androidDriver);

        getExtentTest().info("Driver initialized successfully");
    }
}