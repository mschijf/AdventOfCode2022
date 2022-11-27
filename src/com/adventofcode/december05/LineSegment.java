package com.adventofcode.december05;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LineSegment {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public LineSegment(String line) {
        String[] fromTo = line.split("\\s\\-\\>\\s");
        String[] from = fromTo[0].split(",");
        String[] to = fromTo[1].split(",");
        x1 = Integer.parseInt(from[0]);
        y1 = Integer.parseInt(from[1]);
        x2 = Integer.parseInt(to[0]);
        y2 = Integer.parseInt(to[1]);
    }

    public int maxCoordinatePart() {
        return Math.max(Math.max(x1, y1), Math.max(x2, y2));
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public boolean isVertical() {
        return x1 == x2;
    }

    public boolean isDiagonal() {
        return Math.abs(x1 - x2) == Math.abs(y1 - y2);
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
