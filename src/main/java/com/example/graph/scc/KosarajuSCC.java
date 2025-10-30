package com.example.graph.scc;

import java.util.*;

public class KosarajuSCC {
    private final Map<Integer, List<Integer>> graph;
    private final Map<Integer, List<Integer>> reversed;
    private final boolean[] visited;
    private final Stack<Integer> finishOrder;
    private final List<List<Integer>> components;

    public KosarajuSCC(Map<Integer, List<Integer>> g, int n) {
        this.graph = g;
        this.reversed = new HashMap<>();
        this.visited = new boolean[n];
        this.finishOrder = new Stack<>();
        this.components = new ArrayList<>();
        buildReverseGraph(n);
    }

    private void buildReverseGraph(int n) {
        for (int i = 0; i < n; i++)
            reversed.put(i, new ArrayList<>());
        for (int u : graph.keySet())
            for (int v : graph.get(u))
                reversed.get(v).add(u);
    }

    private void dfs1(int u) {
        visited[u] = true;
        for (int v : graph.getOrDefault(u, Collections.emptyList()))
            if (!visited[v])
                dfs1(v);
        finishOrder.push(u);
    }

    private void dfs2(int u, List<Integer> comp) {
        visited[u] = true;
        comp.add(u);
        for (int v : reversed.getOrDefault(u, Collections.emptyList()))
            if (!visited[v])
                dfs2(v, comp);
    }

    public List<List<Integer>> run(int n) {
        Arrays.fill(visited, false);
        for (int i = 0; i < n; i++)
            if (!visited[i])
                dfs1(i);

        Arrays.fill(visited, false);
        while (!finishOrder.isEmpty()) {
            int node = finishOrder.pop();
            if (!visited[node]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(node, comp);
                components.add(comp);
            }
        }
        return components;
    }
}
