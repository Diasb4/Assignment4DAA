package com.example.graph.scc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TarjanSCC {
    private Graph g;
    private Metrics metrics;

    // inside TarjanSCC
    int time = 0;
    int[] disc = new int[n];Arrays.fill(disc,-1);
    int[] low = new int[n];
    boolean[] onStack = new boolean[n];
    Deque<Integer> stack = new ArrayDeque<>();
    List<List<Integer>> comps = new ArrayList<>();

    void dfs(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;
        metrics.incr("dfsVisits");
        for (Edge e : g.adj.get(u)) {
            int v = e.to;
            metrics.incr("dfsEdges");
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop();
                onStack[w] = false;
                comp.add(w);
                metrics.incr("stackPops");
                if (w == u)
                    break;
            }
            comps.add(comp);
        }
    }

public SCCResult run() {
    for(int i=0;i<n;i++) if (disc[i]==-1) dfs(i);
    build compId[] from comps;
    return new SCCResult(comps, compId);
}

}
