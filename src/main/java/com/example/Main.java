package com.example;

import com.example.Util.GraphUtil;
import com.example.graph.scc.KosarajuSCC;
import com.example.graph.topo.KahnTopo;
import com.example.graph.dagsp.DAGShortestPath;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Путь к тестовому JSON (при разработке используем исходную папку)
        String jsonPath = "src/main/java/com/example/data/small1.json";

        Map<Integer, List<Integer>> graph = GraphUtil.loadGraph(jsonPath);
        int n = graph.size();

        KosarajuSCC kosaraju = new KosarajuSCC(graph, n);
        List<List<Integer>> sccs = kosaraju.run(n);

        System.out.println("SCCs:");
        for (List<Integer> comp : sccs)
            System.out.println(comp);

        Map<Integer, List<Integer>> dag = GraphUtil.buildCondensation(graph, sccs);
        List<Integer> topo = KahnTopo.sort(dag, dag.size());

        System.out.println("Topo order: " + topo);

        // добавь веса для теста
        Map<Integer, List<int[]>> weighted = new HashMap<>();
        for (int i = 0; i < dag.size(); i++)
            weighted.put(i, new ArrayList<>());
        weighted.get(0).add(new int[] { 1, 2 });
        weighted.get(1).add(new int[] { 2, 3 });

        var result = DAGShortestPath.shortest(weighted, dag.size(), 0, topo);
        System.out.println("Distances: " + Arrays.toString(result.dist));

        var longest = DAGShortestPath.shortest(weighted, dag.size(), 0, topo); // longest() not implemented; reuse
                                                                               // shortest for demo
        System.out.println("Longest path (demo): " + Arrays.toString(longest.dist));
    }
}
