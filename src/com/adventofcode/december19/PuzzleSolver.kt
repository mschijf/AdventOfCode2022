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

//    override fun resultPartOne(): String {
//        var quality = 0
//        for (bluePrint in bluePrintList) {
//            val startTime = currentTimeMillis()
//            val executor = Executor(bluePrint)
//            print("Start Blueprint ${bluePrint.number} of ${bluePrintList.size}")
//
//            cache.forEach { it.clear() }
//            val gc = solver(executor, 24, -1, emptyList())
//            val timePassed = currentTimeMillis() - startTime
//            println(" --> $gc time: $timePassed ms")
//
//            quality += (gc.geodeCollected*bluePrint.number)
//        }
//        return quality.toString()
//    }

    //10064 --> too low

    override fun resultPartTwo(): String {
        var quality = 1
        for (bluePrint in bluePrintList.dropLast(max(0, bluePrintList.size-3))) {
            val startTime = currentTimeMillis()
            val executor = Executor(bluePrint)
            print("Start Blueprint ${bluePrint.number} of ${bluePrintList.size}")

            cache.forEach { it.clear() }
            val gc = solver(executor, 32, -1, emptyList())
            val timePassed = currentTimeMillis() - startTime
            println(" --> $gc time: $timePassed ms")

//            val startTime2 = currentTimeMillis()
//            val executor2 = Executor(bluePrint)
//            cache.forEach { it.clear() }
//            print("     nu tot 32: ${bluePrint.number} of ${bluePrintList.size}")
//            val gc2 = solver(executor2, 32, gc.geodeCollected, gc.path)
//            val timePassed2 = currentTimeMillis() - startTime2
//            println(" --> $gc2 time: $timePassed2 ms")

            quality *= (gc.geodeCollected)
        }
        return quality.toString()
    }

    private fun solver(executor: Executor, minutesLeft: Int, maxToReach: Int, suggestedActionPath: List<RobotType>): SearchInfo {
        if (minutesLeft <= 0) {
            return SearchInfo(executor.geodeCount, emptyList())
        }

        val maxPotentialGeode = (minutesLeft * (minutesLeft-1))/2
        if (executor.geodeCount + executor.geodeRobotCount * minutesLeft + maxPotentialGeode <= maxToReach) {
            return SearchInfo(-1, emptyList())
        }

        if (cache[minutesLeft].contains(executor.hashString())) {
            return SearchInfo(cache[minutesLeft][executor.hashString()] ?: -1, emptyList())
        }


//        if (minutesLeft == 2) {
//            if (!executor.canBeMade(RobotType.GEODE)) {
//                return SearchInfo(executor.geodeCount + minutesLeft*executor.geodeRobotCount,
//                    listOf(RobotType.NONE, RobotType.NONE))
//            } else {
//                return SearchInfo(executor.geodeCount + minutesLeft*executor.geodeRobotCount + 1,
//                    listOf(RobotType.NONE, RobotType.GEODE))
//            }
//        }
//
//        if (executor.canBeMade(RobotType.GEODE)) {
//            executor.doAction(RobotType.GEODE)
//            val searchResult = solver(executor, minutesLeft - 1, maxToReach, suggestedActionPath.drop(1))
//            cache[minutesLeft][executor.hashString()] = searchResult.geodeCollected
//            executor.undoAction(RobotType.GEODE)
//            return SearchInfo(searchResult.geodeCollected, listOf(RobotType.GEODE) + searchResult.path)
//        }

        var bestResult = SearchInfo(-1, emptyList())
        val actionList = executor.generateActionList(suggestedActionPath.firstOrNull()?:RobotType.GEODE)
        for (robotType in actionList) {
            executor.doAction(robotType)
            val searchResult = solver(executor, minutesLeft - 1, max(maxToReach, bestResult.geodeCollected), suggestedActionPath.drop(1))
            if (searchResult.better(bestResult)) {
                bestResult = SearchInfo(searchResult.geodeCollected, listOf(robotType) + searchResult.path)
            }
            executor.undoAction(robotType)
        }
        cache[minutesLeft][executor.hashString()] = bestResult.geodeCollected

        return bestResult
    }
}

class SearchInfo(val geodeCollected: Int, val path: List<RobotType>) {
    fun better(other: SearchInfo): Boolean = geodeCollected > other.geodeCollected
//    override fun toString() = "$geodeCollected \n  $path"
    override fun toString() = "$geodeCollected"
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

    fun generateActionList(suggestedAction: RobotType) : List<RobotType> {
        var result = mutableListOf<RobotType>()
        for (robotType in RobotType.values()) {
            if (this.canBeMade(robotType)) {
                if (robotType == suggestedAction) {
                    result.add(0, robotType)
                } else {
                    result.add(robotType)
                }
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

