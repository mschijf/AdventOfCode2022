package com.adventofcode.december11;

public class MainProgram {

    public static void main(String[] args) {
        MainProgram pp = new MainProgram();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private OctopusEnergyMap octopusEnergyMap;

    public MainProgram() {
//        Input input = new Input("input11_smallexample");
//        Input input = new Input("input11_example");
        Input input = new Input("input11_1");
        octopusEnergyMap = input.getOctopusEnergyMap();
        System.out.println();
    }

    public long run() {
        long sum = 0;
        for (int i=0; i<1000; ++i) {
            sum += octopusEnergyMap.countFlashAfterNextGeneration();
            if (octopusEnergyMap.isAllZeros()) {
                return i+1;
            }
        }
        return -1;
    }
}
