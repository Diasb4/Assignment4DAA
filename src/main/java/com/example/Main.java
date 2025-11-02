package com.example;

import com.example.Util.GraphUtil;
import com.example.graph.scc.KosarajuSCC;
import com.example.graph.topo.KahnTopo;
import com.example.graph.dagsp.DAGShortestPath;
import java.util.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Папка, где лежат все твои графы
        String folderPath = "demo\\src\\main\\java\\com\\example\\data";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка с графами не найдена: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.err.println("В папке нет JSON-файлов с графами.");
            return;
        }

        // Перебор всех JSON-графов
        for (File file : files) {
            System.out.println("\n===============================");
            System.out.println("Обработка графа: " + file.getName());
            System.out.println("===============================");

            // Загружаем граф
            Map<Integer, List<GraphUtil.Edge>> graph = GraphUtil.loadGraph(file.getPath());
            int n = graph.size();

            // Преобразуем в "плоский" граф без весов для SCC
            Map<Integer, List<Integer>> plainGraph = new HashMap<>();
            for (var entry : graph.entrySet()) {
                List<Integer> targets = new ArrayList<>();
                for (GraphUtil.Edge e : entry.getValue()) {
                    targets.add(e.to);
                }
                plainGraph.put(entry.getKey(), targets);
            }

            // Поиск компонент сильной связности
            KosarajuSCC kosaraju = new KosarajuSCC(plainGraph, n);
            List<List<Integer>> sccs = kosaraju.run();
            System.out.println("SCCs:");
            for (List<Integer> comp : sccs)
                System.out.println(comp);

            // Конденсация
            Map<Integer, List<Integer>> dag = GraphUtil.buildCondensation(plainGraph, sccs);
            System.out.println("\nCondensation graph (DAG): " + dag);

            // Топологическая сортировка
            List<Integer> topo = KahnTopo.sort(dag, dag.size());
            System.out.println("Topo order: " + topo);

            // Примерные веса для DAG
            Map<Integer, List<int[]>> weighted = new HashMap<>();
            for (int i = 0; i < dag.size(); i++)
                weighted.put(i, new ArrayList<>());
            for (int i = 0; i < dag.size() - 1; i++)
                weighted.get(i).add(new int[] { i + 1, i + 2 });

            // Кратчайшие пути (DAGShortestPath)
            var result = DAGShortestPath.shortest(weighted, dag.size(), 0, topo);
            System.out.println("Shortest distances: " + Arrays.toString(result.dist));
        }
    }
}
