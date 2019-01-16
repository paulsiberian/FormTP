package ru.paulsiberian.formtp.model.dialog;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.paulsiberian.formtp.model.entity.TrainingProgram;

import java.util.ResourceBundle;

public class TrainingProgramDialog extends Dialog<VBox, TrainingProgram> {

    public TrainingProgramDialog(ResourceBundle resources) {
        super(resources.getString("dialog.training_program.title.add"), resources);
    }

    public TrainingProgramDialog(ResourceBundle resources, TrainingProgram trainingProgram) {
        super(resources.getString("dialog.training_program.title.edit"), resources);
        if (trainingProgram != null) {
            Label points = (Label) content.getChildren().get(0);
            Label userCount = (Label) content.getChildren().get(1);
            Label cost = (Label) content.getChildren().get(2);
            ((TextField) points.getGraphic()).setText(String.valueOf(trainingProgram.getPoints()));
            ((TextField) userCount.getGraphic()).setText(String.valueOf(trainingProgram.getUserCount()));
            ((TextField) cost.getGraphic()).setText(String.valueOf(trainingProgram.getCost()));
        }
    }

    @Override
    VBox content() {
        Label pointsText = createTextField(resources.getString("dialog.training_program.lable.points"), "0");
        Label userCountText = createTextField(resources.getString("dialog.training_program.lable.user_count"), "0");
        Label costText = createTextField(resources.getString("dialog.training_program.lable.cost"), "0.0");
        VBox content = new VBox(pointsText, userCountText, costText);
        content.setSpacing(5);
        content.setAlignment(Pos.CENTER_RIGHT);
        return content;
    }

    @Override
    public void setData() {
        data = new TrainingProgram(
                Integer.parseInt(((TextField) ((Label) content.getChildren().get(0)).getGraphic()).getText()),
                Integer.parseInt(((TextField) ((Label) content.getChildren().get(1)).getGraphic()).getText()),
                Double.parseDouble(((TextField) ((Label) content.getChildren().get(2)).getGraphic()).getText())
        );
    }

}
