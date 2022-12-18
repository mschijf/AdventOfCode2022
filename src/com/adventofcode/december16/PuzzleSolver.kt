package com.adventofcode.december16

import com.adventofcode.PuzzleSolverAbstract
import java.util.*

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val valveList = input.inputLines.map{Valve(it)}
    private val valveMap = valveList.associate {it.name to it}
    private val startValve = valveMap["AA"]!!
    private val valveListEssential = valveList.filter{it.rate > 0}
    private val distanceMap = makeDistanceMap()

    override fun resultPartOne(): String {
//        valveList.forEach { it.print() }
        val optimal = solver(
            startValve,
            valveListEssential.toSet(),
            0,
            30
        )
        return optimal.toString()
    }

    override fun resultPartTwo(): String {
        var maxResult = 0
        for (setSize in 0 .. valveListEssential.size / 2) {
            val allCombinationsList = Combination.getCombinationList(valveListEssential.size, setSize)
            print("set-size $setSize - total combinations: ${allCombinationsList.size}. Progress (per 100)")
            allCombinationsList.forEachIndexed {index, combination ->
                if (index % 100 == 0) {
                    print(".")
                }
                val valveSetMan = combination.map {valveListEssential[it] }.toSet()
                val valveSetElephant = valveListEssential.minus(valveSetMan).toSet()

                val optimalMan = solver(startValve, valveSetMan, 0, 26)
                val optimalElephant = solver(startValve, valveSetElephant, 0, 26)

                val totalResult = optimalMan + optimalElephant
                if (totalResult > maxResult) {
                    maxResult = totalResult
                }
            }
            println()
        }
        return maxResult.toString()
    }

    private fun solver(previousValve: Valve, toBeVisited: Set<Valve>, minutesPassed: Int, maxSeconds: Int): Int {
        if (toBeVisited.isEmpty() || minutesPassed >= maxSeconds) {
            return 0
        }

        var maxValue = 0
        for (valve in toBeVisited) {
            // om hier te komen heeft tijd gekost, namelijk distance in minutes
            // om de valve te openen, kost ook tijd: 1
            val distanceInMinutes = distanceMap[previousValve.name]!![valve.name]!!
            val totalMinutesPassed = minutesPassed + distanceInMinutes + 1
            if (totalMinutesPassed < maxSeconds) {
                val pressureReleasedHere = (maxSeconds - totalMinutesPassed) * valve.rate
                val totalPressureAfterHere = solver(
                    valve,
                    toBeVisited - valve,
                    minutesPassed + distanceInMinutes + 1,
                    maxSeconds
                )
                if (totalPressureAfterHere + pressureReleasedHere > maxValue) {
                    maxValue = totalPressureAfterHere + pressureReleasedHere
                }
            }
        }
        return maxValue
    }

    private fun makeDistanceMap(): Map<String, Map<String, Int>> {
        val distanceMap = mutableMapOf<String, MutableMap<String, Int>>()
        valveListEssential.plus(startValve).forEach {fromValve ->
            distanceMap[fromValve.name] = mutableMapOf()
            valveListEssential.forEach {toValve ->
                distanceMap[fromValve.name]!![toValve.name] = getShortestPathSteps(fromValve, toValve)
            }
        }
        return distanceMap
    }

    private fun getShortestPathSteps(fromValve: Valve, toValve: Valve): Int {
        val queue : Queue<Valve> = LinkedList()
        val dMap = mutableMapOf<String, Int>()
        queue.add(fromValve)
        dMap[fromValve.name] = 0
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current == toValve)
                return dMap[current.name]!!

            current.neighbourValveList.forEach {
                val nbValve = valveMap[it]!!
                if (!dMap.contains(nbValve.name) ) {
                    dMap[nbValve.name] = dMap[current.name]!! + 1
                    queue.add(nbValve)
                }
            }
        }
        return 99999999
    }

    private fun print(dMap: Map<String, Map<String, Int>>) {
        dMap.keys.forEach {fromStr->
            print("${valveMap[fromStr]!!.name} -->  ")
            dMap[fromStr]!!.forEach { to ->
                print(" ${valveMap[to.key]!!.name} ${dMap[fromStr]!![to.key]}")
            }
            println()
        }
    }

}


//----------------------------------------------------------------------------------------------------------------------

class Valve(inputStr: String) { //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    val name: String = inputStr.substringAfter("Valve ").substringBefore("has flow rate").trim()
    val rate: Int = inputStr.substringAfter("has flow rate=").substringBefore(";").trim().toInt()
    val neighbourValveList : List<String> = inputStr.replace("valves", "valve")
        .substringAfter(" to valve ").split(", ")

    fun print() {
        println("VALVE $name: rate: $rate , neighbours: $neighbourValveList")
    }
}

internal object Combination {
    /* This code is contributed by Devesh Agrawal (and altered by myself) */

    private var allCombinationsList = mutableListOf<List<Int>>()
    private fun combinationUtil(arr: IntArray, data: IntArray, start: Int, end: Int, index: Int, r: Int) {
        if (index == r) {
            allCombinationsList.add(data.toList())
            return
        }

        var i = start
        while (i <= end && end - i + 1 >= r - index) {
            data[index] = arr[i]
            combinationUtil(arr, data, i + 1, end, index + 1, r)
            i++
        }
    }

    fun getCombinationList(size: Int, splitSize: Int): List<List<Int>> {
        val data = IntArray(splitSize)
        val arr = IntArray(size){it}
        allCombinationsList.clear()
        combinationUtil(arr, data, 0, size - 1, 0, splitSize)
        return allCombinationsList
    }
}

