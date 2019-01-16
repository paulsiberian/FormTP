package ru.paulsiberian.formtp.model.dialog;

import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.ResourceBundle;

public abstract class Dialog<C, D> extends Alert {

    C content;
    D data;
    ResourceBundle resources;

    Dialog(String title, ResourceBundle resources, ButtonType... buttonTypes) {
        super(Alert.AlertType.NONE, null, buttonTypes);
        setTitle(title);
        this.resources = resources;
        content = content();
        getDialogPane().setContent((Node) content);
    }

    Dialog(String title, ResourceBundle resources) {
        this(title, resources, ButtonType.OK, ButtonType.CANCEL);
    }

    static Label createTextField(String labelText, String defaultValue) {
        TextField textField = new TextField(defaultValue);
        textField.setPrefWidth(60);
        Label label = new Label(labelText, textField);
        label.setContentDisplay(ContentDisplay.RIGHT);
        return label;
    }

    abstract C content();

    public abstract void setData();

    public C getContent() {
        return content;
    }

    public D getData() {
        return data;
    }

    public boolean showDialog() {
        ButtonType buttonType = showAndWait().get();
        return buttonType == ButtonType.FINISH | buttonType == ButtonType.OK | buttonType == ButtonType.APPLY;
    }
}
