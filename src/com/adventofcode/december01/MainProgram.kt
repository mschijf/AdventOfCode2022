package com.adventofcode.december01

fun main() {
    MainProgram().MainProgram()
}

class MainProgram {
    private val test = false

    fun MainProgram() {
        val fileName = getFileName(test)
        val input = Input(fileName)
        val baseClass = input.getBaseClass()
        //val output = baseClass.maxCalories()
        val output = baseClass.topThreeCalories()
        println("Puzzle output : $output")
    }

    //------------------------------------------------------------------------------------------------------------------

    private fun getFileName(test: Boolean): String {
        val day = getDayOfMonthFromClassName()
        return "input" + String.format("%02d", day) + "_" + if (test) "example" else "1"
    }

    private fun getDayOfMonthFromClassName(): Int {
        val monthName = "december"
        val i = this.javaClass.name.indexOf(monthName)
        val j = this.javaClass.name.lastIndexOf(".")
        val dayOfMonth = this.javaClass.name.substring(i + monthName.length, j)
        return dayOfMonth.toInt()
    }
}