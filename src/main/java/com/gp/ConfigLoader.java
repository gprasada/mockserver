package com.gp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    public ConfigLoader() {
    }

    Properties getProperties() throws IOException {
        String configFile = System.getProperty("config");
        if (configFile == null) {
            configFile = "config.properties";
        }
        System.out.println("config file : " + configFile);
        Properties properties = new Properties();
        properties.load(new FileInputStream(configFile));
        return properties;
    }
}