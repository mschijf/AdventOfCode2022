package com.adventofcode.december24.version2

import java.lang.Integer.min

// alternative algorithm 2: bruteforce recursive . Works for example and for real input (cache necessary)
//   two speed-ups: 1. if we know we cannot get a faster path in the subtree, we stop
//                  2. cache for solved subtrees
//  furthermore: extra stop condition, to prevent running in cycles

class SearchRecursive(
    private val valley: Valley) {

    private val cache = mutableMapOf<Pair<Pos, Int>, Int>()

    fun solve(): Int {
        cache.clear()
        return search(valley.startPos, valley.endPos,0, 99999, emptyList())
    }

    fun solvePart2(): Int {
        cache.clear()
        val afterFirst = search(valley.startPos, valley.endPos, 0, 99999, emptyList())
        cache.clear()
        val afterSecond = search(valley.endPos, valley.startPos, afterFirst, 99999, emptyList())
        cache.clear()
        val afterThird = search(valley.startPos, valley.endPos, afterFirst+afterSecond, 99999, emptyList())
        return afterFirst + afterSecond + afterThird
    }

    private fun posInPath(pos: Pos, path: List<Pos>) : Boolean {
        if (pos !in path)
            return false
        for (i in (path.size - valley.windCycle) downTo 0 step valley.windCycle)
            if (path[i] == pos)
                return true
        return false
    }

    private fun search(pos: Pos, endPos: Pos, afterMinute: Int, minimum: Int, path: List<Pos>): Int {
        if (pos == endPos)
            return 0

        if (cache.contains(Pair(pos, afterMinute)))
            return cache[Pair(pos, afterMinute)]!!

        if (endPos.distance(pos) + 1 + afterMinute >= minimum)
            return 999999

        if ( posInPath(pos, path) )
            return 999999

        var localMin = 999999
        val newPositionCandidates = valley.generateMoves(pos, afterMinute+1).sortedBy { it.distance(endPos) }
        for (newPos in newPositionCandidates) {
            val stepsTillEnd = 1+search(newPos, endPos,afterMinute+1, min(localMin+afterMinute, minimum), path+pos )
            if (stepsTillEnd < localMin)
                localMin = stepsTillEnd
        }
        cache[Pair(pos, afterMinute)] = localMin
        return localMin
    }

}