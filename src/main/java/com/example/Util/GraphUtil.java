package com.example.Util;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.google.gson.*;

/**
 * Утилиты для загрузки и преобразования графов.
 * Формат входного JSON:
 * {
 * "nodes": 5,
 * "edges": [
 * {"from": 0, "to": 1, "weight": 3},
 * {"from": 1, "to": 2, "weight": 2}
 * ]
 * }
 */
public class GraphUtil {

    // ====== 1. Загрузка графа ======
    public static Map<Integer, List<Integer>> loadGraph(String filePath) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            int n = json.get("nodes").getAsInt();

            for (int i = 0; i < n; i++) {
                graph.put(i, new ArrayList<>());
            }

            JsonArray edges = json.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject edge = e.getAsJsonObject();
                int from = edge.get("from").getAsInt();
                int to = edge.get("to").getAsInt();
                graph.get(from).add(to);
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке графа: " + e.getMessage());
        }
        return graph;
    }

    // ====== 2. Загрузка взвешенного графа ======
    public static Map<Integer, List<int[]>> loadWeightedGraph(String filePath) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            int n = json.get("nodes").getAsInt();

            for (int i = 0; i < n; i++) {
                graph.put(i, new ArrayList<>());
            }

            JsonArray edges = json.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject edge = e.getAsJsonObject();
                int from = edge.get("from").getAsInt();
                int to = edge.get("to").getAsInt();
                int weight = edge.has("weight") ? edge.get("weight").getAsInt() : 1;
                graph.get(from).add(new int[] { to, weight });
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке взвешенного графа: " + e.getMessage());
        }
        return graph;
    }

    // ====== 3. Построение конденсации ======
    public static Map<Integer, List<Integer>> buildCondensation(
            Map<Integer, List<Integer>> graph,
            List<List<Integer>> sccs) {

        Map<Integer, Integer> compId = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (int node : sccs.get(i)) {
                compId.put(node, i);
            }
        }

        Map<Integer, Set<Integer>> dagSet = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++)
            dagSet.put(i, new HashSet<>());

        for (int u : graph.keySet()) {
            for (int v : graph.get(u)) {
                int cu = compId.get(u);
                int cv = compId.get(v);
                if (cu != cv)
                    dagSet.get(cu).add(cv);
            }
        }

        Map<Integer, List<Integer>> dag = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            dag.put(i, new ArrayList<>(dagSet.get(i)));
        }
        return dag;
    }

    // ====== 4. Красивый вывод ======
    public static void printGraph(Map<Integer, List<Integer>> g) {
        for (int u : g.keySet()) {
            System.out.println(u + " -> " + g.get(u));
        }
    }
}
