package ru.paulsiberian.formtp.model.bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class XMLResourceBundle extends ResourceBundle {

    private Properties properties;

    public XMLResourceBundle(InputStream stream) throws IOException {
        properties = new Properties();
        properties.loadFromXML(stream);
    }

    @Override
    protected Object handleGetObject(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(properties.stringPropertyNames());
    }
}
