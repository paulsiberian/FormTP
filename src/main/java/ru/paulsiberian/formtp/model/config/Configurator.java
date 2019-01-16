package ru.paulsiberian.formtp.model.config;

import com.sun.javafx.PlatformUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

public class Configurator {

    private final static String APP_DATA_FOLDER = "ru.paulsiberian.formtp";
    private final static String USER_HOME_FOLDER = System.getProperty("user.home");
    private final static String CONFIG_PROPERTIES = "config.properties";
    private final static String LANGUAGES_LIST = "languages.list";

    public final static double MIN_WINDOW_WIDTH = 600;
    public final static double MIN_WINDOW_HEIGHT = 400;

    private Properties properties;
    private Locale locale;
    private ObservableList<String> languagesObservableList;
    private double windowWidth;
    private double windowHeight;
    private boolean windowMaximized;
    private File configPropertiesFile;
    private File languagesListFile;

    private static Configurator ourInstance = new Configurator();

    private Configurator() {

        String appDataPath = null;
        if (PlatformUtil.isWindows()) appDataPath = USER_HOME_FOLDER + "\\AppData\\Roaming\\" + APP_DATA_FOLDER + "\\";
        if (PlatformUtil.isUnix()) appDataPath = USER_HOME_FOLDER + "/.config/" + APP_DATA_FOLDER + "/";
        if (PlatformUtil.isMac()) appDataPath = USER_HOME_FOLDER + "/Library/" + APP_DATA_FOLDER + "/";

        configPropertiesFile = new File(appDataPath + CONFIG_PROPERTIES);
        languagesListFile = new File(appDataPath + LANGUAGES_LIST);
        File appDataFolder = null;
        if (appDataPath != null) appDataFolder = new File(appDataPath);
        if (appDataFolder != null) {
            if (!appDataFolder.exists()) {
                System.out.println("First run application");
                if (appDataFolder.mkdir()) {
                    System.out.println("App data folder was made");
                    makeConfigProperties();
                    makeLanguageList();
                } else System.out.println("Failed make app data folder");
            } else {
                try (InputStream stream = new FileInputStream(configPropertiesFile)) {
                    properties = new Properties();
                    properties.load(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                locale = new Locale(properties.getProperty("language"));
                windowWidth = Double.parseDouble(properties.getProperty("window.width"));
                windowHeight = Double.parseDouble(properties.getProperty("window.height"));
                windowMaximized = Boolean.parseBoolean(properties.getProperty("window.maximized"));

                ArrayList<String> list = new ArrayList<>();
                try (FileReader reader = new FileReader(languagesListFile);
                     BufferedReader bufferedReader = new BufferedReader(reader)
                ) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.isEmpty()) list.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                languagesObservableList = FXCollections.observableArrayList(list);
            }
        }


    }

    private void makeLanguageList() {
        languagesObservableList = FXCollections.observableArrayList("en", "ru");
        try (FileWriter writer = new FileWriter(languagesListFile);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)
        ) {
            for (String s : languagesObservableList) bufferedWriter.write(s + '\n');
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeConfigProperties() {
        properties = new Properties();
        windowWidth = MIN_WINDOW_WIDTH;
        windowHeight = MIN_WINDOW_HEIGHT;
        windowMaximized = false;
        locale = new Locale("ru");
        save();
    }

    public void save() {
        properties.setProperty("window.width", String.valueOf(windowWidth));
        properties.setProperty("window.height", String.valueOf(windowHeight));
        properties.setProperty("window.maximized", String.valueOf(windowMaximized));
        properties.setProperty("language", locale.toLanguageTag());
        try (FileOutputStream stream = new FileOutputStream(configPropertiesFile)) {
            properties.store(stream, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configurator getInstance() {
        return ourInstance;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(double windowHeight) {
        this.windowHeight = windowHeight;
    }

    public boolean isWindowMaximized() {
        return windowMaximized;
    }

    public void setWindowMaximized(boolean windowMaximized) {
        this.windowMaximized = windowMaximized;
    }

    public ObservableList<String> getLanguageList() {
        return languagesObservableList;
    }
}
