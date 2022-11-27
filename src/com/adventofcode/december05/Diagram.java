package com.adventofcode.december05;

public class Diagram {
    private int[][] diagram;

    public Diagram(int size) {
        initDiagram(size);
    }

    private void initDiagram(int size) {
        int max = size;
        diagram = new int[max+1][max+1];
        for (int r=0; r < diagram.length; r++) {
            for (int c=0; c < diagram.length; c++) {
                diagram[r][c] = 0;
            }
        }
    }

    public void fillLine(LineSegment lineSegment) {
        if (lineSegment.isVertical()) {
            int lo = Math.min(lineSegment.getY1(), lineSegment.getY2());
            int hi = Math.max(lineSegment.getY1(), lineSegment.getY2());
            for (int y = lo; y <= hi; ++y) {
                diagram[y][lineSegment.getX1()] ++;
            }
        } else if (lineSegment.isHorizontal()) {
            int lo = Math.min(lineSegment.getX1(), lineSegment.getX2());
            int hi = Math.max(lineSegment.getX1(), lineSegment.getX2());
            for (int x = lo; x <= hi; ++x) {
                diagram[lineSegment.getY1()][x] ++;
            }
        } else if (lineSegment.isDiagonal()){
            if (lineSegment.getX1() < lineSegment.getX2()) {
                int stepY = (lineSegment.getY1() < lineSegment.getY2() ? 1 : -1);
                int y = lineSegment.getY1();
                for (int x = lineSegment.getX1(); x <= lineSegment.getX2(); x++) {
                    diagram[y][x]++;
                    y += stepY;
                }
            } else {
                int stepY = (lineSegment.getY2() < lineSegment.getY1() ? 1 : -1);
                int y = lineSegment.getY2();
                for (int x = lineSegment.getX2(); x <= lineSegment.getX1(); x++) {
                    diagram[y][x]++;
                    y += stepY;
                }
            }
        } else {
            throw new RuntimeException("weird !!!????");
        }
    }

    public int countOverlappingPoints() {
        int count = 0;
        for (int r=0; r < diagram.length; r++) {
            for (int c=0; c < diagram.length; c++) {
                if (diagram[r][c] > 1)
                    count++;
            }
        }
        return count;
    }


}
