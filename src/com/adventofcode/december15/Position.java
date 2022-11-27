package com.adventofcode.december15;

import java.util.HashSet;
import java.util.Set;

public class Position {
    public static int visitCount = 0;
    public static int updateCount = 0;

    private int riskLevel;
    private Integer riskLevelFromStart;
    private Set<Position> neighBourSet;
    private int row;
    private int col;

    public Position(int row, int col, int v) {
        this.row = row;
        this.col = col;
        riskLevel = v;
        neighBourSet = new HashSet<>();
        riskLevelFromStart = null;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public void calculateRiskLevelFromStart() {
        ++visitCount;
        if (hasRiskLeveFromStart())
            return;

        boolean hasKnowNeighbour = false;
        int min = Integer.MAX_VALUE;
        for (Position neighbour: this.getNeighBourSet()) {
            if (neighbour.hasRiskLeveFromStart()) {
                hasKnowNeighbour = true;
                if (neighbour.getRiskLevelFromStart() < min) {
                    min = neighbour.getRiskLevelFromStart();
                }
            }
        }
        if (!hasKnowNeighbour) {
            updateCount++;
            this.riskLevelFromStart = riskLevel;
        } else if (min < Integer.MAX_VALUE) {
            updateCount++;
            this.riskLevelFromStart = min + riskLevel;
            updateRiskValueNeighBours(min + riskLevel);
        }
    }

    private void updateRiskValueNeighBours(int otherPath) {
        for (Position neighbour: this.getNeighBourSet()) {
            neighbour.updateRiskLevelFromStart(otherPath);
        }
    }

    private void updateRiskLevelFromStart(int otherPath) {
        ++visitCount;
        if (this.hasRiskLeveFromStart()) {
            if ((otherPath + this.riskLevel) < (this.getRiskLevelFromStart())){
                ++updateCount;
                this.riskLevelFromStart = otherPath + riskLevel;
                updateRiskValueNeighBours(otherPath + riskLevel);
            }
        }
    }

    public int getRiskLevelFromStart() {
        return riskLevelFromStart;
    }

    public boolean hasRiskLeveFromStart() {
        return riskLevelFromStart != null;
    }

    public void addNeighBour(Position neighbour) {
        neighBourSet.add(neighbour);
    }

    public Set<Position> getNeighBourSet() {
        return neighBourSet;
    }

}
