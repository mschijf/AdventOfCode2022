package com.adventofcode.december09;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HeightMap {

    private int[][] heightMap;
    private boolean[][] alreadyVisited;

    public HeightMap(List<String> input) {
        heightMap = new int[input.size()][input.get(0).length()];
        for (int i=0; i < heightMap.length; ++i) {
            for (int c=0; c < heightMap[i].length; ++c) {
                heightMap[i][c] = input.get(i).charAt(c) - '0';
            }
        }
    }

    public int riskLevel() {
        int sum = 0;
        for (int r = 0; r < heightMap.length; ++r) {
            for (int c = 0; c < heightMap[r].length; ++c) {
                if (       (c == 0                       || heightMap[r][c] < heightMap[r][c - 1])
                        && (c == heightMap[r].length - 1 || heightMap[r][c] < heightMap[r][c + 1])
                        && (r == 0                       || heightMap[r][c] < heightMap[r - 1][c])
                        && (r == heightMap.length - 1    || heightMap[r][c] < heightMap[r + 1][c])
                ) {
                    sum += (heightMap[r][c] + 1);
                }
            }
        }
        return sum;
    }

    private void initVisitedMap() {
        alreadyVisited = new boolean[heightMap.length][heightMap[0].length];
        for (int r=0; r < heightMap.length; ++r) {
            for (int c=0; c < heightMap[r].length; ++c) {
                alreadyVisited[r][c] = false;
            }
        }
    }

    public int countBasinSizeNumber() {
        initVisitedMap();
        List<Integer> basinSizeList = new ArrayList<>();
        for (int r = 0; r < heightMap.length; ++r) {
            for (int c = 0; c < heightMap[r].length; ++c) {
                if (heightMap[r][c] != 9) {
                    int basinSize = countBasinSize(r,c);
                    basinSizeList.add(basinSize);
                }
            }
        }
        basinSizeList.sort(Comparator.reverseOrder());
        return basinSizeList.get(0) * basinSizeList.get(1) * basinSizeList.get(2);
    }


    private int countBasinSize(int row, int col) {
        if (row < 0 || row >= heightMap.length || col < 0 || col >= heightMap[0].length)
            return 0;
        if (heightMap[row][col] == 9)
            return 0;
        if (alreadyVisited[row][col])
            return 0;

        alreadyVisited[row][col] = true;
        return 1
                + countBasinSize(row, col-1)
                + countBasinSize(row, col+1)
                + countBasinSize(row-1, col)
                + countBasinSize(row+1, col);
    }


}
