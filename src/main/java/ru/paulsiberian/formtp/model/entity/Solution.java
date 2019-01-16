package ru.paulsiberian.formtp.model.entity;

public class Solution {

    private String key;
    private TrainingProgram value;
    private final static String SEP = "-";

    public Solution(String key, TrainingProgram value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String[] getArrayKey() {
        return key.split(SEP);
    }

    public TrainingProgram getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
