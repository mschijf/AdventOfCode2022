package com.adventofcode.general

import java.io.File

class Input(test: Boolean, dayOfMonth: Int) {
    private val fileName = "input" + String.format("%02d", dayOfMonth) + "_" + if (test) "example" else "1"
    val inputLines = File("data/$fileName").bufferedReader().readLines()
}