package com.adventofcode.december13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class DotsPattern {

    private List<Coordinate>  coordinateList = new ArrayList<>();
    private List<FoldCommand> commandList = new ArrayList<>();

    boolean[][] pattern;
    int maxX;
    int maxY;

    public DotsPattern(List<String> inputLineList) {
        for (String line: inputLineList) {
            if (line.startsWith("fold")) {
                commandList.add(new FoldCommand(line));
            } else if (!line.isEmpty()) {
                String[] coordString = line.split(",");
                coordinateList.add(new Coordinate(Integer.parseInt(coordString[0]), Integer.parseInt(coordString[1])));
            } else {
                //empty line, ignore
            }
        }
        maxX = coordinateList.stream().max(Comparator.comparing(Coordinate::getX)).map(Coordinate::getX).orElseThrow(NoSuchElementException::new) + 1;
        maxY = coordinateList.stream().max(Comparator.comparing(Coordinate::getY)).map(Coordinate::getY).orElseThrow(NoSuchElementException::new) + 1;
        pattern = new boolean[maxY][maxX];
        createPattern();
    }

    private void createPattern() {
        for (int y=0; y<pattern.length; ++y)
            for (int x=0; x<pattern[y].length; ++x)
                pattern[y][x] = false;

        for (Coordinate c: coordinateList) {
            pattern[c.getY()][c.getX()] = true;
        }
    }

    public void fold() {
        for (FoldCommand fold: commandList) {
            foldOne(fold);
        }
    }

    public void foldFirst() {
        foldOne(commandList.get(0));
    }


    private void foldOne(FoldCommand fold) {
        if (fold.isHorizontalFolding()) {
            if (fold.getNumber() < maxY / 2)
                throw new RuntimeException("unexpected fold Y number");
            for (int y = fold.getNumber() + 1; y < maxY; ++y) {
                copyHorizontalLine(y, fold.getNumber() - (y - fold.getNumber()));
            }
            maxY = fold.getNumber();
        } else {
            if (fold.getNumber() < maxX / 2)
                throw new RuntimeException("unexpected fold X number");
            for (int x = fold.getNumber() + 1; x < maxX; ++x) {
                copyVerticalLine(x, fold.getNumber() - (x - fold.getNumber()));
            }
            maxX = fold.getNumber();
        }
    }


    private void copyHorizontalLine(int from, int to) {
        for (int x=0; x<maxX; ++x) {
            pattern[to][x] = pattern[to][x] || pattern[from][x];
        }
    }

    private void copyVerticalLine(int from, int to) {
        for (int y=0; y<maxY; ++y) {
            pattern[y][to] = pattern[y][to] || pattern[y][from];
        }
    }

    public int countVisible() {
        int count = 0;
        for (int y=0; y<maxY; ++y){
            for (int x=0; x<maxX; ++x) {
                if (pattern[y][x])
                    ++count;
            }
        }
        return count;
    }

    public void print() {
        for (int y=0; y<maxY; ++y){
            for (int x=0; x<maxX; ++x) {
                if (pattern[y][x])
                    System.out.print("#");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
    }
}


