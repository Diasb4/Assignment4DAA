package com.example;

import com.example.Util.GraphUtil;
import com.example.graph.scc.KosarajuSCC;
import com.example.graph.topo.KahnTopo;
import com.example.graph.dagsp.DAGShortestPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String folderPath = "C:\\Users\\Laptop\\Desktop\\base\\Projects\\Новая\\Assignment4\\demo\\src\\main\\java\\com\\example\\data";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Folder with graphs not found: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json") && !name.contains("_result"));
        if (files == null || files.length == 0) {
            System.err.println("No JSON files with graphs found in the folder.");
            return;
        }

        for (File file : files) {
            System.out.println("Processing graph: " + file.getName());

            Map<Integer, List<GraphUtil.Edge>> graph = GraphUtil.loadGraph(file.getPath());
            int n = graph.size();

            Map<Integer, List<Integer>> plainGraph = new HashMap<>();
            for (var entry : graph.entrySet()) {
                List<Integer> targets = new ArrayList<>();
                for (GraphUtil.Edge e : entry.getValue()) {
                    targets.add(e.to);
                }
                plainGraph.put(entry.getKey(), targets);
            }

            KosarajuSCC kosaraju = new KosarajuSCC(plainGraph, n);
            List<List<Integer>> sccs = kosaraju.run();
            System.out.println("SCCs:");
            for (List<Integer> comp : sccs)
                System.out.println(comp);

            Map<Integer, List<Integer>> dag = GraphUtil.buildCondensation(plainGraph, sccs);
            System.out.println("\nCondensation graph (DAG): " + dag);

            List<Integer> topo = KahnTopo.sort(dag, dag.size());
            System.out.println("Topo order: " + topo);

            Map<Integer, List<int[]>> weighted = new HashMap<>();
            for (int i = 0; i < dag.size(); i++)
                weighted.put(i, new ArrayList<>());
            for (int i = 0; i < dag.size() - 1; i++)
                weighted.get(i).add(new int[] { i + 1, i + 2 });

            var result = DAGShortestPath.shortest(weighted, dag.size(), 0, topo);
            System.out.println("Shortest distances: " + Arrays.toString(result.dist));

            // Сохраняем результат в JSON
            saveResultToJson(file.getName(), sccs, dag, topo, result);
        }
    }

    private static void saveResultToJson(String fileName, List<List<Integer>> sccs,
            Map<Integer, List<Integer>> dag, List<Integer> topo,
            DAGShortestPath.Result result) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("source_file", fileName);
        output.put("SCCs", sccs);
        output.put("DAG", dag);
        output.put("topo_order", topo);
        output.put("shortest_distances", result.dist);
        output.put("predecessors", result.prev);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .create();

        String outName = fileName.replace(".json", "_result.json");
        File outFile = new File(
                "C:\\Users\\Laptop\\Desktop\\base\\Projects\\Новая\\Assignment4\\demo\\src\\main\\java\\com\\example\\output",
                outName);

        outFile.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(outFile)) {
            gson.toJson(output, writer);
            System.out.println("Results is in: " + outFile.getPath());
        } catch (IOException e) {
            System.err.println("Error writing JSON: " + e.getMessage());
        }
    }
}
