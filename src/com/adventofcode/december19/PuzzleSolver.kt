package com.adventofcode.december19

import com.adventofcode.PuzzleSolverAbstract
import java.lang.Integer.max
import java.lang.System.currentTimeMillis

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val bluePrintList = input.inputLines.map { BluePrint(it) }

    private val cache = Array(33) {HashMap<String, Int>()}

    override fun resultPartOne(): String {
        var quality = 0
        for (bluePrint in bluePrintList) {
            val startTime = currentTimeMillis()
            val executor = Executor(bluePrint)
            print("Start Blueprint ${bluePrint.number} of ${bluePrintList.size}")

            cache.forEach { it.clear() }
            val gc = solver(executor, 24, -1)
            val timePassed = currentTimeMillis() - startTime
            println(" --> $gc time: $timePassed ms")

            quality += gc*bluePrint.number
        }
        return quality.toString()
    }

    override fun resultPartTwo(): String {
        var quality = 1
        for (bluePrint in bluePrintList.dropLast(max(0, bluePrintList.size-3))) {
            val startTime = currentTimeMillis()
            val executor = Executor(bluePrint)
            print("Start Blueprint ${bluePrint.number} of ${bluePrintList.size}")

            cache.forEach { it.clear() }
            val gc = solver(executor, 32, -1)
            val timePassed = currentTimeMillis() - startTime
            println(" --> $gc time: $timePassed ms")

            quality *= gc
        }
        return quality.toString()
    }

    private fun solver(executor: Executor, minutesLeft: Int, maxToReach: Int): Int {
        if (minutesLeft <= 0) {
            return executor.geodeCount
        }

        val maxPotentialGeode = (minutesLeft * (minutesLeft-1))/2
        if (executor.geodeCount + executor.geodeRobotCount * minutesLeft + maxPotentialGeode <= maxToReach) {
            return -1
        }

        if (cache[minutesLeft].contains(executor.hashString())) {
            return cache[minutesLeft][executor.hashString()] ?: -1
        }


        if (minutesLeft == 2) {
            return if (!executor.canBeMade(RobotType.GEODE)) {
                executor.geodeCount + minutesLeft*executor.geodeRobotCount
            } else {
                executor.geodeCount + minutesLeft*executor.geodeRobotCount + 1
            }
        }

        if (executor.canBeMade(RobotType.GEODE)) {
            executor.doAction(RobotType.GEODE)
            val searchResult = solver(executor, minutesLeft - 1, maxToReach)
            cache[minutesLeft][executor.hashString()] = searchResult
            executor.undoAction(RobotType.GEODE)
            return searchResult
        }

        var bestResult = -1
        val actionList = executor.generateActionList()
        for (robotType in actionList) {
            executor.doAction(robotType)
            val searchResult = solver(executor, minutesLeft - 1, max(maxToReach, bestResult))
            if (searchResult > bestResult) {
                bestResult = searchResult
            }
            executor.undoAction(robotType)
        }
        cache[minutesLeft][executor.hashString()] = bestResult

        return bestResult
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Executor(private val bluePrint: BluePrint) {
    private var oreRobotCount = 1
    private var clayRobotCount = 0
    private var obsidianRobotCount = 0
    var geodeRobotCount = 0

    private var oreCount = 0
    private var clayCount = 0
    private var obsidianCount = 0
    var geodeCount = 0

    fun hashString() = "$oreRobotCount,$clayRobotCount,$obsidianRobotCount,$geodeRobotCount,$oreCount,$clayCount,$obsidianCount,$geodeCount"

    fun generateActionList() : List<RobotType> {
        val result = mutableListOf<RobotType>()
        for (robotType in RobotType.values()) {
            if (this.canBeMade(robotType)) {
                result.add(robotType)
            }
        }
        return result
    }

    fun doAction(robotType: RobotType) {
        preMakeRobot(robotType)
        collect()
        postMakeRobot(robotType)
    }

    fun undoAction(robotType: RobotType) {
        unpostMakeRobot(robotType)
        uncollect()
        unpreMakeRobot(robotType)
    }

    fun canBeMade(robotType: RobotType) : Boolean {
        return when (robotType) {
            RobotType.NONE -> true
            RobotType.ORE -> bluePrint.oreRobot.canBeMade(oreCount)
            RobotType.CLAY -> bluePrint.clayRobot.canBeMade(oreCount)
            RobotType.OBSIDIAN -> bluePrint.obsidianRobot.canBeMade(oreCount, clayCount)
            RobotType.GEODE -> bluePrint.geodeRobot.canBeMade(oreCount, obsidianCount)
        }
    }

    private fun preMakeRobot(robotType: RobotType) {
        when (robotType) {
            RobotType.NONE -> {} //no-op
            RobotType.ORE ->
                oreCount -= bluePrint.oreRobot.oreCosts
            RobotType.CLAY ->
                oreCount -= bluePrint.clayRobot.oreCosts
            RobotType.OBSIDIAN -> {
                oreCount -= bluePrint.obsidianRobot.oreCosts
                clayCount -= bluePrint.obsidianRobot.clayCosts
            }
            RobotType.GEODE -> {
                oreCount -= bluePrint.geodeRobot.oreCosts
                obsidianCount -= bluePrint.geodeRobot.obsidianCosts
            }
        }
    }

    private fun postMakeRobot(robotType: RobotType) {
        when (robotType) {
            RobotType.NONE -> {} //no-op
            RobotType.ORE -> oreRobotCount++
            RobotType.CLAY -> clayRobotCount++
            RobotType.OBSIDIAN -> obsidianRobotCount++
            RobotType.GEODE -> geodeRobotCount++
        }
    }

    private fun collect() {
        oreCount += oreRobotCount
        clayCount += clayRobotCount
        obsidianCount += obsidianRobotCount
        geodeCount += geodeRobotCount
    }

    private fun unpreMakeRobot(robotType: RobotType) {
        when (robotType) {
            RobotType.NONE -> {} //no-op
            RobotType.ORE ->
                oreCount += bluePrint.oreRobot.oreCosts
            RobotType.CLAY ->
                oreCount += bluePrint.clayRobot.oreCosts
            RobotType.OBSIDIAN -> {
                oreCount += bluePrint.obsidianRobot.oreCosts
                clayCount += bluePrint.obsidianRobot.clayCosts
            }
            RobotType.GEODE -> {
                oreCount += bluePrint.geodeRobot.oreCosts
                obsidianCount += bluePrint.geodeRobot.obsidianCosts
            }
        }
    }

    private fun unpostMakeRobot(robotType: RobotType) {
        when (robotType) {
            RobotType.NONE -> {} //no-op
            RobotType.ORE -> oreRobotCount--
            RobotType.CLAY -> clayRobotCount--
            RobotType.OBSIDIAN -> obsidianRobotCount--
            RobotType.GEODE -> geodeRobotCount--
        }
    }
    private fun uncollect() {
        oreCount -= oreRobotCount
        clayCount -= clayRobotCount
        obsidianCount -= obsidianRobotCount
        geodeCount -= geodeRobotCount
    }

    fun print() {
        print("  Grondstof ore : $oreCount, clay: $clayCount, obsidian: $obsidianCount, geode: $geodeCount   ")
        println("  Robot     ore : $oreRobotCount, clay: $clayRobotCount, obsidian: $obsidianRobotCount, geode: $geodeRobotCount")
    }
}

class BluePrint(inputStr: String) {
    val number = inputStr.substringAfter("Blueprint ").substringBefore(": Each ore robot costs").toInt()
    val oreRobot = OreRobot(inputStr)
    val clayRobot = ClayRobot(inputStr)
    val obsidianRobot = ObsidianRobot(inputStr)
    val geodeRobot = GeodeRobot(inputStr)

    fun print() {
        println ("$number: ${oreRobot.oreCosts}  ${clayRobot.oreCosts}  (${obsidianRobot.oreCosts}, ${obsidianRobot.clayCosts})  (${geodeRobot.oreCosts}, ${geodeRobot.obsidianCosts})")
    }

}

class OreRobot(inputStr: String) {
    val oreCosts = inputStr.substringAfter("Each ore robot costs ").substringBefore(" ore. Each clay robot costs").toInt()

    fun canBeMade(oreCount: Int): Boolean {
        return oreCosts <= oreCount
    }
}

class ClayRobot(inputStr: String) {
    val oreCosts = inputStr.substringAfter("Each clay robot costs ").substringBefore(" ore. Each obsidian robot costs").toInt()

    fun canBeMade(oreCount: Int): Boolean {
        return oreCosts <= oreCount
    }
}

class ObsidianRobot(inputStr: String) {
    val oreCosts = inputStr.substringAfter("Each obsidian robot costs ").substringBefore(" ore and ").toInt()
    val clayCosts = inputStr.substringAfter(" ore and ").substringBefore(" clay. Each geode robot costs ").toInt()

    fun canBeMade(oreCount: Int, clayCount: Int): Boolean {
        return oreCosts <= oreCount && clayCosts <= clayCount
    }
}

class GeodeRobot(inputStr: String) {
    val oreCosts = inputStr.substringAfter("Each geode robot costs ").substringBefore(" ore and ").toInt()
    val obsidianCosts = inputStr.substringAfter("Each geode robot costs ").substringAfter(" ore and ").substringBefore(" obsidian.").toInt()

    fun canBeMade(oreCount: Int, obsidianCount: Int): Boolean {
        return oreCosts <= oreCount && obsidianCosts <= obsidianCount
    }
}

enum class RobotType {
    GEODE, OBSIDIAN, CLAY, ORE, NONE
}

