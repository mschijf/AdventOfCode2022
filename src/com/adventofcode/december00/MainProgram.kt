package com.adventofcode.december00

fun main() {
    MainProgram().MainProgram()
}

class MainProgram {
    private val test = false

    fun MainProgram() {
        val fileName = getFileName(test)
        val input = Input(fileName)
        input.getInputList().forEach { println(it) }
        val baseClass = input.getBaseClass()
        val output = run()
        println("Puzzle output : $output")
    }

    fun run(): Long {
        return 0
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