package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int numNodes = 100;
        int numArcsPerNode = 10;
        Graph graph = new Graph(numNodes, numArcsPerNode);

        System.out.println("Брой на дъгите: " + graph.arcs.size());
        System.out.println("Брой на възлите: " + graph.numNodes);
        System.out.println("Брой на дъги към възел: " + graph.numArcsPerNode);
        System.out.println("-------------------------");

        System.out.println("Времена за изпълнение при различни стойности на броя на процесорите (numProcessors):");

        int[] numProcessorsValues = {1, 2, 3, 4};

        // Създаване на списък за съхранение на времената за изпълнение
        List<Long> executionTimes = new ArrayList<>();

        // Измерване на времето за изпълнение за всяка стойност на numProcessors
        for (int pr : numProcessorsValues) {
            ParallelGraphColoring coloringTest = new ParallelGraphColoring(graph, pr);
            long startTime = System.currentTimeMillis();
            coloringTest.colorGraph();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("numProcessors = " + pr + ": " + executionTime + " милисекунди");
            executionTimes.add(executionTime);
        }

        System.out.println("-------------------------");

        // Изчисляване на скалирането
        System.out.println("Скалиране:");
        for (int i = 1; i < executionTimes.size(); i++) {
            BigDecimal executionTimesDecimalOne = BigDecimal.valueOf(executionTimes.get(0));
            BigDecimal executionTimesDecimal = BigDecimal.valueOf(executionTimes.get(i));
            BigDecimal scaling = executionTimesDecimalOne.divide(executionTimesDecimal, 2, RoundingMode.CEILING);
            System.out.println("Скалиране за numProcessors = " + numProcessorsValues[i] + ": " + scaling);
        }

        // Create the sequential graph coloring object
        ParallelGraphColoring sequentialColoring = new ParallelGraphColoring(graph, 1);
        System.out.println("-------------------------");
        // Color the graph sequentially
        System.out.println("Последователно изпълнение без паралелизация:");
        long startSequential = System.currentTimeMillis();
        sequentialColoring.colorPartition(graph.arcs);
        long finishSequential = System.currentTimeMillis();
        long timeElapsedSequential = finishSequential - startSequential;
        System.out.println("Време за изпълнение: " + timeElapsedSequential + " милисекунди");

        System.out.println("-------------------------");
        BigDecimal timeElapsedSequentialDecimal = BigDecimal.valueOf(timeElapsedSequential);
        BigDecimal timeElapsedParallelDecimal = BigDecimal.valueOf(executionTimes.get(3));
        BigDecimal speedup = timeElapsedSequentialDecimal.divide(timeElapsedParallelDecimal, 2,  RoundingMode.CEILING);
        System.out.println("Ускорение: " + speedup);

        System.out.println("-------------------------");

        // Calculate the minimum number of colors used
        Set<Integer> colors = new HashSet<>();
        for (Arc arc : graph.arcs) {
            colors.add(arc.color);
        }
        int minColors = colors.size();

        System.out.println("Минимален брой цветове за оцветяване на дъгите на графа: " + minColors);

    }
}