package com.adventofcode.december17

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=true).showResult()
}

const val chamberWidth = 7
const val leftEdgeRoom = 2
const val bottomEdgeRoom = 3

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val jetsStream = JetStream(input.inputLines[0])
    private val rockShapeList = RockShapeList()

    override fun resultPartOne(): String {
        jetsStream.reset()
        rockShapeList.reset()
        val rockPieces = mutableSetOf<Coordinate>()

        repeat(2022) {
            val rock = Rock(rockShape=rockShapeList.nextShape(), highestTop=(rockPieces.maxOfOrNull { it.y } ?: -1) + 1)
            rockRolling(rock, rockPieces)
            rockPieces.addAll(rock.coordinateList)
        }

        return (rockPieces.maxOf {  it.y } + 1).toString()
    }

    override fun resultPartTwo(): String {
        jetsStream.reset()
        rockShapeList.reset()
        val rockPieces = mutableSetOf<Coordinate>()
        val stateList = mutableListOf<State>()

        stateList.add(State(rockPieces, rockShapeList, jetsStream))
        repeat(5000) {
            val rock = Rock(rockShape=rockShapeList.nextShape(), highestTop=(rockPieces.maxOfOrNull { it.y } ?: -1) + 1)
            rockRolling(rock, rockPieces)
            rockPieces.addAll(rock.coordinateList)

            val newState = State(rockPieces, rockShapeList, jetsStream)
            val index = stateList.indexOfFirst { it == newState }
            if (index >= 0) {
                println("DAAR IS TIE!  $index --> ${stateList.size}  hoogte: ${newState.height - stateList[index].height}")

                val cycleLength = (stateList.size - index)
                val heightPerCycle = newState.height - stateList[index].height
                val countRepeatingRounds = 1_000_000_000_000 / cycleLength
                val initialRounds = 1_000_000_000_000 % cycleLength

                val startHeight = stateList[initialRounds.toInt()].height
                return (countRepeatingRounds * heightPerCycle + startHeight).toString()
            }
            stateList.add(newState)
        }
        return "NO ANSWER FOUND"
    }

    private fun rockRolling(rock: Rock, rockPieces: Set<Coordinate>) {
        rock.pushByJet(jetsStream.nextDirection(), rockPieces)
        while (rock.canFallDown(rockPieces)) {
            rock.fallDownOneUnit()
            rock.pushByJet(jetsStream.nextDirection(), rockPieces)
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------

class State(rockPieces: Set<Coordinate>, rockShapeList: RockShapeList, jetStream: JetStream) {
    private val firstColumnHeight = rockPieces.filter { it.x == 0 }.maxOfOrNull { it.y } ?: 0
    private val topPattern = rockPieces.groupBy { v -> v.x }.map{it.value.maxOf { c -> c.y } - firstColumnHeight}
    private val rockShapeIndex =  rockShapeList.currentIndex
    private val jetStreamIndex = jetStream.currentIndex
    val height = (rockPieces.maxOfOrNull { it.y } ?: -1) + 1

    override fun equals(other: Any?): Boolean {
        if (other !is State)
            return super.equals(other)
        return (rockShapeIndex == other.rockShapeIndex) && (jetStreamIndex == other.jetStreamIndex) && (topPattern == other.topPattern)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Rock(rockShape: RockShape, highestTop: Int) {
    var coordinateList = rockShape.coordinateList
        .map { c -> Coordinate(c.x + leftEdgeRoom, c.y+highestTop+bottomEdgeRoom+rockShape.shapeHeight-1) }

    fun canFallDown(rockPieces: Set<Coordinate>): Boolean {
        return if (coordinateList.minOf { it.y } <= 0) {
            false
        } else {
            coordinateList
                .map { c -> Coordinate(c.x, c.y - 1) }
                .intersect(rockPieces)
                .isEmpty()
        }
    }

    private fun canBePushedLeft(rockPieces: Set<Coordinate>): Boolean {
        return if (coordinateList.minOf { it.x } <= 0) {
            false
        } else {
            coordinateList
                .map { c -> Coordinate(c.x - 1, c.y) }
                .intersect(rockPieces)
                .isEmpty()
        }
    }

    private fun canBePushedRight(rockPieces: Set<Coordinate>): Boolean {
        return if (coordinateList.maxOf { it.x } >= chamberWidth-1) {
            false
        } else {
            return coordinateList
                .map { c -> Coordinate(c.x + 1, c.y) }
                .intersect(rockPieces)
                .isEmpty()
        }
    }

    fun pushByJet(dir: Direction, rockPieces: Set<Coordinate>) {
        if (dir == Direction.LEFT) {
            if (canBePushedLeft(rockPieces)) {
                coordinateList = coordinateList.map { c -> Coordinate(c.x - 1, c.y) }
            }
        } else {
            if (canBePushedRight(rockPieces)) {
                coordinateList = coordinateList.map { c -> Coordinate(c.x + 1, c.y) }
            }
        }
    }

    fun fallDownOneUnit() {
        coordinateList = coordinateList
                .map { c -> Coordinate(c.x, c.y-1) }
    }
}

//----------------------------------------------------------------------------------------------------------------------

class JetStream(inputStr: String) {
    var currentIndex = 0
        private set

    private val dirList = inputStr
        .map {if (it == '<') Direction.LEFT else Direction.RIGHT}

    fun nextDirection(): Direction {
        val oldIndex = currentIndex
        currentIndex = (currentIndex + 1) % dirList.size
        return dirList[oldIndex]
    }

    fun reset() {
        currentIndex = 0
    }
}

enum class Direction {
    LEFT, RIGHT
}

class RockShapeList {
    private val shapeList = listOf(
        RockShape(listOf(Coordinate(0,0), Coordinate(1,0), Coordinate(2,0), Coordinate(3,0))),
        RockShape(listOf(Coordinate(1,0), Coordinate(0,-1), Coordinate(1,-1), Coordinate(2,-1), Coordinate(1,-2))),
        RockShape(listOf(Coordinate(2,0), Coordinate(2,-1), Coordinate(0,-2), Coordinate(1,-2), Coordinate(2,-2))),
        RockShape(listOf(Coordinate(0,0), Coordinate(0,-1), Coordinate(0,-2), Coordinate(0,-3))),
        RockShape(listOf(Coordinate(0,0), Coordinate(1,0), Coordinate(0,-1), Coordinate(1,-1))),
    )

    var currentIndex = 0
        private set

    fun nextShape(): RockShape {
        val oldIndex = currentIndex
        currentIndex = (currentIndex + 1) % shapeList.size
        return shapeList[oldIndex]
    }

    fun reset() {
        currentIndex = 0
    }
}

data class RockShape(val coordinateList : List<Coordinate>) {
    val shapeHeight = coordinateList.distinctBy { it.y }.count()
}

data class Coordinate(val x: Int, val y: Int)
