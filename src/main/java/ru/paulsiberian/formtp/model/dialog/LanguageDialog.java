package ru.paulsiberian.formtp.model.dialog;

import javafx.scene.control.*;
import ru.paulsiberian.formtp.model.config.Configurator;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageDialog extends Dialog<Label, Locale> {

    private ChoiceBox<String> choiceBox;

    public LanguageDialog(ResourceBundle resources) {
        super(resources.getString("dialog.language"), resources, ButtonType.APPLY, ButtonType.CANCEL);
        choiceBox = (ChoiceBox<String>) content.getGraphic();
        choiceBox.setItems(Configurator.getInstance().getLanguageList());
        choiceBox.setValue(resources.getLocale().toLanguageTag());
    }

    @Override
    Label content() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(60);
        Label label = new Label(resources.getString("dialog.language"), choiceBox);
        label.setContentDisplay(ContentDisplay.RIGHT);
        return label;
    }

    @Override
    public void setData() {
        data = new Locale(choiceBox.getValue());
    }
}
