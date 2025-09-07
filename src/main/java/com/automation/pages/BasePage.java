package com.automation.pages;

import com.automation.base.BaseTest;
import com.automation.utils.WaitUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.interactions.Actions;


/**
 * Base page class with common page operations
 */
public class BasePage {

    protected AppiumDriver driver;
    protected Random random;
    protected String listName;

    public BasePage() {
        this.driver = BaseTest.getDriver();
        this.random = new Random();
    }

    /**
     * Wait and click element
     */
    protected void click(By locator) {
        WaitUtils.safeClick(driver, locator);
        BaseTest.getExtentTest().info("Clicked on element: " + locator.toString());
    }

    /**
     * Wait and send keys to element
     */
    protected void sendKeys(By locator, String text) {
        WebElement element = WaitUtils.waitForElementToBeVisible(driver, locator);

        // Use Actions class for more reliable input
        Actions actions = new Actions(driver);
        actions.click(element).perform();

        // Small wait
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Clear and type
        element.clear();
        actions.sendKeys(text).perform();

        BaseTest.getExtentTest().info("Entered text '" + text + "' in element: " + locator.toString());
    }

    /**
     * Get text from element
     */
    protected String getText(By locator) {
        String text = WaitUtils.getTextSafely(driver, locator);
        BaseTest.getExtentTest().info("Retrieved text '" + text + "' from element: " + locator.toString());
        return text;
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(driver, locator, 5);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is present
     */
    protected boolean isElementPresent(By locator) {
        return WaitUtils.isElementPresent(driver, locator);
    }

    /**
     * Get list of elements
     */
    protected List<WebElement> getElements(By locator) {
        WaitUtils.waitForElementToBePresent(driver, locator);
        return driver.findElements(locator);
    }

    /**
     * Generate random string for test data
     */
    public String generateRandomString(String prefix) {
        return prefix + "_" + random.nextInt(1000);
    }

    /**
     * Wait for page to load
     */
    protected void waitForPageToLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}