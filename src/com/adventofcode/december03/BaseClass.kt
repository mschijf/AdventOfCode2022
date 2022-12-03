package com.adventofcode.december03

class BaseClass(private val input: Input) {

    fun totalScore(): Int {
        return input.inputLines
            .map{Pair(it.substring(0, it.length/2), it.substring(it.length/2, it.length))}
            .map{appearsInBoth(it.first, it.second)}
            .map{toValue(it)}
            .sum()
    }

    fun totalScore2(): Int {
        return input.inputLines
            .chunked(3)
            .map { appearsInBoth(appearsInBoth(it[0], it[1]), it[2]) }
            .sumOf { toValue(it) }
    }

    private fun appearsInBoth(first: String, second: String): String {
        return first.filter { second.indexOf(it) >= 0 }
    }

    private fun toValue (s: String) : Int {
        if (s.lowercase() == s) {
            return s[0] - 'a' + 1
        } else {
            return s[0] - 'A' + 27
        }
    }

}
