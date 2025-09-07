package com.automation.utils;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for capturing and managing screenshots
 */
public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    static {
        // Create screenshots directory if it doesn't exist
        createDirectoryIfNotExists(SCREENSHOT_DIR);
    }

    /**
     * Capture screenshot and return the file path
     */
    public static String captureScreenshot(AppiumDriver driver, String testName) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String fileName = String.format("%s_%s.png", testName, timestamp);
            String destinationPath = SCREENSHOT_DIR + fileName;

            File destinationFile = new File(destinationPath);
            FileUtils.copyFile(sourceFile, destinationFile);

            System.out.println("Screenshot captured: " + destinationPath);
            return destinationPath;

        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot with custom file name
     */
    public static String captureScreenshot(AppiumDriver driver, String testName, String customName) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String fileName = String.format("%s_%s_%s.png", testName, customName, timestamp);
            String destinationPath = SCREENSHOT_DIR + fileName;

            File destinationFile = new File(destinationPath);
            FileUtils.copyFile(sourceFile, destinationFile);

            System.out.println("Screenshot captured: " + destinationPath);
            return destinationPath;

        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot as byte array
     */
    public static byte[] captureScreenshotAsBytes(AppiumDriver driver) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            return takesScreenshot.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot as bytes: " + e.getMessage());
            return null;
        }
    }

    /**
     * Create directory if it doesn't exist
     */
    private static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created directory: " + directoryPath);
            } else {
                System.err.println("Failed to create directory: " + directoryPath);
            }
        }
    }

    /**
     * Clean up old screenshots (older than specified days)
     */
    public static void cleanupOldScreenshots(int daysOld) {
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (screenshotDir.exists() && screenshotDir.isDirectory()) {
            File[] files = screenshotDir.listFiles();
            if (files != null) {
                long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60L * 60L * 1000L);

                for (File file : files) {
                    if (file.isFile() && file.lastModified() < cutoffTime) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            System.out.println("Deleted old screenshot: " + file.getName());
                        }
                    }
                }
            }
        }
    }
}