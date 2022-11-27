package com.adventofcode.december11;

import javafx.util.Pair;

import java.util.List;
import java.util.Stack;

public class OctopusEnergyMap {

    private int[][] energyMap;
    private Stack<Pair<Integer, Integer>> stack = new Stack<>();

    public OctopusEnergyMap(List<String> inputList) {
        energyMap = new int[inputList.size()][inputList.get(0).length()];
        for (int i = 0; i < energyMap.length; ++i) {
            for (int c = 0; c < energyMap[i].length; ++c) {
                energyMap[i][c] = inputList.get(i).charAt(c) - '0';
            }
        }
    }

    public int countFlashAfterNextGeneration() {
        int countFlash = 0;

        //first round: add 1 to each
        for (int row = 0; row < energyMap.length; ++row) {
            for (int col = 0; col < energyMap[row].length; ++col) {
                updateField(row, col);
            }
        }

        //second round: process flashers
        int max = energyMap.length;
        while (!stack.isEmpty()) {
            countFlash++;

            Pair<Integer, Integer> pair = stack.pop();
            int row = pair.getKey();
            int col = pair.getValue();

            if (row-1 >= 0 && col-1 >= 0) updateField(row-1, col-1);
            if (row-1 >= 0) updateField(row-1, col);
            if (row-1 >= 0 && col+1 < max) updateField(row-1, col+1);

            if (col-1 >= 0) updateField(row, col-1);
            if (col+1 < max) updateField(row, col+1);

            if (row+1 < max && col-1 >= 0) updateField(row+1, col-1);
            if (row+1 < max) updateField(row+1, col);
            if (row+1 < max && col+1 < max) updateField(row+1, col+1);
        }

        //third round: reset higher than 10 to 0
        for (int row = 0; row < energyMap.length; ++row) {
            for (int col = 0; col < energyMap[row].length; ++col) {
                resetFlashField(row, col);
            }
        }

        return countFlash;
    }

    void updateField(int row, int col) {
        ++energyMap[row][col];
        if (energyMap[row][col] == 10) {
            stack.push(new Pair<>(row, col));
        }
    }

    void resetFlashField(int row, int col) {
        if (energyMap[row][col] >= 10) {
            energyMap[row][col] = 0;
        }
    }

    public boolean isAllZeros() {
        for (int row = 0; row < energyMap.length; ++row) {
            for (int col = 0; col < energyMap[row].length; ++col) {
                if (energyMap[row][col] != 0)
                    return false;
            }
        }
        return true;
    }


}
