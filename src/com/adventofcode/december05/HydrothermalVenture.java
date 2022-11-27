package com.adventofcode.december05;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HydrothermalVenture {

    public static void main(String[] args) {
        HydrothermalVenture pp = new HydrothermalVenture();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<LineSegment> lineSegmentList;

    public HydrothermalVenture() {
//        Input input = new Input("input05_example");
        Input input = new Input("input05_1");
        lineSegmentList = input.getChunkLines();
    }

    public long run() {
        Diagram diagram = new Diagram(maxCoordinatePart());
        for (LineSegment l: lineSegmentList) {
            diagram.fillLine(l);
        }
        return diagram.countOverlappingPoints();
    }


    private int maxCoordinatePart() {
        int max = -1;
        for (LineSegment l: lineSegmentList) {
            max = Math.max(max, l.maxCoordinatePart());
        }
        return max;
    }


}
