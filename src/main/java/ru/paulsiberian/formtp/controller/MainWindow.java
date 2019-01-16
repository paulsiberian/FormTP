package ru.paulsiberian.formtp.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.paulsiberian.formtp.model.config.Configurator;
import ru.paulsiberian.formtp.model.dialog.*;
import ru.paulsiberian.formtp.model.dialog.Dialog;
import ru.paulsiberian.formtp.model.entity.BusinessProcess;
import ru.paulsiberian.formtp.model.entity.TrainingProgram;
import ru.paulsiberian.formtp.model.task.FindingBestSolutionTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    private Button languageButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField allocatedMoneyTextField;
    @FXML
    private Button startButton;
    @FXML
    private TreeView<String> processesTreeView;
    @FXML
    private ProgressIndicator progressIndicator;

    private ResourceBundle resources;
    private TreeItem<String> rootTreeItem;
    private FindingBestSolutionTask task;
    private ArrayList<BusinessProcess> businessProcesses;
    private TreeItem<String> selectedTreeItem;
    private int selectedIndex;
    private IntegerProperty selectedLevel;

    private void start(ActionEvent event) {
        for (BusinessProcess bp : businessProcesses) {
            if (bp.isEmpty()) {
                int index = businessProcesses.indexOf(bp);
                String message = "Бизнес-процесс[" + index + "] не содержит программ обучения!\nНажмите \"Продолжить\" для удаления бизнес-процесса[" + index + "].";
                Alert dialog = new Alert(Alert.AlertType.WARNING, message, ButtonType.NEXT, ButtonType.CANCEL);
                ButtonType button = dialog.showAndWait().get();
                if (button == ButtonType.NEXT) {
                    businessProcesses.remove(bp);
                } else if (button == ButtonType.CANCEL) {
                    return;
                }
            } else bp.getSolutions().clear();
        }
        double allocatedMoney = Double.parseDouble(allocatedMoneyTextField.getText());
        task = new FindingBestSolutionTask(businessProcesses.toArray(new BusinessProcess[0]), allocatedMoney, resources);
        task.setOnSucceeded(this::result);
        startButton.setDisable(true);
        processesTreeView.setDisable(true);
        progressIndicator.setVisible(true);
        progressIndicator.progressProperty().unbind();
        progressIndicator.setProgress(0);
        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    private void add(ActionEvent event) {
        Dialog dialog;
        switch (selectedLevel.getValue()) {
            case 0 :
                dialog = new BusinessProcessDialog(resources);
                if (dialog.showDialog()) {
                    dialog.setData();
                    int minUserCount = ((BusinessProcessDialog) dialog).getData();
                    businessProcesses.add(new BusinessProcess(minUserCount));
                    rootTreeItem.getChildren().add(
                            new TreeItem<>(businessProcesses.size() + " "
                                    + resources.getString("main_window.tree_item.business_process")
                                    + " ( k* = " + minUserCount + " )"
                            )
                    );
                }
                break;
            case 1 :
                dialog = new TrainingProgramDialog(resources);
                if (dialog.showDialog()) {
                    dialog.setData();
                    int points = ((TrainingProgramDialog) dialog).getData().getPoints();
                    int userCount = ((TrainingProgramDialog) dialog).getData().getUserCount();
                    double cost = ((TrainingProgramDialog) dialog).getData().getCost();
                    BusinessProcess businessProcess = businessProcesses.get(selectedIndex);
                    businessProcess.add(new TrainingProgram(points, userCount, cost));
                    selectedTreeItem.getChildren().add(
                            new TreeItem<>(businessProcess.size() + " "
                                    + resources.getString("main_window.tree_item.training_program")
                                    + " ( q = " + points
                                    + " ; k = " + userCount
                                    + " ; c = " + cost + " )"
                            )
                    );
                }
                break;
        }
    }

    private void edit(ActionEvent event) {
        Dialog dialog;
        switch (selectedLevel.getValue()) {
            case 1 :
                BusinessProcess businessProcess = businessProcesses.get(selectedIndex);
                dialog = new BusinessProcessDialog(resources, businessProcess);
                if (dialog.showDialog()) {
                    dialog.setData();
                    int minUserCount = ((BusinessProcessDialog) dialog).getData();
                    businessProcess.setMinUserCount(minUserCount);
                    String[] oldValue = selectedTreeItem.getValue().split(" ");
                    oldValue[oldValue.length - 2] = String.valueOf(minUserCount);
                    StringBuilder newValue = new StringBuilder();
                    for (String s : oldValue) newValue.append(s).append(' ');
                    selectedTreeItem.setValue(newValue.deleteCharAt(newValue.length() - 1).toString());
                }
                break;
            case 2 :
                TreeItem<String> parent = selectedTreeItem.getParent();
                int parentIndex = parent.getParent().getChildren().indexOf(parent);
                TrainingProgram trainingProgram = businessProcesses.get(parentIndex).get(selectedIndex);
                dialog = new TrainingProgramDialog(resources, trainingProgram);
                if (dialog.showDialog()) {
                    dialog.setData();
                    int points = ((TrainingProgramDialog) dialog).getData().getPoints();
                    int userCount = ((TrainingProgramDialog) dialog).getData().getUserCount();
                    double cost = ((TrainingProgramDialog) dialog).getData().getCost();
                    trainingProgram.setPoints(points);
                    trainingProgram.setUserCount(userCount);
                    trainingProgram.setCost(cost);
                    String[] oldValue = selectedTreeItem.getValue().split(" ");
                    oldValue[5] = String.valueOf(points);
                    oldValue[9] = String.valueOf(userCount);
                    oldValue[13] = String.valueOf(cost);
                    StringBuilder newValue = new StringBuilder();
                    for (String s : oldValue) newValue.append(s).append(' ');
                    selectedTreeItem.setValue(newValue.deleteCharAt(newValue.length() - 1).toString());
                }
                break;
        }
    }

    private void delete(ActionEvent event) {
        TreeItem<String> parent = selectedTreeItem.getParent();
        switch (selectedLevel.getValue()) {
            case 1 :
                businessProcesses.remove(selectedIndex);
                break;
            case 2 :
                int parentIndex = parent.getParent().getChildren().indexOf(parent);
                businessProcesses.get(parentIndex).remove(selectedIndex);
                break;
        }
        parent.getChildren().remove(selectedIndex);
        for (TreeItem<String> item : parent.getChildren()) {
            String[] itemValue = item.getValue().split(" ", 2);
            int oldIndex = Integer.parseInt(itemValue[0]);
            int actualIndex = item.getParent().getChildren().indexOf(item) + 1;
            if (oldIndex != actualIndex) item.setValue(actualIndex + " " + itemValue[1]);
        }
    }

    private void selection(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
        selectedTreeItem = newValue;
        selectedLevel.setValue(processesTreeView.getTreeItemLevel(selectedTreeItem));
        if (selectedTreeItem != rootTreeItem) selectedIndex = selectedTreeItem.getParent().getChildren().indexOf(selectedTreeItem);
        else selectedIndex = 0;
    }

    private void result(WorkerStateEvent event) {
        startButton.setDisable(false);
        processesTreeView.setDisable(false);
        progressIndicator.setVisible(false);
        Dialog dialog = new ResultDialog(task.getValue(), resources);
        dialog.setData();
        dialog.showDialog();
        task.cancel(true);
    }

    private void changeLanguage(ActionEvent event) {
        Dialog dialog = new LanguageDialog(resources);
        if (dialog.showDialog()) {
            dialog.setData();
            Configurator.getInstance().setLocale(((LanguageDialog) dialog).getData());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        selectedLevel = new SimpleIntegerProperty(0);
        businessProcesses = new ArrayList<>();
        rootTreeItem = new TreeItem<>(resources.getString("main_window.tree_item.root"));
        rootTreeItem.setExpanded(true);
        processesTreeView.setRoot(rootTreeItem);
        processesTreeView.getSelectionModel().selectedItemProperty().addListener(this::selection);
        startButton.setOnAction(this::start);
        addButton.setOnAction(this::add);
        editButton.setOnAction(this::edit);
        deleteButton.setOnAction(this::delete);
        languageButton.setOnAction(this::changeLanguage);
        progressIndicator.setVisible(false);
        addButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedLevel.getValue() == 2, selectedLevel));
        editButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedLevel.getValue() == 0, selectedLevel));
        deleteButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedLevel.getValue() == 0, selectedLevel));
    }

}
