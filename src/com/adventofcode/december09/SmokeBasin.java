package com.adventofcode.december09;

import java.util.List;

public class SmokeBasin {

    public static void main(String[] args) {
        SmokeBasin pp = new SmokeBasin();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private HeightMap heightMap;

    public SmokeBasin() {
//        Input input = new Input("input09_example");
        Input input = new Input("input09_1");
        heightMap = input.getHeightMapLines();
    }

    public long run() {
//        return heightMap.riskLevel();
        return heightMap.countBasinSizeNumber();
    }

    //---------------------------------------------------------------------------------------------------------------------
}
