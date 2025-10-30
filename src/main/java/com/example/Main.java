package com.example;

import com.example.Util.GraphUtil;
import com.example.graph.scc.KosarajuSCC;
import com.example.graph.topo.KahnTopo;
import com.example.graph.dagsp.DAGShortestPath;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Загружаем граф из JSON
        String jsonPath = "src/main/java/com/example/data/medium1.json";
        Map<Integer, List<GraphUtil.Edge>> weightedGraph = GraphUtil.loadGraph(jsonPath);

        // Преобразуем в обычный граф без весов
        Map<Integer, List<Integer>> plainGraph = new HashMap<>();
        for (var entry : weightedGraph.entrySet()) {
            List<Integer> targets = new ArrayList<>();
            for (GraphUtil.Edge e : entry.getValue()) {
                targets.add(e.to);
            }
            plainGraph.put(entry.getKey(), targets);
        }

        int n = plainGraph.size();

        // Находим сильно связные компоненты
        KosarajuSCC kosaraju = new KosarajuSCC(plainGraph, n);
        List<List<Integer>> sccs = kosaraju.run();

        System.out.println("SCCs:");
        for (List<Integer> comp : sccs)
            System.out.println(comp);

        // Строим конденсацию графа
        Map<Integer, List<Integer>> dag = GraphUtil.buildCondensation(plainGraph, sccs);
        System.out.println("\nCondensation graph (DAG): " + dag);

        // Топологическая сортировка
        List<Integer> topo = KahnTopo.sort(dag, dag.size());
        System.out.println("Topo order: " + topo);

        // Добавим веса для теста на DAG
        Map<Integer, List<int[]>> weightedDag = new HashMap<>();
        for (int i = 0; i < dag.size(); i++)
            weightedDag.put(i, new ArrayList<>());

        // Примерные рёбра
        weightedDag.get(0).add(new int[] { 1, 2 });
        weightedDag.get(1).add(new int[] { 2, 3 });
        weightedDag.get(0).add(new int[] { 2, 5 });

        // Короткие пути в DAG
        var result = DAGShortestPath.shortest(weightedDag, dag.size(), 0, topo);
        System.out.println("\nShortest distances: " + Arrays.toString(result.dist));

        // "Longest path" — для примера используем ту же функцию, инвертировав веса
        var longest = DAGShortestPath.longest(weightedDag, dag.size(), 0, topo);
        System.out.println("Longest distances: " + Arrays.toString(longest.dist));
    }
}
