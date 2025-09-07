package com.automation.utils;

import com.automation.base.BaseTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Utility class for custom waits and element interactions
 */
public class WaitUtils {

    private static final int DEFAULT_TIMEOUT = 30;
    private static final int POLLING_INTERVAL = 2;

    /**
     * Wait for element to be visible and return it
     */
    public static WebElement waitForElementToBeVisible(AppiumDriver driver, By locator) {
        return waitForElementToBeVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be visible with custom timeout
     */
    public static WebElement waitForElementToBeVisible(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable and return it
     */
    public static WebElement waitForElementToBeClickable(AppiumDriver driver, By locator) {
        return waitForElementToBeClickable(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be clickable with custom timeout
     */
    public static WebElement waitForElementToBeClickable(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be present
     */
    public static WebElement waitForElementToBePresent(AppiumDriver driver, By locator) {
        return waitForElementToBePresent(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be present with custom timeout
     */
    public static WebElement waitForElementToBePresent(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to disappear
     */
    public static boolean waitForElementToDisappear(AppiumDriver driver, By locator) {
        return waitForElementToDisappear(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to disappear with custom timeout
     */
    public static boolean waitForElementToDisappear(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextToBePresentInElement(AppiumDriver driver, By locator, String text) {
        return waitForTextToBePresentInElement(driver, locator, text, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for text to be present in element with custom timeout
     */
    public static boolean waitForTextToBePresentInElement(AppiumDriver driver, By locator, String text, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Check if element is present without waiting
     */
    public static boolean isElementPresent(AppiumDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Safe click with wait for element to be clickable
     */
    public static void safeClick(AppiumDriver driver, By locator) {
        WebElement element = waitForElementToBeClickable(driver, locator);
        element.click();
    }

    /**
     * Safe send keys with wait for element to be visible
     */
    public static void safeSendKeys(AppiumDriver driver, By locator, String text) {
        WebElement element = WaitUtils.waitForElementToBeVisible(driver, locator);

        // Clear any existing text first
        element.clear();

        // Add a small wait after clearing
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        element.sendKeys(text);
        BaseTest.getExtentTest().info("Entered text '" + text + "' in element: " + locator.toString());
    }

    /**
     * Get text safely with wait
     */
    public static String getTextSafely(AppiumDriver driver, By locator) {
        WebElement element = waitForElementToBeVisible(driver, locator);
        return element.getText();
    }
}