package com.adventofcode.mylambdas


fun CharSequence.distinct(): String {
    val result = StringBuilder()
    this.forEach { ch -> if (!result.contains(ch)) result.append(ch) }
    return result.toString()
}
