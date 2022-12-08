package com.adventofcode.december08

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val grid = input.inputLines.map { it -> it.toList().map{Info(it - '0', 0)} }

    override fun resultPartOne(): String {
        countVisibleFromLeft()
        countVisibleFromRight()
        countVisibleFromTop()
        countVisibleFromBottom()
        return grid
            .sumOf { it -> it.sumOf { it.second } }
            .toString()
    }

    override fun resultPartTwo(): String {
        return grid
            .mapIndexed { rowIndex, row -> List(row.size) { colIndex -> scenicValue(rowIndex, colIndex) } }
            .maxOfOrNull { it.max() }
            .toString()
    }

    private fun scenicValue(row: Int, col: Int) = lookDown(row, col) * lookUp(row, col) * lookLeft(row, col) * lookRight(row, col)

    private fun countVisibleFromLeft() {
        for (row in grid.indices) {
            var heighest = -1
            for (col in 0 until grid[row].size) {
                if (grid[row][col].first > heighest) {
                    grid[row][col].second = 1
                    heighest = grid[row][col].first
                }
            }
        }
    }

    private fun countVisibleFromRight() {
        for (row in grid.indices) {
            var heighest = -1
            for (col in grid[row].size - 1 downTo 0) {
                if (grid[row][col].first > heighest) {
                    grid[row][col].second = 1
                    heighest = grid[row][col].first
                }
            }
        }
    }

    private fun countVisibleFromTop() {
        for (col in 0 until grid[0].size) {
            var heighest = -1
            for (row in grid.indices) {
                if (grid[row][col].first > heighest) {
                    grid[row][col].second = 1
                    heighest = grid[row][col].first
                }
            }
        }
    }

    private fun countVisibleFromBottom() {
        for (col in 0 until grid[0].size) {
            var heighest = -1
            for (row in grid.size-1 downTo 0) {
                if (grid[row][col].first > heighest) {
                    grid[row][col].second = 1
                    heighest = grid[row][col].first
                }
            }
        }
    }

    private fun lookRight(row: Int, col: Int): Int {
        var result = 0
        for (i in col-1 downTo 0) {
            result++
            if (grid[row][i].first >= grid[row][col].first)
                return result
        }
        return result
    }

    private fun lookLeft(row: Int, col: Int): Int {
        var result = 0
        for (i in col+1 until grid[row].size) {
            result++
            if (grid[row][i].first >= grid[row][col].first)
                return result
        }
        return result
    }

    private fun lookUp(row: Int, col: Int): Int {
        var result = 0
        for (i in row-1 downTo 0) {
            result++
            if (grid[i][col].first >= grid[row][col].first)
                return result
        }
        return result
    }

    private fun lookDown(row: Int, col: Int): Int {
        var result = 0
        for (i in row+1 until grid.size) {
            result++
            if (grid[i][col].first >= grid[row][col].first)
                return result
        }
        return result
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Info(var first: Int, var second: Int)