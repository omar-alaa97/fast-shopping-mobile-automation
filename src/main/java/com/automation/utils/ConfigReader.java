package com.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton class to read configuration properties
 */
public class ConfigReader {

    private static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigReader
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("config.properties file not found in classpath");
            }

        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties: " + e.getMessage());
        }
    }

    /**
     * Get property value by key
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value;
    }

    /**
     * Get property value with default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get integer property
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    /**
     * Get boolean property
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}