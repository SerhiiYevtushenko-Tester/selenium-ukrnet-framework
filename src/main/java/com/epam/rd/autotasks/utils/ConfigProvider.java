package com.epam.rd.autotasks.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {
    private static Properties properties = new Properties();
    private static Properties secretProperties = new Properties();

    static {
        String env = System.getProperty("env", "qa");
        String configPath = env + ".properties";

        try (InputStream envStream = ConfigProvider.class.getClassLoader().getResourceAsStream(configPath);
             InputStream secretStream = ConfigProvider.class.getClassLoader().getResourceAsStream("secret.properties")) {

            if (envStream != null) {
                properties.load(envStream);
            } else {
                throw new RuntimeException("Configuration file not found: " + configPath);
            }

            if (secretStream != null) {
                secretProperties.load(secretStream);
            } else {
                System.out.println("Warning: secret.properties not found! Make sure to create it.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration files", e);
        }
    }

    public static String getProperty(String key) {
        return secretProperties.getProperty(key, properties.getProperty(key));
    }
}