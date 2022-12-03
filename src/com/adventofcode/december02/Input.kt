package com.adventofcode.december02

import java.io.File

class Input(fileName: String) {
    val inputLines = File("data/$fileName").bufferedReader().readLines()
}