package com.solutions.denisovich.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig {
    private static final Logger LOG = LogManager.getLogger(PropertiesConfig.class);
    private final Properties properties = new Properties();

    public String  getProperty(String name){
        return properties.getProperty(name);
    }

    public void loadPropertiesFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
