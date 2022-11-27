package com.adventofcode.december12;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    Map<String, List<String>> adjList = new HashMap<>();

//    public Graph(String inputLine) {
//
//    }

    public Graph(List<String> inputList) {
        for (String line: inputList) {
            String[] nodes = line.split("\\-");
            addDirectedEdge(nodes[0], nodes[1]);
            addDirectedEdge(nodes[1], nodes[0]);
        }
    }

    private void addDirectedEdge(String from, String to) {
        if (!adjList.containsKey(from)) {
            adjList.put(from, new ArrayList<>());
        }
        if (from.equals("end") || to.equals("start"))
            return;
        adjList.get(from).add(to);
    }

    public int findPaths() {
        return findPathsRecursive("start", "start");
    }

    private int findPathsRecursive(String path, String node) {
        int count = 0;
        if (node.equals("end")) {
            return 1;
        }
        for (String to : adjList.get(node)) {
            if (to.toUpperCase(Locale.ROOT).equals(to) || !inPath(path, to)) {
                count = count + findPathsRecursive(path + "," + to, to);
            }
        }
        return count;
    }

    private boolean inPath(String path, String node ) {
//        return path.contains(node); // <-- genoeg voor deel 1
        if (!path.contains(node))
            return false;
        List<String> nodes =
                Arrays.stream(path.split(","))
                .filter(s->s.equals(s.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        for (int i=0; i<nodes.size()-1; ++i) {
            for (int j=i+1; j<nodes.size(); ++j) {
                if (nodes.get(i).equals(nodes.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }
}
