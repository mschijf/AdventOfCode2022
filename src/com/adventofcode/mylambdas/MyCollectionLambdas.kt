package com.adventofcode.mylambdas

public fun <T> Iterable<T>.splitByCondition(predicate: (T) -> Boolean): List<List<T>> {
    val result = ArrayList<List<T>>()
    var tmp = mutableListOf<T>()
    this.forEach {
        if (predicate(it)) {
            result.add(tmp)
            tmp = mutableListOf<T>()
        } else {
            tmp.add(it)
        }
    }
    return result
}
