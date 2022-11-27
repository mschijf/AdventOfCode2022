package com.adventofcode.december12;

public class MainProgram {

    public static void main(String[] args) {
        MainProgram pp = new MainProgram();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private Graph graph;
    private static final String monthNumber = "12";
    private static final String postfix = "1";

    public MainProgram() {
        Input input = new Input("input" + monthNumber + "_" + postfix);
        graph = input.getGraph();
        System.out.println();
    }

    public long run() {
        long sum = graph.findPaths();
        return sum;
    }
}
