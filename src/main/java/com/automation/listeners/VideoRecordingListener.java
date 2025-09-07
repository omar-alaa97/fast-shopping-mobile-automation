package com.automation.listeners;

import com.automation.base.BaseTest;
import com.automation.utils.ConfigReader;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Enhanced TestNG listener for screen recording successful test cases
 * Integrates with existing framework and configuration
 */
public class VideoRecordingListener implements ITestListener {

    private Process recordingProcess;
    private String currentTestName;
    private String currentDeviceRecordingPath;
    private final boolean recordingEnabled;

    private static final String RECORDINGS_DIR = "test-output/recordings/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public VideoRecordingListener() {
        // Check if recording is enabled in config
        this.recordingEnabled = isRecordingEnabled();

        if (recordingEnabled) {
            createDirectoryIfNotExists();
            System.out.println("Video recording enabled for successful tests");
        } else {
            System.out.println("Video recording disabled");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (!recordingEnabled) return;

        currentTestName = result.getMethod().getMethodName();

        try {
            if (isAdbAvailable() && isDeviceConnected()) {
                startScreenRecording(currentTestName);
                BaseTest.getExtentTest().info("Screen recording started for: " + currentTestName);
            } else {
                System.out.println("ADB not available or device not connected - skipping recording");
            }
        } catch (Exception e) {
            System.err.println("Failed to start screen recording: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (!recordingEnabled) return;

        System.out.println("Test PASSED - saving recording: " + currentTestName);

        try {
            stopAndSaveRecording(currentTestName);
            BaseTest.getExtentTest().info("Screen recording saved for successful test");
        } catch (Exception e) {
            System.err.println("Failed to save successful test recording: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (!recordingEnabled) return;

        System.out.println("Test FAILED - discarding recording: " + currentTestName);

        try {
            stopAndDiscardRecording();
        } catch (Exception e) {
            System.err.println("Failed to handle failed test recording: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (!recordingEnabled) return;

        System.out.println("Test SKIPPED - discarding recording: " + result.getMethod().getMethodName());

        try {
            stopAndDiscardRecording();
        } catch (Exception e) {
            System.err.println("Failed to handle skipped test recording: " + e.getMessage());
        }
    }

    /**
     * Start screen recording using ADB
     */
    private void startScreenRecording(String testName) throws Exception {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        currentDeviceRecordingPath = "/sdcard/SUCCESS_" + testName + "_" + timestamp + ".mp4";

        // Stop any existing recordings first
        stopExistingRecordings();

        // Start new recording
        ProcessBuilder processBuilder = new ProcessBuilder(
                "adb", "shell", "screenrecord",
                "--time-limit", "300", // 5 minutes max per recording
                "--bit-rate", "6000000", // 6Mbps for good quality
                "--size", "720x1280", // Optimize size for better performance
                currentDeviceRecordingPath
        );

        recordingProcess = processBuilder.start();

        // Wait a moment to ensure recording starts
        Thread.sleep(1000);

        System.out.println("Screen recording started: " + currentDeviceRecordingPath);
    }

    /**
     * Stop recording and save for successful test
     */
    private void stopAndSaveRecording(String testName) throws Exception {
        if (recordingProcess != null && recordingProcess.isAlive()) {
            // Stop recording gracefully
            recordingProcess.destroy();
            recordingProcess.waitFor(5, TimeUnit.SECONDS);

            // Wait for file to be written to device
            Thread.sleep(2000);

            // Pull recording from device
            pullRecordingFromDevice(testName);

            recordingProcess = null;
            currentDeviceRecordingPath = null;
        }
    }

    /**
     * Stop recording and discard for failed/skipped tests
     */
    private void stopAndDiscardRecording() throws Exception {
        if (recordingProcess != null && recordingProcess.isAlive()) {
            recordingProcess.destroy();
            recordingProcess.waitFor(3, TimeUnit.SECONDS);

            // Clean up the recording from device
            if (currentDeviceRecordingPath != null) {
                ProcessBuilder cleanBuilder = new ProcessBuilder("adb", "shell", "rm", "-f", currentDeviceRecordingPath);
                cleanBuilder.start();
            }

            recordingProcess = null;
            currentDeviceRecordingPath = null;
        }
    }

    /**
     * Pull successful test recording from device
     */
    private void pullRecordingFromDevice(String testName) throws Exception {
        if (currentDeviceRecordingPath == null) return;

        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String localPath = RECORDINGS_DIR + "SUCCESS_" + testName + "_" + timestamp + ".mp4";

        // Pull the recording
        ProcessBuilder pullBuilder = new ProcessBuilder("adb", "pull", currentDeviceRecordingPath, localPath);
        Process pullProcess = pullBuilder.start();

        if (pullProcess.waitFor(10, TimeUnit.SECONDS) && pullProcess.exitValue() == 0) {
            System.out.println("SUCCESS: Recording saved to: " + localPath);

            // Clean up from device
            ProcessBuilder cleanBuilder = new ProcessBuilder("adb", "shell", "rm", "-f", currentDeviceRecordingPath);
            cleanBuilder.start();

        } else {
            System.err.println("Failed to pull recording from device");
        }
    }

    /**
     * Stop any existing screen recordings
     */
    private void stopExistingRecordings() throws Exception {
        ProcessBuilder killBuilder = new ProcessBuilder("adb", "shell", "pkill", "-f", "screenrecord");
        Process killProcess = killBuilder.start();
        killProcess.waitFor(2, TimeUnit.SECONDS);
    }

    /**
     * Check if recording is enabled in configuration
     */
    private boolean isRecordingEnabled() {
        try {
            ConfigReader config = ConfigReader.getInstance();
            return config.getBooleanProperty("video.recording.enabled");
        } catch (Exception e) {
            // Default to false if config not found
            return false;
        }
    }

    /**
     * Create directory if it doesn't exist
     */
    private static void createDirectoryIfNotExists() {
        File directory = new File(VideoRecordingListener.RECORDINGS_DIR);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created recordings directory: " + VideoRecordingListener.RECORDINGS_DIR);
            }
        }
    }

    /**
     * Check if ADB is available
     */
    private boolean isAdbAvailable() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("adb", "version");
            Process process = processBuilder.start();
            return process.waitFor(3, TimeUnit.SECONDS) && process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if device is connected
     */
    private boolean isDeviceConnected() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("adb", "devices");
            Process process = processBuilder.start();

            if (process.waitFor(3, TimeUnit.SECONDS) && process.exitValue() == 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.contains("\tdevice")) { // Connected device
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}