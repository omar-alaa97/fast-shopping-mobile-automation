package com.automation.listeners;

import com.automation.base.BaseTest;
import com.automation.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for automatic screenshot capture
 */
public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("Test failed: " + testName + " - Capturing screenshot...");

        try {
            if (BaseTest.getDriver() != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(
                        BaseTest.getDriver(),
                        testName
                );

                if (screenshotPath != null) {
                    System.setProperty("screenshot.path", screenshotPath);
                    System.out.println("Screenshot saved: " + screenshotPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }
}