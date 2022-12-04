package com.adventofcode.december01

import java.io.File

class Input(test: Boolean) {
    val fileName = getFileName(test)
    val inputLines = File("data/$fileName").bufferedReader().readLines()

    private fun getFileName(test: Boolean): String {
        val day = getDayOfMonthFromClassName()
        return "input" + String.format("%02d", day) + "_" + if (test) "example" else "1"
    }

    fun getDayOfMonthFromClassName(): Int {
        val monthName = "december"
        val i = this.javaClass.name.indexOf(monthName)
        val j = this.javaClass.name.lastIndexOf(".")
        val dayOfMonth = this.javaClass.name.substring(i + monthName.length, j)
        return dayOfMonth.toInt()
    }

}