package com.adventofcode.december19

import com.adventofcode.PuzzleSolverAbstract
import kotlin.math.E
import kotlin.math.absoluteValue
import kotlin.math.min

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    val bluePrintList = input.inputLines.map { BluePrint(it) }

    val cache = Array(25) {HashMap<String, Int>()}

    override fun resultPartOne(): String {
        var quality = 0
        for (bluePrint in bluePrintList) {
            val executor = Executor(bluePrint)
            print("Start Blueprint ${bluePrint.number} of ${bluePrintList.size}")
            cache.forEach { it.clear() }
            val gc = solver(executor, 24)
            println(" --> $gc")
            quality += (gc*bluePrint.number)
        }
        return quality.toString()
    }

    //2258 -- too low

    private fun solver(executor: Executor, minutesLeft: Int): Int {
        if (minutesLeft <= 0) {
            return executor.geodeCount
        }

        if (cache[minutesLeft].contains(executor.hashString())) {
            return cache[minutesLeft][executor.hashString()] ?: 0
        }

        if (minutesLeft == 2) {
            if (!executor.canBeMade(RobotType.GEODE)) {
                return executor.geodeCount + minutesLeft*executor.geodeRobotCount
            } else {
                return executor.geodeCount + minutesLeft*executor.geodeRobotCount + 1
            }
        }

        if (executor.canBeMade(RobotType.GEODE)) {
            executor.doAction(RobotType.GEODE)
            val geodeCollected = solver(executor, minutesLeft - 1)
            cache[minutesLeft][executor.hashString()] = geodeCollected
            executor.undoAction(RobotType.GEODE)
            return geodeCollected
        }

        var maxGeodeCollected = 0
        for (robotType in RobotType.values()) {
            if (executor.canBeMade(robotType)) {
                executor.doAction(robotType)
                val geodeCollected = solver(executor, minutesLeft - 1)
                cache[minutesLeft][executor.hashString()] = geodeCollected
                if (geodeCollected > maxGeodeCollected) {
                    maxGeodeCollected = geodeCollected
                }
                executor.undoAction(robotType)
            }
        }

        return maxGeodeCollected
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Executor(val bluePrint: BluePrint) {
    var oreRobotCount = 1
    var clayRobotCount = 0
    var obsidianRobotCount = 0
    var geodeRobotCount = 0

    var oreCount = 0
    var clayCount = 0
    var obsidianCount = 0
    var geodeCount = 0

    fun hashString() = "$oreRobotCount,$clayRobotCount,$obsidianRobotCount,$geodeRobotCount,$oreCount,$clayCount,$obsidianCount,$geodeCount"

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
            RobotType.ORE -> bluePrint.oreRobot.canBeMade(oreCount, clayCount, obsidianCount)
            RobotType.CLAY -> bluePrint.clayRobot.canBeMade(oreCount, clayCount, obsidianCount)
            RobotType.OBSIDIAN -> bluePrint.obsidianRobot.canBeMade(oreCount, clayCount, obsidianCount)
            RobotType.GEODE -> bluePrint.geodeRobot.canBeMade(oreCount, clayCount, obsidianCount)
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

    fun canBeMade(oreCount: Int, clayCount: Int, obsidianCount: Int): Boolean {
        return oreCosts <= oreCount
    }
}

class ClayRobot(inputStr: String) {
    val oreCosts = inputStr.substringAfter("Each clay robot costs ").substringBefore(" ore. Each obsidian robot costs").toInt()

    fun canBeMade(oreCount: Int, clayCount: Int, obsidianCount: Int): Boolean {
        return oreCosts <= oreCount
    }
}

class ObsidianRobot(inputStr: String) {
    val oreCosts = inputStr.substringAfter("Each obsidian robot costs ").substringBefore(" ore and ").toInt()
    val clayCosts = inputStr.substringAfter(" ore and ").substringBefore(" clay. Each geode robot costs ").toInt()

    fun canBeMade(oreCount: Int, clayCount: Int, obsidianCount: Int): Boolean {
        return oreCosts <= oreCount && clayCosts <= clayCount
    }
}

class GeodeRobot(inputStr: String) {
    val oreCosts = inputStr.substringAfter("Each geode robot costs ").substringBefore(" ore and ").toInt()
    val obsidianCosts = inputStr.substringAfter("Each geode robot costs ").substringAfter(" ore and ").substringBefore(" obsidian.").toInt()

    fun canBeMade(oreCount: Int, clayCount: Int, obsidianCount: Int): Boolean {
        return oreCosts <= oreCount && obsidianCosts <= obsidianCount
    }
}

enum class RobotType {
    GEODE, OBSIDIAN, CLAY, ORE, NONE
}

