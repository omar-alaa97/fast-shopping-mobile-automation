package com.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manager class for ExtentReports configuration and operations
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static final String REPORTS_DIR = "test-output/extent-reports/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Initialize ExtentReports
     */
    public static void initializeReport() {
        if (extent == null) {
            createDirectory();

            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String reportPath = REPORTS_DIR + "ExtentReport_" + timestamp + ".html";

            sparkReporter = new ExtentSparkReporter(reportPath);
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            configureReport();
        }
    }

    /**
     * Configure report settings
     */
    private static void configureReport() {
        // Spark Reporter Configuration
        sparkReporter.config().setDocumentTitle("Fast Shopping Mobile App - Test Report");
        sparkReporter.config().setReportName("Mobile Automation Test Results");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        // ExtentReports Configuration
        extent.setSystemInfo("Application", "Fast Shopping Mobile App");
        extent.setSystemInfo("Test Framework", "Appium + TestNG");
        extent.setSystemInfo("Platform", "Android");
        extent.setSystemInfo("Automation Engineer", "Test Automation Team");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
    }

    /**
     * Create a new test in the report
     */
    public static ExtentTest createTest(String testName) {
        return extent.createTest(testName);
    }

    /**
     * Create a new test with description
     */
    public static ExtentTest createTest(String testName, String description) {
        return extent.createTest(testName, description);
    }

    /**
     * Flush the report
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("Extent Report generated successfully!");
        }
    }

    /**
     * Get ExtentReports instance
     */
    public static ExtentReports getExtentReports() {
        return extent;
    }

    /**
     * Create reports directory if it doesn't exist
     */
    private static void createDirectory() {
        java.io.File directory = new java.io.File(REPORTS_DIR);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created reports directory: " + REPORTS_DIR);
            } else {
                System.err.println("Failed to create reports directory: " + REPORTS_DIR);
            }
        }
    }
}