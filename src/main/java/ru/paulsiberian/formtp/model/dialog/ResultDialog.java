package ru.paulsiberian.formtp.model.dialog;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.paulsiberian.formtp.model.entity.Solution;

import java.util.ResourceBundle;

public class ResultDialog extends Dialog<VBox, Solution> {

    private TextArea textArea;

    public ResultDialog(Solution solution, ResourceBundle resources) {
        super(resources.getString("dialog.result.title"), resources, ButtonType.CLOSE);
        data = solution;
    }

    private String getResultText(Solution s) {

        StringBuilder value = new StringBuilder();
        String[] x = s.getArrayKey();

        value.append("q=").append(s.getValue().getPoints()).append("; ")
                .append("k=").append(s.getValue().getUserCount()).append("; ")
                .append("c=").append(s.getValue().getCost()).append(".\n");

        for (int i = 0; i < x.length; i++) {
            value.append('x').append(i).append('=').append(x[i]).append('\n');
        }

        return value.toString();
    }

    @Override
    VBox content() {
        textArea = new TextArea();
        Label label = new Label(resources.getString("dialog.result.message"));
        VBox content = new VBox(label, textArea);
        content.setSpacing(5);
        content.setAlignment(Pos.CENTER);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        textArea.setEditable(false);
        textArea.setPrefWidth(300);
        return content;
    }

    @Override
    public void setData() {
        textArea.appendText(getResultText(data));
    }

}
