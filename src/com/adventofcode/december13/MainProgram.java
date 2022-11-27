package com.adventofcode.december13;

import java.util.List;

public class MainProgram {

    public static void main(String[] args) {
        MainProgram pp = new MainProgram();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private DotsPattern dotsPattern;

    private static final String monthNumber = "13";
    private static final String postfix = "1";

    public MainProgram() {
        Input input = new Input("input" + monthNumber + "_" + postfix);
        dotsPattern = input.getDotsPattern();
    }

    public long run() {
//        dotsPattern.print();
        dotsPattern.fold();
        System.out.println("==================================================================");
        dotsPattern.print();
        return dotsPattern.countVisible();
    }
}
