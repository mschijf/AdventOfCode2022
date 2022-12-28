package com.adventofcode.december24

import com.adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=true).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val valley = Valley(input.inputLines)

    var cache = HashMap<Int, Int>()

    override fun resultPartOne(): String {
        val tree = Tree(valley)
        val optimalPath = tree.search()
        return "NOU NOU: $optimalPath"
    }
//
//    private fun solver(elf: Pos, treeDepth: Int, minutesPassed: Int): Int {
//        if (valley.isEndPos(elf.row, elf.col)) { // end position
//            return minutesPassed
//        }
//        if (treeDepth == 0) {
//            return 99999
//        }
//
//        if (cache.contains(100000*minutesPassed + 1000*elf.row + elf.col)) {
//            return cache[100000*minutesPassed + 1000*elf.row + elf.col]!!
//        }
//
//        var fastestPath = 99999
//        valley.doBlizzardMove()
//        for (newElfPos in generateMoves(elf)) {
//            val value = solver(newElfPos, treeDepth-1, minutesPassed+1)
//            if (value < fastestPath) {
//                fastestPath = value
//            }
//        }
//        valley.undoBlizzardMove()
//
//        cache[100000*minutesPassed + 1000*elf.row + elf.col] = fastestPath
//        return fastestPath
//    }
//
//    private fun generateMoves(elf: Pos): List<Pos> {
//        val result = mutableListOf<Pos>(elf)
//        for (direction in Direction.values()) {
//            if (valley.isFreeField(elf.row+direction.dRow, elf.col + direction.dCol)) {
//                result.add(Pos(elf.row+direction.dRow, elf.col + direction.dCol))
//            }
//        }
//        return result.sortedBy { move -> move.distance(valley.endPos) }
//    }
}

//----------------------------------------------------------------------------------------------------------------------

class Valley(inputLines: List<String>) {
    private val valley = inputLines.map{ str -> Array(str.length) { i -> PositionInfo(str[i]) } }
    private val maxRow = valley.size
    private val maxCol = valley[0].size

    val startPos = Pos(0, valley.first().indexOfFirst { it.isGround } )
    val endPos = Pos(valley.lastIndex, valley.last().indexOfFirst { it.isGround } )

    fun isEndPos(row: Int, col: Int) = (row == maxRow - 1) && valley[row][col].isGround
    fun isFreeField(row: Int, col: Int) = (row in 0 until maxRow) && (col in 0 until maxCol) && valley[row][col].blizzardList.isEmpty() && valley[row][col].isGround

    fun doBlizzardMove() {
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].blizzardList.forEach {blizzard ->
                    val nextBlizzardPos = when (blizzard) {
                        '>' -> nextBlizzardPos(row, col, Direction.RIGHT)
                        '<' -> nextBlizzardPos(row, col, Direction.LEFT)
                        '^' -> nextBlizzardPos(row, col, Direction.UP)
                        'v' -> nextBlizzardPos(row, col, Direction.DOWN)
                        else -> Pos(0,0)
                    }
                    valley[nextBlizzardPos.row][nextBlizzardPos.col].addBlizzard(blizzard)
                }
            }
        }
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].alignBlizzards()
            }
        }
    }

    fun undoBlizzardMove() {
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].blizzardList.forEach {blizzard ->
                    val previousBlizzardPos = when (blizzard) {
                        '>' -> nextBlizzardPos(row, col, Direction.LEFT)
                        '<' -> nextBlizzardPos(row, col, Direction.RIGHT)
                        '^' -> nextBlizzardPos(row, col, Direction.DOWN)
                        'v' -> nextBlizzardPos(row, col, Direction.UP)
                        else -> Pos(0,0)
                    }
                    valley[previousBlizzardPos.row][previousBlizzardPos.col].addBlizzard(blizzard)
                }
            }
        }
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].alignBlizzards()
            }
        }
    }



    private fun nextBlizzardPos(row: Int, col: Int, dir: Direction): Pos {
        val newRow = row + dir.dRow
        val newCol = col + dir.dCol
        if (valley[newRow][newCol].isWall) {
            val oppRow = ((row + 2*dir.dRow + maxRow) % maxRow) + dir.dRow
            val oppCol = ((col + 2*dir.dCol + maxCol) % maxCol) + dir.dCol
            return Pos(oppRow, oppCol)
        } else {
            return Pos(newRow, newCol)
        }
    }

    fun print() {
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].print()
            }
            println()
        }
    }
}

class PositionInfo(inputChar: Char) {
    val isWall = inputChar == '#'
    val isGround = inputChar != '#'

    val blizzardList = listOfNotNull(if (inputChar in "<>^v") inputChar else null).toMutableList()
    private val afterMoveBlizzardList = mutableListOf<Char>()

    fun addBlizzard(blizzardChar: Char) {
        afterMoveBlizzardList.add(blizzardChar)
    }

    fun alignBlizzards() {
        blizzardList.clear()
        blizzardList.addAll(afterMoveBlizzardList)
        afterMoveBlizzardList.clear()
    }

    fun print() {
        if (isWall) {
            print('#')
        } else if (blizzardList.isEmpty()) {
            print('.')
        } else if (blizzardList.size == 1) {
            print(blizzardList.first())
        } else {
            print(blizzardList.size)
        }
    }
}

class Pos(val row: Int, val col: Int) {
    override fun hashCode() = 1000* row + col
    override fun equals(other: Any?): Boolean {
        if (other is Pos)
            return other.row == row && other.col == col
        return super.equals(other)
    }

    fun distance(other: Pos) = (row - other.row).absoluteValue + (col - other.col).absoluteValue
}

enum class Direction(val dRow: Int, val dCol: Int) {
    RIGHT(0,1),
    DOWN(1,0),
    LEFT(0,-1),
    UP(-1,0);
}
