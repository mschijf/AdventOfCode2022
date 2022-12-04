package com.adventofcode

import java.io.File

class Input(test: Boolean, dayOfMonth: Int) {
    private val path = "data/december${String.format("%02d", dayOfMonth)}"
    private val fileName = if (test) "example" else "input"
    private val file = File("$path/$fileName")

    val inputLines = if (file.exists()) file.bufferedReader().readLines() else emptyList()
}