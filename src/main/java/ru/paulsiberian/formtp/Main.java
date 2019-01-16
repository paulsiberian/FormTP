package ru.paulsiberian.formtp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.paulsiberian.formtp.model.bundle.ResourceBundleControl;
import ru.paulsiberian.formtp.model.config.Configurator;

import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application {

    private void visitAuthorPage(ActionEvent event) {
        getHostServices().showDocument("https://github.com/paulsiberian");
    }

    private void exit(WindowEvent event) {
        Stage stage = (Stage) event.getSource();
        if (!stage.isMaximized()) {
            Configurator.getInstance().setWindowWidth(stage.getWidth());
            Configurator.getInstance().setWindowHeight(stage.getHeight());
        }
        Configurator.getInstance().setWindowMaximized(stage.isMaximized());
        Configurator.getInstance().save();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        ResourceBundle resources = ResourceBundle.getBundle("bundles.Bundle", Configurator.getInstance().getLocale(), new ResourceBundleControl());
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(classLoader.getResource("layouts/MainWindow.fxml")));
        loader.setResources(resources);
        Parent root = loader.load();
        Separator separator = new Separator(Orientation.HORIZONTAL);
        Hyperlink copyright = new Hyperlink("© Храпунов П.Н., 2018");
        copyright.setOnAction(this::visitAuthorPage);
        ((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().addAll(separator, copyright);
        primaryStage.setTitle(resources.getString("main_window.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(Configurator.MIN_WINDOW_WIDTH);
        primaryStage.setMinHeight(Configurator.MIN_WINDOW_HEIGHT);
        primaryStage.setWidth(Configurator.getInstance().getWindowWidth());
        primaryStage.setHeight(Configurator.getInstance().getWindowHeight());
        primaryStage.setMaximized(Configurator.getInstance().isWindowMaximized());
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(this::exit);
        primaryStage.show();
    }
}
