package com.example;

import com.example.graph.topo.KahnTopo;
import com.example.graph.scc.KosarajuSCC;
import com.example.graph.dagsp.DAGShortestPath;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    @Test
    public void testKahnTopoSimpleDAG() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(0, Arrays.asList(1, 2));
        dag.put(1, Arrays.asList(3));
        dag.put(2, Arrays.asList(3));
        dag.put(3, Collections.emptyList());

        List<Integer> order = KahnTopo.sort(dag, 4);

        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    public void testKahnTopoWithCycle() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(0, Arrays.asList(1));
        dag.put(1, Arrays.asList(2));
        dag.put(2, Arrays.asList(0)); // цикл

        assertThrows(RuntimeException.class, () -> KahnTopo.sort(dag, 3));
    }

    @Test
    public void testKahnTopoDisconnected() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(0, Arrays.asList(1));
        dag.put(1, Collections.emptyList());
        dag.put(2, Collections.emptyList());

        List<Integer> order = KahnTopo.sort(dag, 3);
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
    }

    @Test
    public void testKosarajuSCCBasic() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList(2));
        graph.put(2, Arrays.asList(0, 3));
        graph.put(3, Arrays.asList(4));
        graph.put(4, Arrays.asList(5));
        graph.put(5, Arrays.asList(3));

        KosarajuSCC kosaraju = new KosarajuSCC(graph, 6);
        List<List<Integer>> sccs = kosaraju.run();

        assertEquals(2, sccs.size());
        Set<Set<Integer>> expected = new HashSet<>();
        expected.add(new HashSet<>(Arrays.asList(0, 1, 2)));
        expected.add(new HashSet<>(Arrays.asList(3, 4, 5)));

        for (List<Integer> comp : sccs) {
            assertTrue(expected.contains(new HashSet<>(comp)));
        }
    }

    @Test
    public void testKosarajuSCCSingleNode() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Collections.emptyList());

        KosarajuSCC kosaraju = new KosarajuSCC(graph, 1);
        List<List<Integer>> sccs = kosaraju.run();

        assertEquals(1, sccs.size());
        assertEquals(Collections.singletonList(0), sccs.get(0));
    }

    @Test
    public void testDAGShortestPath() {
        Map<Integer, List<int[]>> dag = new HashMap<>();
        dag.put(0, Arrays.asList(new int[] { 1, 3 }, new int[] { 2, 6 }));
        dag.put(1, Arrays.asList(new int[] { 2, 4 }, new int[] { 3, 4 }));
        dag.put(2, Arrays.asList(new int[] { 3, 8 }, new int[] { 4, 7 }));
        dag.put(3, Arrays.asList(new int[] { 4, 2 }, new int[] { 5, 5 }));
        dag.put(4, Arrays.asList(new int[] { 5, 1 }));
        dag.put(5, Collections.emptyList());

        // топологический порядок
        Map<Integer, List<Integer>> simple = new HashMap<>();
        for (int u : dag.keySet()) {
            simple.put(u, new ArrayList<>());
            for (int[] e : dag.get(u))
                simple.get(u).add(e[0]);
        }
        List<Integer> topo = KahnTopo.sort(simple, 6);

        DAGShortestPath.Result res = DAGShortestPath.shortest(dag, 6, 0, topo);

        assertEquals(0.0, res.dist[0]);
        assertEquals(3.0, res.dist[1]);
        assertEquals(6.0, res.dist[2]);
        assertEquals(7.0, res.dist[3]);
        assertEquals(9.0, res.dist[4]);
        assertEquals(10.0, res.dist[5]);
    }
}
