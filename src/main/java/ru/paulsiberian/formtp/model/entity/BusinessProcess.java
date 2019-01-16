package ru.paulsiberian.formtp.model.entity;

import java.util.ArrayList;

public class BusinessProcess extends ArrayList<TrainingProgram> {

    private int minUserCount;

    private ArrayList<Solution> solutions;

    public BusinessProcess(int minUserCount) {
        super();
        this.minUserCount = minUserCount;
        solutions = new ArrayList<>();
    }

    public void setMinUserCount(int minUserCount) {
        this.minUserCount = minUserCount;
    }

    public int getMinUserCount() {
        return minUserCount;
    }

    public ArrayList<Solution> getSolutions() {
        return solutions;
    }

}
