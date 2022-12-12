package com.adventofcode.december12

import com.adventofcode.PuzzleSolverAbstract
import java.util.*

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val grid = input.inputLines.map{it.toList()}

    override fun resultPartOne(): String {
        val stepCount = doSolve(findStartPos())
        return stepCount.toString()
    }

    override fun resultPartTwo(): String {
        return getStartPosList()
            .map{startPos -> doSolve(startPos)}
            .filter { stepCount -> stepCount > 0 }
            .min()
            .toString()
    }

    private fun doSolve(startPos: Pos): Int {
        val visitedAfterStepsTaken = List(grid.size) { row -> MutableList(grid[row].size) {0} }
        val stepQueue : Queue<Pos> = LinkedList()
        stepQueue.add(startPos)
        while (stepQueue.isNotEmpty()) {
            val current = stepQueue.remove()
            val stepsDone = visitedAfterStepsTaken[current.row][current.col]
            if (grid[current.row][current.col] == 'E') {
                return stepsDone
            } else {
                current
                    .neighBoursInGrid(grid)
                    .filter {neighbour -> isLegalStep(current, neighbour) && visitedAfterStepsTaken[neighbour.row][neighbour.col] <= 0}
                    .forEach { neighbour ->
                        stepQueue.add(neighbour)
                        visitedAfterStepsTaken[neighbour.row][neighbour.col] = stepsDone + 1
                    }
            }
        }
        return -1
    }

    private fun findStartPos(): Pos {
        for (row in grid.indices)
            for (col in grid[row].indices)
                if (grid[row][col] == 'S')
                    return Pos(row, col)
        return (Pos(0,0))
    }

    private fun getStartPosList(): List<Pos> {
        return grid
            .mapIndexed{ rowIndex, row -> row.mapIndexed { colIndex, col -> Pair(Pos(rowIndex, colIndex), col)}}
            .flatten()
            .filter {it.second == 'a' || it.second == 'S'}
            .map {it.first}
    }

    private fun isLegalStep(fromPos: Pos, toPos:Pos): Boolean {
        return (letterValue(grid[toPos.row][toPos.col]) - letterValue(grid[fromPos.row][fromPos.col]) <= 1)
    }

    private fun letterValue(letter: Char) = (if (letter == 'S') 'a' else if (letter == 'E') 'z' else letter) - 'a'

}

//----------------------------------------------------------------------------------------------------------------------

class Pos(val row: Int, val col: Int) {

    fun neighBoursInGrid(grid: List<List<Any>>): List<Pos> {
        val result = mutableListOf<Pos>()
        if (up().isOnGrid(grid)) result.add(up())
        if (down().isOnGrid(grid)) result.add(down())
        if (left().isOnGrid(grid)) result.add(left())
        if (right().isOnGrid(grid)) result.add(right())
        return result
    }

    private fun isOnGrid(grid: List<List<Any>>) = (row >= 0 && row < grid.size && col >= 0 && col < grid[row].size)

    private fun up() = Pos(row-1, col)
    private fun down() = Pos (row+1, col)
    private fun left() = Pos (row, col-1)
    private fun right() = Pos (row, col+1)
}
