package com.adventofcode.december22

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val pathSteps = input.inputLines.last().split(Regex("[RL]")).map { it.toInt() }
    private val pathDirs = input.inputLines.last().filter { "RL".contains(it)}.map{ it }
    private val board = Board(input.inputLines.dropLast(2))

    override fun resultPartOne(): String {
        var direction = Direction.RIGHT

        var dirIndex = 0
        for (stepSize in pathSteps) {
            board.doSteps(direction, stepSize)
            if (dirIndex < pathDirs.size) {
                direction = direction.turn(pathDirs[dirIndex])
                dirIndex++
            }
        }
        return ((board.getWalker().row+1)*1000 + (board.getWalker().col+1)*4 + direction.ordinal).toString()
    }

}

class Board(inputLines: List<String>) {
    private val width = inputLines.maxOf { it.length }
    private val height = inputLines.size
    private val board = inputLines
        .map { str -> CharArray(width) { i -> if (i < str.length) str[i] else ' ' } }

    private var walker = Pos(0,0).nextPos(Direction.RIGHT)
    fun getWalker() = walker

    fun doSteps(dir: Direction, stepLength: Int) {
        repeat(stepLength) {
            val nextPos = walker.nextPos(dir)
            if (isWall(nextPos)) {
                return
            }
            walker = nextPos
        }
    }

    private fun isWall(pos: Pos) = board[pos.row][pos.col] == '#'

    fun print() {
        board.forEach { println(it) }
    }

    inner class Pos(val row: Int, val col: Int) {
        fun nextPos(dir: Direction): Pos {
            var newRow = row
            var newCol = col
            do {
                newRow = (newRow + dir.dRow + height) % height
                newCol = (newCol + dir.dCol + width) % width
            } while (board[newRow][newCol] == ' ')
            return Pos(newRow, newCol)
        }
    }
}


enum class Direction(val dRow: Int, val dCol: Int) {
    RIGHT(0,1),
    DOWN(1,0),
    LEFT(0,-1),
    UP(-1,0);

    fun turn(turnChar: Char): Direction {
        when (turnChar) {
            'R' -> return Direction.values()[(this.ordinal + 1) % Direction.values().size]
            'L' -> return Direction.values()[(this.ordinal + Direction.values().size - 1) % Direction.values().size]
            else -> println("ERRORRRR")
        }
        return this
    }
}

//----------------------------------------------------------------------------------------------------------------------

