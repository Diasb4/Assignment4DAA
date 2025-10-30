package com.example.graph.scc;

import java.util.*;

public class SCCResult {
    public final List<List<Integer>> components;
    public final List<Integer> sizes;
    public final int[] compId;

    public SCCResult(List<List<Integer>> components, List<Integer> sizes, int[] compId) {
        this.components = components;
        this.sizes = sizes;
        this.compId = compId;
    }
}
