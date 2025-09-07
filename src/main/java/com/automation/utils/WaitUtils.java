package com.automation.utils;

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
    public static void waitForElementToBePresent(AppiumDriver driver, By locator) {
        waitForElementToBePresent(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be present with custom timeout
     */
    public static void waitForElementToBePresent(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Safe click with wait for element to be clickable
     */
    public static void safeClick(AppiumDriver driver, By locator) {
        WebElement element = waitForElementToBeClickable(driver, locator);
        element.click();
    }
}