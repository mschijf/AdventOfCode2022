package com.adventofcode.december24.version2

class AStar(
    private val valley: Valley) {

    fun solve():Int {
        val tree = Tree(valley)
        return tree.search(valley.startPos, valley.endPos)
    }
}

class Tree(
    private val valley: Valley) {

    fun search(fromPos: Pos, toPos: Pos): Int {
        val root = Node(null, fromPos, toPos, 0)
        while (root.value in -999999 .. 999999) {
            val pair = findMostPromisingNode(root)
            val mpn = pair.first
            val pathLength = pair.second
            expand(mpn, pathLength, toPos)
            update(mpn)
        }
        return optimalPathLength(root)
    }

    private fun optimalPathLength(node: Node): Int {
        var pathLength = 0
        var mpn = node
        while (mpn.children.isNotEmpty()) {
            mpn = mpn.findMostPromisingNode()
            pathLength++
        }
        return pathLength
    }

    private fun findMostPromisingNode(node: Node): Pair<Node, Int> {
        var pathLength = 0
        var mpn = node
        while (mpn.children.isNotEmpty()) {
            mpn = mpn.findMostPromisingNode()
            pathLength++
        }
        return mpn to pathLength
    }

    private fun expand(node: Node, pathLength: Int, endPos: Pos) {
        node.expand(valley, pathLength, endPos)
    }

    private fun update(node: Node) {
        var mpn: Node?  = node
        while (mpn != null) {
            mpn.update()
            mpn = mpn.parent
        }
    }
}

class Node(
    val parent: Node?,
    private val elf: Pos,
    endPos: Pos,
    pathLength: Int) {
    var children = emptyList<Node>()
    var value = evaluate(elf, pathLength, endPos)

    private fun evaluate(elfPos: Pos, pathLength: Int, endPos: Pos): Int {
        if (elfPos == endPos) {
            return -1000000
        } else {
            return elfPos.distance(endPos) + pathLength
        }
    }

    fun findMostPromisingNode(): Node {
        if (children.isEmpty())
            return this
        return children.first{ch -> ch.value == this.value}
    }

    fun expand(valley: Valley, pathLength: Int, endPos: Pos) {
        val moveList = valley.generateMoves(elf, pathLength+1)
        children = moveList.map { newElfPos -> Node(this, newElfPos, endPos, pathLength+1)}
        if (children.isEmpty()) {
            value = 1000000
        }
    }

    fun update() {
        if (children.isNotEmpty()) {
            value = children.minOf { ch -> ch.value }
        }
    }
}

