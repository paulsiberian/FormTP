package ru.paulsiberian.formtp.model.task;

import javafx.concurrent.Task;
import ru.paulsiberian.formtp.model.entity.BusinessProcess;
import ru.paulsiberian.formtp.model.entity.Solution;
import ru.paulsiberian.formtp.model.tool.Solutions;

import java.util.Arrays;
import java.util.ResourceBundle;

public class FindingBestSolutionTask extends Task<Solution> {

    private BusinessProcess[] businessProcesses;
    private double allocatedMoney;
    private ResourceBundle resources;

    public FindingBestSolutionTask(BusinessProcess[] businessProcesses, double allocatedMoney, ResourceBundle resources) {
        this.businessProcesses = businessProcesses;
        this.allocatedMoney = allocatedMoney;
        this.resources = resources;
    }

    @Override
    protected Solution call() {

        updateMessage(resources.getString("task_message.business_processes_count") + ": " + businessProcesses.length + '\n');

        int k = 0;

        for (BusinessProcess bp : businessProcesses) {

            updateMessage(String.format("%s[%d]:\n%s", resources.getString("task_message.business_process"), k, bp));

            Solutions.find(bp);

            updateMessage("-\t " + resources.getString("task_message.all_solutions") + ":\n-\t\t" + bp.getSolutions());

            Solutions.removeAllInvalid(bp, allocatedMoney);

            updateMessage("-\t" +resources.getString("task_message.valid_solutions") + ":\n-\t\t" + bp.getSolutions() + '\n');

            k++;
        }

        Solution[] solutions = Solutions.findAllValid(businessProcesses, allocatedMoney);

        updateMessage(resources.getString("task_message.all_valid_solutions") + ":\n\t" + Arrays.toString(solutions));

        Solution bestSolution = Solutions.findBest(solutions);

        updateMessage("\n" + resources.getString("task_message.best_solution") + ": " + bestSolution + '\n');

        return bestSolution;
    }

}
