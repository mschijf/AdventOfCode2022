package com.adventofcode.december20

import com.adventofcode.PuzzleSolverAbstract
import java.lang.Math.floorMod

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val list = input.inputLines.map { Pair(it.toInt(), false) }.toMutableList()

        var count =0
        val cycleLength = list.size
        var orgIndex = list.indexOfFirst { !it.second }
        while (orgIndex >= 0) {
            val orgValue = list[orgIndex].first
            val newPair = Pair(orgValue, true)
            list.removeAt(orgIndex)
            val newIndex = orgIndex + orgValue
            if (newIndex < 0) {
//                list.add(floorMod(newIndex, cycleLength) - ((-newIndex / cycleLength)+1) , newPair)
                list.add(floorMod(newIndex, cycleLength-1), newPair)
            } else if (newIndex > cycleLength) {
//                list.add(floorMod(newIndex, cycleLength) + (newIndex / cycleLength), newPair)
                list.add(floorMod(newIndex, cycleLength-1), newPair)
            }  else if (newIndex == 0) {
                list.add(newPair)
            } else {
                list.add(newIndex, newPair)
            }
            orgIndex = list.indexOfFirst { !it.second }
            count++
        }
        println("Count $count:     ${list.map { it.first }}")

        val index0 = list.indexOfFirst { it.first == 0 }
        val value1000 = list[(index0 + 1000) % cycleLength].first
        val value2000 = list[(index0 + 2000) % cycleLength].first
        val value3000 = list[(index0 + 3000) % cycleLength].first


        return (value1000 + value2000 + value3000).toString()
    }

    override fun resultPartTwo(): String {
        val decryptionKey = 811589153L
        var list = input.inputLines.mapIndexed { index, it -> Pair(it.toLong()*decryptionKey, index) }.toMutableList()
        val cycleLength = list.size


        repeat(10) {
            var index = 0
            var orgIndex = list.indexOfFirst { it.second == index}
            while (orgIndex >= 0) {
                val orgValue = list[orgIndex].first
                val newPair = Pair(orgValue, index)

                if (orgValue == 0L) {
                    list[orgIndex] = newPair
                } else {

                    list.removeAt(orgIndex)

                    val newIndex = orgIndex + orgValue
                    if (newIndex < 0) {
                        list.add(floorMod(newIndex, cycleLength - 1), newPair)
                    } else if (newIndex > cycleLength) {
                        list.add(floorMod(newIndex, cycleLength - 1), newPair)
                    } else if (newIndex == 0L) {
                        list.add(newPair)
                    } else {
                        list.add(newIndex.toInt(), newPair)
                    }
                }
                index++
                orgIndex = list.indexOfFirst { it.second == index }
            }

//            for (i in list.indices) {
//                list[i] = Pair(list[i].first, false)
//            }
        }


        val index0 = list.indexOfFirst { it.first == 0L }
        val value1000 = list[(index0 + 1000) % cycleLength].first
        val value2000 = list[(index0 + 2000) % cycleLength].first
        val value3000 = list[(index0 + 3000) % cycleLength].first


        return (value1000 + value2000 + value3000).toString()
    }


}

//----------------------------------------------------------------------------------------------------------------------
