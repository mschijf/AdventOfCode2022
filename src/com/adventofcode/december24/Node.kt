package com.adventofcode.december24

class Node(
    val parent: Node?,
    private val elf: Pos,
    valley: Valley,
    pathLength: Int) {
    var children = emptyList<Node>()
    var value = evaluate(valley, elf, pathLength)

    private fun evaluate(valley: Valley, elfPos: Pos, pathLength: Int): Int {
        if (elfPos == valley.startPos) {
            return elfPos.distance(valley.endPos) + pathLength + 10000
        } else {
            return elfPos.distance(valley.endPos) + pathLength + 1
        }
    }

    fun findMostPromisingNode(): Node {
        if (children.isEmpty())
            return this
        return children.first{ch -> ch.value == this.value}
    }

    fun expand(valley: Valley, pathLength: Int) {
        val moveList = generateMoves(valley)
        children = moveList.map { newElfPos -> Node(this, newElfPos, valley, pathLength)}
    }

    fun update() {
        value = children.minOf { ch -> ch.value }
    }

    private fun generateMoves(valley: Valley): List<Pos> {
        val result = mutableListOf(elf)
        for (direction in Direction.values()) {
            if (valley.isFreeField(elf.row+direction.dRow, elf.col + direction.dCol)) {
                result.add(Pos(elf.row+direction.dRow, elf.col + direction.dCol))
            }
        }
        return result.sortedBy { move -> move.distance(valley.endPos) }
    }
}

class Tree(
    private val valley: Valley) {
    private val root = Node(null, valley.startPos, valley, 0)

    fun search(): Int {
        while (root.value != 0) {
            val pair = findMostPromisingNode()
            val mpn = pair.first
            val pathLength = pair.second
            expand(mpn, pathLength)
            update(mpn)
        }
        return optimalPathLength()
    }

    private fun optimalPathLength(): Int {
        var pathLength = 0
        var mpn = root
        while (mpn.children.isNotEmpty()) {
            mpn = mpn.findMostPromisingNode()
            pathLength++
        }
        return pathLength
    }

    private fun findMostPromisingNode(): Pair<Node, Int> {
        var pathLength = 0
        var mpn = root
        while (mpn.children.isNotEmpty()) {
            mpn = mpn.findMostPromisingNode()
            valley.doBlizzardMove()
            pathLength++
        }
        return mpn to pathLength
    }

    private fun expand(node: Node, pathLength: Int) {
        valley.doBlizzardMove()
        node.expand(valley, pathLength)
    }

    private fun update(node: Node) {
        var mpn: Node?  = node
        while (mpn != null) {
            mpn.update()
            mpn = mpn.parent
            valley.undoBlizzardMove()
        }
    }
}