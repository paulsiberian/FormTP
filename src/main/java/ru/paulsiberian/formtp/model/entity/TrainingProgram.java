package ru.paulsiberian.formtp.model.entity;

public class TrainingProgram {

    private int points;
    private int userCount;
    private double cost;

    public TrainingProgram(int points, int userCount, double cost) {
        this.points = points;
        this.userCount = userCount;
        this.cost = cost;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getPoints() {
        return points;
    }

    public int getUserCount() {
        return userCount;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "TrainingProgram{" +
                "points=" + points +
                ", userCount=" + userCount +
                ", cost=" + cost +
                '}';
    }
}
