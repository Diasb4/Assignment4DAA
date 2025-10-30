package com.example.graph.scc;

import java.util.*;

public class KosarajuSCC {
    private final Graph g;
    private final Metrics metrics;

    public KosarajuSCC(Graph graph, Metrics m) {
        this.g = graph;
        this.metrics = m;
    }

    public SCCResult run() {
        int n = g.size();
        boolean[] visited = new boolean[n];
        Stack<Integer> finishOrder = new Stack<>();


        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                dfs1(v, visited, finishOrder);
            }
        }

        Graph transposed = g.getTranspose();

        Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();
        int[] compId = new int[n];
        Arr
    }
}