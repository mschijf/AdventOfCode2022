package com.adventofcode.december14;

import java.util.List;

public class MainProgram {

    public static void main(String[] args) {
        MainProgram pp = new MainProgram();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<Polymer> polymerList;
    private Polymer polymer;

    private static final String monthNumber = "14";
    private static final String postfix = "1";

    public MainProgram() {
        Input input = new Input("input" + monthNumber + "_" + postfix);
        polymer = input.getBaseClass();
    }

    public long run() {
        long sum = polymer.runProcesses(40);
//        long sum = polymer.runProcessesAlternative(10);
//        long sum = polymer.runProcessesAlternative(40);
        return sum;
    }
}
