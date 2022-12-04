package com.adventofcode.general

import java.io.File

class Input(test: Boolean, dayOfMonth: Int) {
    private val path = "data"
    private val fileName = "input" + String.format("%02d", dayOfMonth) + "_" + if (test) "example" else "1"
    private val file = File("$path/$fileName")
    val inputLines = if (file.exists()) file.bufferedReader().readLines() else emptyList()
}