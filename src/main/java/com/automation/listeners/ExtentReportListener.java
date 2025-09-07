package com.automation.listeners;

import com.automation.utils.ExtentReportManager;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for ExtentReports integration
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        System.out.println("Test Suite started: " + suite.getName());
        ExtentReportManager.initializeReport();
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("Test Suite finished: " + suite.getName());
        ExtentReportManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getMethod().getMethodName());
        System.out.println("Failure reason: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test skipped: " + result.getMethod().getMethodName());
    }
}