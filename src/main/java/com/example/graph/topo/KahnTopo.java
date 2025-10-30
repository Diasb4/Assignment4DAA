package com.example.graph.topo;

import java.util.*;

public class KahnTopo {
    public static List<Integer> sort(Map<Integer, List<Integer>> dag, int n) {
        int[] indegree = new int[n];
        for (int u : dag.keySet())
            for (int v : dag.get(u))
                indegree[v]++;

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
            if (indegree[i] == 0)
                q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : dag.getOrDefault(u, Collections.emptyList())) {
                indegree[v]--;
                if (indegree[v] == 0)
                    q.add(v);
            }
        }

        if (order.size() != n)
            throw new RuntimeException("Graph has a cycle, not a DAG!");
        return order;
    }
}
