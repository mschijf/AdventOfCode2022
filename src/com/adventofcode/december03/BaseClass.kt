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
        val tripleList = mutableListOf<Triple<String, String, String>>()
        for (i in 3..input.inputLines.size step 3) {
            tripleList.add(Triple(input.inputLines[i-3], input.inputLines[i-2], input.inputLines[i-1]))
        }
        return tripleList
            .map{appearsInBoth(appearsInBoth(it.first, it.second), it.third)}
            .map{toValue(it)}
            .sum()
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
