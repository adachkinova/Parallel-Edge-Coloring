package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ParallelGraphColoring {
    final Graph graph;
    int numProcessors;

    public ParallelGraphColoring(Graph graph, int numProcessors) {
        this.graph = graph;
        this.numProcessors = numProcessors;
    }

    public void colorGraph() {

        ForkJoinPool pool = new ForkJoinPool(numProcessors);
        int numArcs = graph.arcs.size();
        int partitionSize = numArcs / numProcessors;
        int remainder = numArcs % numProcessors;

        // Create partitions
        int startIndex = 0;
        for (int i = 0; i < numProcessors; i++) {
            int endIndex = startIndex + partitionSize + (i < remainder ? 1 : 0);
            List<Arc> partition = new ArrayList<>(graph.arcs.subList(startIndex, endIndex));
            startIndex = endIndex;
            pool.execute(() -> colorPartition(partition));
        }

        pool.shutdown();

        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void colorPartition(List<Arc> partition) {
        for (Arc arc : partition) {
            synchronized (graph) {
                arc.color = getNextAvailableColor(arc.src);
            }
        }
    }

    private int getNextAvailableColor(int src) {
        int color = 0;
        while (colorExists(src, color)) {
            color++;
        }
        return color;
    }

    private boolean colorExists(int src, int color) {
        for (Arc arc : graph.arcs) {
            if (arc.src == src && arc.color == color) {
                return true;
            }
        }
        return false;
    }
}
