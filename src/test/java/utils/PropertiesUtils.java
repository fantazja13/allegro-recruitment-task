package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static Properties properties;

    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    static {
        properties = new Properties();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("data.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot load properties file");
        }
    }
}
