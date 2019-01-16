package ru.paulsiberian.formtp.model.bundle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleControl extends ResourceBundle.Control {

    private final static String XML = "xml";
    private final static List<String> SINGLETON_LIST = Collections.singletonList(XML);

    @Override
    public List<String> getFormats(String baseName) {
        return SINGLETON_LIST;
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IOException {

        if ((baseName == null) || (locale == null) || (format == null) || (loader == null))
            throw new IllegalArgumentException("baseName, locale, format и loader не должны быть null");

        if (!format.equals(XML))
            throw new IllegalArgumentException("format должен быть xml");

        final String bundleName = toBundleName(baseName, locale);
        final String resourceName = toResourceName(bundleName, format);
        final URL url = loader.getResource(resourceName);
        if (url == null) return null;
        final URLConnection connection = url.openConnection();
        if (connection == null) return null;
        if (reload) connection.setUseCaches(false);

        try (
                final InputStream stream = connection.getInputStream();
                final BufferedInputStream buffer = new BufferedInputStream(stream)
        ) {
            final ResourceBundle bundle = new XMLResourceBundle(buffer);
            return bundle;
        }
    }
}
