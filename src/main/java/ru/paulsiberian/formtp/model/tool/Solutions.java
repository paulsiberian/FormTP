package ru.paulsiberian.formtp.model.tool;

import ru.paulsiberian.formtp.model.entity.BusinessProcess;
import ru.paulsiberian.formtp.model.entity.Solution;
import ru.paulsiberian.formtp.model.entity.TrainingProgram;

import java.util.ArrayList;

public class Solutions {

    private final static String SEP = "-";

    private static Solution tp(TrainingProgram tp, int x) {
        int userCount = tp.getUserCount() * x;
        int points = tp.getPoints() * userCount * x;
        double cost = tp.getCost() * userCount * x;
        return new Solution(String.valueOf(x), new TrainingProgram(points, userCount, cost));
    }

    private static Solution sum1(Solution s1, Solution s2) {
        String key = s1.getKey() + s2.getKey();
        int points = s1.getValue().getPoints() + s2.getValue().getPoints();
        int userCount = s1.getValue().getUserCount() + s2.getValue().getUserCount();
        double cost = s1.getValue().getCost() + s2.getValue().getCost();
        return new Solution(key, new TrainingProgram(points, userCount, cost));
    }

    private static Solution sum2(Solution s1, Solution s2) {
        String key = s1.getKey() + SEP + s2.getKey();
        int points = s1.getValue().getPoints() + s2.getValue().getPoints();
        int userCount = s1.getValue().getUserCount() + s2.getValue().getUserCount();
        double cost = s1.getValue().getCost() + s2.getValue().getCost();
        return new Solution(key, new TrainingProgram(points, userCount, cost));
    }

    public static void find(BusinessProcess businessProcess) {
        for (int i = 0; i < businessProcess.size(); i++) {

            if (businessProcess.getSolutions().isEmpty()) {

                Solution s10 = Solutions.tp(businessProcess.get(i), 0);
                Solution s11 = Solutions.tp(businessProcess.get(i), 1);

                if (businessProcess.size() == 1) {

                    businessProcess.getSolutions().add(s10);
                    businessProcess.getSolutions().add(s11);

                } else {

                    Solution s20 = Solutions.tp(businessProcess.get(i + 1), 0);
                    Solution s21 = Solutions.tp(businessProcess.get(i + 1), 1);

                    businessProcess.getSolutions().add(Solutions.sum1(s10, s20));
                    businessProcess.getSolutions().add(Solutions.sum1(s11, s20));
                    businessProcess.getSolutions().add(Solutions.sum1(s10, s21));
                    businessProcess.getSolutions().add(Solutions.sum1(s11, s21));

                    i++;
                }
            } else if (businessProcess.size() > 2) {

                Solution[] solutions = businessProcess.getSolutions().toArray(new Solution[0]);
                businessProcess.getSolutions().clear();

                Solution s10 = Solutions.tp(businessProcess.get(i), 0);
                Solution s11 = Solutions.tp(businessProcess.get(i), 1);

                for (Solution s : solutions) {

                    businessProcess.getSolutions().add(Solutions.sum1(s, s10));
                    businessProcess.getSolutions().add(Solutions.sum1(s, s11));

                }
            }
        }
    }

    public static void removeAllInvalid(BusinessProcess businessProcess, double allocatedMoney) {
        Solution[] solutions = businessProcess.getSolutions().toArray(new Solution[0]);

        for (Solution s : solutions){

            if (s.getValue().getUserCount() < businessProcess.getMinUserCount()
                    | s.getValue().getCost() > allocatedMoney) {

                businessProcess.getSolutions().remove(s);
            }
        }

        solutions = businessProcess.getSolutions().toArray(new Solution[0]);

        for (Solution s1 : solutions) {

            for (Solution s2 : solutions) {

                if ((s1.getValue().getPoints() == s2.getValue().getPoints()
                        && s1.getValue().getCost() > s2.getValue().getCost())
                        | (s1.getValue().getPoints() < s2.getValue().getPoints()
                        && s1.getValue().getUserCount() == s2.getValue().getUserCount()
                        && s1.getValue().getCost() > s2.getValue().getCost())) {

                    businessProcess.getSolutions().remove(s1);
                }
            }
        }
    }

    public static Solution[] findAllValid(BusinessProcess[] businessProcesses, double allocatedMoney) {
        ArrayList<Solution> solutionsList = new ArrayList<>();

        for (int i = 0; i < businessProcesses.length; i++) {

            if (solutionsList.isEmpty()) {

                if (businessProcesses.length == 1) {

                    solutionsList.addAll(businessProcesses[i].getSolutions());

                } else {

                    for (Solution s1 : businessProcesses[i].getSolutions()) {

                        for (Solution s2 : businessProcesses[i + 1].getSolutions()) {

                            solutionsList.add(Solutions.sum2(s1, s2));
                        }
                    }

                    i++;
                }
            } else if (businessProcesses.length > 2) {

                Solution[] solutions = solutionsList.toArray(new Solution[0]);
                solutionsList.clear();

                for (Solution s1 : solutions) {

                    for (Solution s2 : businessProcesses[i].getSolutions()) {

                        solutionsList.add(Solutions.sum2(s1, s2));
                    }
                }
            }
        }

        Solution[] solutions = solutionsList.toArray(new Solution[0]);

        for (Solution s : solutions) {

            if (s.getValue().getCost() > allocatedMoney) {

                solutionsList.remove(s);
            }
        }

        solutions = solutionsList.toArray(new Solution[0]);

        for (Solution s1 : solutions) {

            for (Solution s2 : solutions) {

                if ((s1.getValue().getPoints() == s2.getValue().getPoints()
                        && s1.getValue().getCost() > s2.getValue().getCost())
                        | (s1.getValue().getPoints() < s2.getValue().getPoints()
                        && s1.getValue().getUserCount() == s2.getValue().getUserCount()
                        && s1.getValue().getCost() > s2.getValue().getCost())) {

                    solutionsList.remove(s1);
                }
            }
        }
        return solutionsList.toArray(new Solution[0]);
    }

    public static Solution findBest(Solution[] solutions) {
        Solution bestSolution = solutions[0];

        for (Solution s : solutions) {

            if (s.getValue().getPoints() > bestSolution.getValue().getPoints()) {

                bestSolution = s;
            }
        }

        for (Solution s : solutions) {

            if (s.getValue().getPoints() == bestSolution.getValue().getPoints()
                    && s.getValue().getUserCount() > bestSolution.getValue().getUserCount()) {

                bestSolution = s;
            }
        }
        return bestSolution;
    }

}
