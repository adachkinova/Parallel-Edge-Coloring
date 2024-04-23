package org.example;

public class Arc {
    int id;
    int src; // Source node
    int dest; // Destination node
    int color;

    public Arc(int id, int src, int dest) {
        this.id = id;
        this.src = src;
        this.dest = dest;
        this.color = -1; // Initially uncolored
    }
}