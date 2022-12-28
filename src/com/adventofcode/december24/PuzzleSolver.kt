package com.adventofcode.december24

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val valley = Valley(input.inputLines)
        val minutesPassed = walkTo(valley, valley.startPos, valley.endPos)
        return minutesPassed.toString()
    }

    override fun resultPartTwo(): String {
        val valley = Valley(input.inputLines)
        val minutesPassed1 = walkTo(valley, valley.startPos, valley.endPos)
        val minutesPassed2 = walkTo(valley, valley.endPos, valley.startPos)
        val minutesPassed3 = walkTo(valley, valley.startPos, valley.endPos)
        return (minutesPassed1 + minutesPassed2 + minutesPassed3).toString()
    }

    private fun walkTo(valley: Valley, fromPos: Pos, toPos: Pos):Int {
        var minutesPassed = 0
        var candidatePerMinuteSet = setOf(fromPos)

        while (!candidatePerMinuteSet.contains(toPos)) {
            minutesPassed++
            valley.doBlizzardMove()
            candidatePerMinuteSet = candidatePerMinuteSet.map{elfPos -> valley.generateMoves(elfPos)}.flatten().toSet()
        }
        return minutesPassed
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Valley(inputLines: List<String>) {
    private val valley = inputLines.map{ str -> Array(str.length) { i -> PositionInfo(str[i]) } }
    private val maxRow = valley.size
    private val maxCol = valley[0].size

    val startPos = Pos(0, valley.first().indexOfFirst { it.isGround } )
    val endPos = Pos(valley.lastIndex, valley.last().indexOfFirst { it.isGround } )

    private fun isFreeField(pos: Pos) = (pos.row in 0 until maxRow) && (pos.col in 0 until maxCol) && valley[pos.row][pos.col].blizzardList.isEmpty() && valley[pos.row][pos.col].isGround

    fun generateMoves(elf: Pos): List<Pos> {
        return Direction.values()
            .map { direction -> elf.moveTo(direction) }
            .plusElement(elf)
            .filter {newPos -> isFreeField(newPos)}
    }

    fun doBlizzardMove() {
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].blizzardList.forEach { blizzardDirection ->
                    val nextBlizzardPos = nextBlizzardPos(row, col, blizzardDirection)
                    valley[nextBlizzardPos.row][nextBlizzardPos.col].addBlizzard(blizzardDirection)
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

    fun print(candidatePerMinuteSet: Set<Pos>) {
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                if (Pos(row, col) in candidatePerMinuteSet) {
                    print("E")
                } else {
                    valley[row][col].print()
                }
            }
            println()
        }
    }
}

class PositionInfo(inputChar: Char) {
    val isWall = inputChar == '#'
    val isGround = inputChar != '#'

    val blizzardList = listOfNotNull(if (inputChar in "<>^v") toDirection(inputChar) else null).toMutableList()
    private val afterMoveBlizzardList = mutableListOf<Direction>()

    fun addBlizzard(blizzard: Direction) {
        afterMoveBlizzardList.add(blizzard)
    }

    fun alignBlizzards() {
        blizzardList.clear()
        blizzardList.addAll(afterMoveBlizzardList)
        afterMoveBlizzardList.clear()
    }

    private fun toDirection(blizzardChar: Char): Direction {
        return when (blizzardChar) {
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            else -> throw Exception("Unexpected Blizzard Char")
        }
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
    override fun equals(other: Any?) = if (other is Pos) other.row == row && other.col == col else super.equals(other)
    fun moveTo(dir: Direction) = Pos(row+dir.dRow, col+dir.dCol)
}

enum class Direction(val dRow: Int, val dCol: Int, private val directionChar: Char) {
    RIGHT(0,1, '>'),
    DOWN(1,0, 'v'),
    LEFT(0,-1, '<'),
    UP(-1,0, '^');

    override fun toString() = directionChar.toString()
}
