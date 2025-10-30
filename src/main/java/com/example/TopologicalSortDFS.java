package com.example;

/**
 * Hello world!
 *
 */
import java.util.*;

// Топологическая сортировка с помощью DFS
public class TopologicalSortDFS {
    private int V; // количество вершин
    private List<List<Integer>> adj; // список смежности

    public TopologicalSortDFS(int vertices) {
        V = vertices;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++)
            adj.add(new ArrayList<>());
    }

    // Добавляем ребро u → v
    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    // Рекурсивный DFS
    private void dfs(int v, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;

        // идем глубже по всем соседям
        for (int neighbor : adj.get(v)) {
            if (!visited[neighbor])
                dfs(neighbor, visited, stack);
        }

        // когда больше некуда идти — добавляем в стек
        stack.push(v);
    }

    // Основной метод сортировки
    public void topologicalSort() {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[V];

        // запускаем DFS из каждой вершины (на случай, если граф несвязный)
        for (int i = 0; i < V; i++) {
            if (!visited[i])
                dfs(i, visited, stack);
        }

        // теперь вершины в стеке в обратном порядке зависимостей
        System.out.print("Topological Order: ");
        while (!stack.isEmpty())
            System.out.print(stack.pop() + " ");
    }

    // пример использования
    public static void main(String[] args) {
        TopologicalSortDFS g = new TopologicalSortDFS(6);
        g.addEdge(5, 2);
        g.addEdge(5, 0);
        g.addEdge(4, 0);
        g.addEdge(4, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        g.topologicalSort();
    }
}
