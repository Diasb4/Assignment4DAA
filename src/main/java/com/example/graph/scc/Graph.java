package com.example.graph.scc;

import java.util.*;

public class Graph {
    private final int V;
    private final List<List<Integer>> adj;

    public Graph(int vertices) {
        this.V = vertices;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++)
            adj.add(new ArrayList<>());
    }

    public void addEdge(int from, int to) {
        adj.get(from).add(to);
    }

    public List<Integer> getAdj(int v) {
        return adj.get(v);
    }

    public int size() {
        return V;
    }

    public Graph getTranspose() {
        Graph t = new Graph(V);
        for (int v = 0; v < V; v++) {
            for (int nei : adj.get(v)) {
                t.addEdge(nei, v);
            }
        }
        return t;
    }
}
