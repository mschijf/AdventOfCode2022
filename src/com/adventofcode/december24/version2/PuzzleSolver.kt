package com.adventofcode.december24.version2

import com.adventofcode.PuzzleSolverAbstract
import java.lang.Math.floorMod
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=false).showResult()
}

// alternative A* algorithm solution. Works for example, but not for real input (stopped after few minutes with heap space error ...)
//     didn't implement a cache to make a directed acyclic graph

// alternative algorithm 2: bruteforce recursive . Works for example and for real input (cache necessary)

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val valley = Valley(input.inputLines)
//        val solver = AStar(valley)
        val solver = SearchRecursive(valley)
        val minutesPassed = solver.solve()
        return minutesPassed.toString()
    }

    override fun resultPartTwo(): String {
        val valley = Valley(input.inputLines)
        val solver = SearchRecursive(valley)
        val minutesPassed = solver.solvePart2()
        return minutesPassed.toString()
    }

}

//----------------------------------------------------------------------------------------------------------------------

class Valley(inputLines: List<String>) {
    private val valley = inputLines.map{ str -> List(str.length) { i -> PositionInfo(str[i]) } }
    private val maxRow = valley.size
    private val maxCol = valley[0].size
    val windCycle = (maxCol-2) * (maxRow-2)

    val startPos = Pos(0, valley.first().indexOfFirst { it.isGround } )
    val endPos = Pos(valley.lastIndex, valley.last().indexOfFirst { it.isGround } )

    private fun isFreeField(pos: Pos, afterMinute: Int): Boolean {
        return (pos.row in 0 until maxRow) &&
                (pos.col in 0 until maxCol) &&
                valley[pos.row][pos.col].isGround &&
                !hasBlizzard(pos.row, pos.col, afterMinute)
    }

    private fun hasBlizzard(row: Int, col: Int, afterMinute: Int): Boolean {
        return  valley[row][floorMod(col-1-afterMinute, maxCol-2) + 1].hasRightBlizzard() ||
                valley[row][floorMod(col-1+afterMinute, maxCol-2) + 1].hasLeftBlizzard() ||
                valley[floorMod(row-1-afterMinute, maxRow-2) + 1][col].hasDownBlizzard() ||
                valley[floorMod(row-1+afterMinute, maxRow-2) + 1][col].hasUpBlizzard()
    }

    fun generateMoves(elf: Pos,  afterMinute: Int): List<Pos> {
        return Direction.values()
            .map { direction -> elf.moveTo(direction) }
            .plusElement(elf)
            .filter {newPos -> isFreeField(newPos, afterMinute)}
    }
}

class PositionInfo(inputChar: Char) {
    val isWall = inputChar == '#'
    val isGround = inputChar != '#'
    private val blizzardDirectionByStart = if (inputChar in "<>^v") toDirection(inputChar) else null

    fun hasRightBlizzard() = blizzardDirectionByStart == Direction.RIGHT
    fun hasLeftBlizzard() = blizzardDirectionByStart == Direction.LEFT
    fun hasDownBlizzard() = blizzardDirectionByStart == Direction.DOWN
    fun hasUpBlizzard() = blizzardDirectionByStart == Direction.UP

    private fun toDirection(blizzardChar: Char): Direction {
        return when (blizzardChar) {
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            else -> throw Exception("Unexpected Blizzard Char")
        }
    }
}


data class Pos(val row: Int, val col: Int) {
    override fun hashCode() = 1000* row + col
    override fun equals(other: Any?) = if (other is Pos) other.row == row && other.col == col else super.equals(other)
    fun moveTo(dir: Direction) = Pos(row+dir.dRow, col+dir.dCol)
    fun distance(endPos: Pos): Int = (row - endPos.row).absoluteValue + (col - endPos.col).absoluteValue
}

enum class Direction(val dRow: Int, val dCol: Int, private val directionChar: Char) {
    RIGHT(0,1, '>'),
    DOWN(1,0, 'v'),
    LEFT(0,-1, '<'),
    UP(-1,0, '^');

    override fun toString() = directionChar.toString()
}
