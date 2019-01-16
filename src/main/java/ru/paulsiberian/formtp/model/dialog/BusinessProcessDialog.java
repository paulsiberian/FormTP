package ru.paulsiberian.formtp.model.dialog;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.paulsiberian.formtp.model.entity.BusinessProcess;

import java.util.ResourceBundle;

public class BusinessProcessDialog extends Dialog<Label, Integer> {

    public BusinessProcessDialog(ResourceBundle resources) {
        super(resources.getString("dialog.business_process.title.add"), resources);
    }

    public BusinessProcessDialog(ResourceBundle resource, BusinessProcess businessProcess) {
        super(resource.getString("dialog.business_process.title.edit"), resource);
        if (businessProcess != null)
            ((TextField) content.getGraphic()).setText(String.valueOf(businessProcess.getMinUserCount()));
    }

    @Override
    Label content() {
        return createTextField(resources.getString("dialog.business_process.lable.min_user_count"), "0");
    }

    @Override
    public void setData() {
        data = Integer.parseInt(((TextField) content.getGraphic()).getText());
    }

}
