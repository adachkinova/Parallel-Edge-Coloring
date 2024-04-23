package org.example;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Arc> arcs;
    int numNodes;
    int numArcsPerNode;

    public Graph(int numNodes, int numArcsPerNode) {
        this.numNodes = numNodes;
        this.numArcsPerNode = numArcsPerNode;
        arcs = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            for (int j = 1; j <= numArcsPerNode; j++) {
                int dest = (i + j) % numNodes; // Assign destinations for arcs
                arcs.add(new Arc(arcs.size(), i, dest));
            }
        }
    }
}