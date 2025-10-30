package com.example.graph.dagsp;

import java.util.*;

public class DAGShortestPath {
    public static class Result {
        public double[] dist;
        public int[] prev;
    }

    public static Result shortest(Map<Integer, List<int[]>> dag, int n, int source, List<Integer> topoOrder) {
        double[] dist = new double[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (int[] edge : dag.getOrDefault(u, Collections.emptyList())) {
                    int v = edge[0];
                    double w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        prev[v] = u;
                    }
                }
            }
        }

        Result res = new Result();
        res.dist = dist;
        res.prev = prev;
        return res;
    }

    public static List<Integer> reconstructPath(int target, int[] prev) {
        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at])
            path.add(at);
        Collections.reverse(path);
        return path;
    }

    public static Result longest(Map<Integer, List<int[]>> graph, int n, int src, List<Integer> topo) {
        double[] dist = new double[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        dist[src] = 0;

        for (int u : topo) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (int[] edge : graph.getOrDefault(u, List.of())) {
                    int v = edge[0], w = edge[1];
                    if (dist[v] < dist[u] + w)
                        dist[v] = dist[u] + w;
                }
            }
        }

        Result res = new Result();
        res.dist = dist;
        return res;
    }

}
