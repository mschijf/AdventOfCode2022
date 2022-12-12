package com.adventofcode.december12

import com.adventofcode.PuzzleSolverAbstract
import java.util.*

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    val grid = input.inputLines.map{it.toList()}
    val visited = List(grid.size) { row -> MutableList(grid[row].size) {0} }

    override fun resultPartOne(): String {
        val start = findPos('S')
        val stepCount = doSolve(start)
//        visited.forEach { it.forEachIndexed {  colIndex, cell -> it[colIndex] = 0 } }
//        val stepQueue : Queue<Pos> = LinkedList<Pos>()
//
//        stepQueue.add(start)
//        var stepCount = -1
//        while (stepQueue.isNotEmpty()) {
//            val current = stepQueue.remove()
//            if (grid[current.row][current.col] == 'E') {
//                stepCount = visited[current.row][current.col]
//                break
//            }
//            if (isLegalStep(current, current.up())) {stepQueue.add(current.up()); visited[current.up().row][current.up().col] = visited[current.row][current.col]+1}
//            if (isLegalStep(current, current.down())) {stepQueue.add(current.down()); visited[current.down().row][current.down().col] = visited[current.row][current.col]+1}
//            if (isLegalStep(current, current.left())) {stepQueue.add(current.left()); visited[current.left().row][current.left().col] = visited[current.row][current.col]+1}
//            if (isLegalStep(current, current.right())) {stepQueue.add(current.right()); visited[current.right().row][current.right().col] = visited[current.row][current.col]+1}
//        }
        return stepCount.toString()
    }

    override fun resultPartTwo(): String {
        val startPosList = grid
            .mapIndexed{rowIndex, row -> row.mapIndexed {colIndex, col -> Pair(Pos(rowIndex, colIndex), col)}}
            .flatten()
            .filter {it.second == 'a' || it.second == 'S'}
            .map {it.first}
            .map{doSolve(it)}
            .filter { it > 0}
            .min()

        return startPosList.toString()
    }

    fun doSolve(startPos: Pos): Int {
        visited.forEach { it.forEachIndexed {  colIndex, cell -> it[colIndex] = 0 } }
        val stepQueue : Queue<Pos> = LinkedList<Pos>()
        stepQueue.add(startPos)
        var stepCount = -1
        while (stepQueue.isNotEmpty()) {
            val current = stepQueue.remove()
            if (grid[current.row][current.col] == 'E') {
                stepCount = visited[current.row][current.col]
                break
            }
            if (isLegalStep(current, current.up())) {stepQueue.add(current.up()); visited[current.up().row][current.up().col] = visited[current.row][current.col]+1}
            if (isLegalStep(current, current.down())) {stepQueue.add(current.down()); visited[current.down().row][current.down().col] = visited[current.row][current.col]+1}
            if (isLegalStep(current, current.left())) {stepQueue.add(current.left()); visited[current.left().row][current.left().col] = visited[current.row][current.col]+1}
            if (isLegalStep(current, current.right())) {stepQueue.add(current.right()); visited[current.right().row][current.right().col] = visited[current.row][current.col]+1}
        }
        return stepCount
    }

    fun findPos(letter: Char): Pos {
        for (row in grid.indices)
            for (col in grid[row].indices)
                if (grid[row][col] == letter)
                    return Pos(row, col)
        return (Pos(0,0))
    }

    fun isLegalStep(fromPos: Pos, toPos:Pos): Boolean {
        if (toPos.row < 0 || toPos.row >= grid.size || toPos.col < 0 || toPos.col >= grid[toPos.row].size)
            return false
        if (visited[toPos.row][toPos.col] > 0)
            return false
        var fromLetter = grid[fromPos.row][fromPos.col]
        var toLetter = grid[toPos.row][toPos.col]
        if (fromLetter == 'S') fromLetter = 'a'
        if (fromLetter == 'E') fromLetter = 'z'
        if (toLetter == 'S') toLetter = 'a'
        if (toLetter == 'E') toLetter = 'z'
        if (toLetter - fromLetter > 1)
            return false
        return true
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Pos(val row: Int, val col: Int) {
    fun up() = Pos(row-1, col)
    fun down() = Pos (row+1, col)
    fun left() = Pos (row, col-1)
    fun right() = Pos (row, col+1)
}
