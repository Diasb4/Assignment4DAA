package com.example.graph.scc;

import java.util.*;

public class KosarajuSCC {
    private final Map<Integer, List<Integer>> graph;
    private final int n;
    private final Map<Integer, List<Integer>> revGraph;
    private final boolean[] visited;
    private final Stack<Integer> order;

    public KosarajuSCC(Map<Integer, List<Integer>> graph, int n) {
        this.graph = graph;
        this.n = n;
        this.revGraph = new HashMap<>();
        this.visited = new boolean[n];
        this.order = new Stack<>();
        buildReverseGraph();
    }

    private void buildReverseGraph() {
        for (int u = 0; u < n; u++) {
            revGraph.putIfAbsent(u, new ArrayList<>());
        }
        for (var entry : graph.entrySet()) {
            int u = entry.getKey();
            for (int v : entry.getValue()) {
                revGraph.get(v).add(u);
            }
        }
    }

    public List<List<Integer>> run() {
        Arrays.fill(visited, false);

        // Первая фаза: обычный DFS
        for (int i = 0; i < n; i++) {
            if (!visited[i])
                dfs1(i);
        }

        Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();

        // Вторая фаза: обратный обход
        while (!order.isEmpty()) {
            int v = order.pop();
            if (!visited[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(v, comp);
                components.add(comp);
            }
        }
        return components;
    }

    private void dfs1(int u) {
        visited[u] = true;
        for (int v : graph.getOrDefault(u, List.of())) {
            if (!visited[v])
                dfs1(v);
        }
        order.push(u);
    }

    private void dfs2(int u, List<Integer> comp) {
        visited[u] = true;
        comp.add(u);
        for (int v : revGraph.getOrDefault(u, List.of())) {
            if (!visited[v])
                dfs2(v, comp);
        }
    }
}
