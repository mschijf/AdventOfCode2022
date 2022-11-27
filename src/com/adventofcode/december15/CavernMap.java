package com.adventofcode.december15;

import java.util.List;

public class CavernMap {

    private Position[][] riskMap;

    public CavernMap(List<String> inputLineList, boolean timesFive) {
        int maxRepeat = timesFive ? 5 : 1;
        riskMap = new Position[maxRepeat * inputLineList.size()][];
        int lineCount = 0;
        for (int rowRepeat=0; rowRepeat < maxRepeat; ++rowRepeat) {
            for (String line : inputLineList) {
                riskMap[lineCount] = new Position[maxRepeat * line.length()];
                for (int colRepeat = 0; colRepeat < maxRepeat; ++colRepeat) {
                    for (int i = 0; i < line.length(); ++i) {
                        int val = (line.charAt(i) - '0');
                        val = 1 + ((val + colRepeat + rowRepeat - 1) % 9);
                        int thiscl = i + line.length() * colRepeat;
                        riskMap[lineCount][thiscl] = new Position(lineCount, thiscl, val);
                    }
                }
                ++lineCount;
            }
        }
        initNeighBours();
    }

    public void print() {
        for (int row=0; row < riskMap.length; ++row) {
            for (int col=0; col < riskMap[row].length; ++col) {
                System.out.print(riskMap[row][col].getRiskLevel());
            }
            System.out.println();
        }
    }

    public CavernMap(List<String> inputLineList) {
        this(inputLineList, false);
    }

    private void initNeighBours() {
        for (int row=0; row < riskMap.length; ++row) {
            for (int col=0; col < riskMap[row].length; ++col) {
                Position p = riskMap[row][col];
                if (legalPosition(row-1, col)) p.addNeighBour(riskMap[row-1][col]);
                if (legalPosition(row, col+1)) p.addNeighBour(riskMap[row][col+1]);
                if (legalPosition(row+1, col)) p.addNeighBour(riskMap[row+1][col]);
                if (legalPosition(row, col-1)) p.addNeighBour(riskMap[row][col-1]);
            }
        }
    }

    public int findShortestPath() {
        determinePathLengths();
        int lastValue = riskMap[riskMap.length-1][riskMap[riskMap.length-1].length-1].getRiskLevelFromStart();
        return lastValue - riskMap[0][0].getRiskLevelFromStart();
    }

    private void determinePathLengths() {
        for (int row=0; row < riskMap.length; ++row) {
            for (int col=0; col < riskMap[row].length; ++col) {
                riskMap[row][col].calculateRiskLevelFromStart();
            }
        }
    }

    private boolean legalPosition(int row, int col) {
        return (row >= 0 && row < riskMap.length && col >= 0 && col < riskMap[row].length);
    }
}
