package ru.paulsiberian.formtp.model;

import org.junit.Assert;
import org.junit.Test;
import ru.paulsiberian.formtp.model.entity.BusinessProcess;
import ru.paulsiberian.formtp.model.entity.Solution;
import ru.paulsiberian.formtp.model.entity.TrainingProgram;
import ru.paulsiberian.formtp.model.tool.Solutions;

import java.util.Arrays;

public class FindBestSolutionTest extends Assert {

    @Test
    public void test() {

        final Solution expected = new Solution("101-10-10", new TrainingProgram(59, 14, 1026));
        final double moneyAvailable = 1100;

        BusinessProcess[] businessProcesses = new BusinessProcess[3];
        businessProcesses[0] = new BusinessProcess(5);
        businessProcesses[1] = new BusinessProcess(3);
        businessProcesses[2] = new BusinessProcess(2);
        businessProcesses[0].add(new TrainingProgram(4, 3, 60));
        businessProcesses[0].add(new TrainingProgram(3, 2, 64));
        businessProcesses[0].add(new TrainingProgram(5, 3, 90));
        businessProcesses[1].add(new TrainingProgram(3, 4, 54));
        businessProcesses[1].add(new TrainingProgram(4, 3, 90));
        businessProcesses[2].add(new TrainingProgram(5, 4, 90));
        businessProcesses[2].add(new TrainingProgram(3, 2, 54));

        System.out.println("Всего бизнес-процессов: " + businessProcesses.length + '\n');
        int k = 0;
        for (BusinessProcess bp : businessProcesses) {
            System.out.println(String.format("Бизнес-процесс[%d]:\n%s", k, bp));
            Solutions.find(bp);
            System.out.println("-\t Все решения:\n-\t\t" + bp.getSolutions());
            Solutions.removeAllInvalid(bp, moneyAvailable);
            System.out.println("-\t Допустимые решения:\n-\t\t" + bp.getSolutions() + '\n');
            k++;
        }
        Solution[] solutions = Solutions.findAllValid(businessProcesses, moneyAvailable);
        System.out.println("Все допустимые решения:\n\t" + Arrays.toString(solutions));
        Solution bestSolution = Solutions.findBest(solutions);
        System.out.println("\nЛучшее решение: " + bestSolution + '\n');

        assertEquals(expected, bestSolution);
    }
}
