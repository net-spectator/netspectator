package utils.converter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesOperator {
    public static Properties returnProperties(String path) {
        File file = new File(path);
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(new FileReader(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static boolean propertiesWriter(Properties properties, String path) {
        try {
            properties.store(new FileWriter(path), "update");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
