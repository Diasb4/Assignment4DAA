package com.example.Util;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class GraphUtil {

    public static class Edge {
        public int from;
        public int to;
        public double weight;

        public Edge(int f, int t, double w) {
            this.from = f;
            this.to = t;
            this.weight = w;
        }
    }

    public static Map<Integer, List<Edge>> loadGraph(String path) {
        try (Reader reader = new FileReader(path)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            // Читаем вершины
            JsonArray nodesArr = json.getAsJsonArray("nodes");
            Set<Integer> nodes = new HashSet<>();
            for (JsonElement e : nodesArr) {
                nodes.add(e.getAsInt());
            }

            // Читаем рёбра
            JsonArray edgesArr = json.getAsJsonArray("edges");
            Map<Integer, List<Edge>> graph = new HashMap<>();
            for (int node : nodes)
                graph.put(node, new ArrayList<>());

            for (JsonElement el : edgesArr) {
                JsonObject edge = el.getAsJsonObject();
                int from = edge.get("from").getAsInt();
                int to = edge.get("to").getAsInt();
                double w = edge.has("weight") ? edge.get("weight").getAsDouble() : 1.0;
                graph.get(from).add(new Edge(from, to, w));
            }

            return graph;

        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл графа: " + path, e);
        }
    }

    public static Map<Integer, List<Integer>> buildCondensation(
            Map<Integer, List<Integer>> graph,
            List<List<Integer>> sccs) {

        Map<Integer, Integer> compId = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i))
                compId.put(v, i);
        }

        Map<Integer, List<Integer>> dag = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++)
            dag.put(i, new ArrayList<>());

        for (var entry : graph.entrySet()) {
            int u = entry.getKey();
            for (int v : entry.getValue()) {
                int cu = compId.get(u);
                int cv = compId.get(v);
                if (cu != cv && !dag.get(cu).contains(cv))
                    dag.get(cu).add(cv);
            }
        }

        return dag;
    }

}
