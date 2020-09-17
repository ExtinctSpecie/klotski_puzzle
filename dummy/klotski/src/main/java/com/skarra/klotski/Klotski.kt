package com.skarra.klotski

import java.util.*

class Klotski(
    private val trappedPieceLabel: Int,
    private val exitIndexes: Array<Int>,
    private val squares: Array<Int>,
    private val squareLabels: Map<Int, Char>
) {


    private val dequeStack: Deque<Board> = LinkedList()
    private val allBoards = mutableSetOf<String>()
    private val solvedBoards = mutableListOf<Board>()

    init {
        val initialBoard = initialBoard()
        dequeStack.addLast(initialBoard)
        allBoards.add(hashKey(initialBoard))
    }

    fun solve() {
        while (dequeStack.size > 0) {
            val currentBoard = dequeStack.pollFirst()
            val nextBoards = currentBoard.nextBoards()

            for (board in nextBoards) {
                if (hashKey(board) in allBoards) continue

                if (board.solved(trappedPieceLabel, exitIndexes)) solvedBoards.add(board)
                board.previousBoard = currentBoard
                dequeStack.addLast(board)
                allBoards.add(hashKey(board))
            }
        }
    }

    fun allSolvedRoutes(): List<List<Board>> {

        val allSolutions = arrayListOf<List<Board>>()

        solvedBoards.forEach {
            val innerSolutions = arrayListOf<Board>()
            var currentBoard: Board? = it

            while (currentBoard != null) {
                innerSolutions.add(currentBoard)
                currentBoard = currentBoard.previousBoard
            }

            allSolutions.add(innerSolutions)
        }

        return allSolutions
    }

    private fun hashKey(board: Board): String {
        val chars = arrayListOf<Char?>()
        for (s in board.squares) {
            chars.add(squareLabels[s])
        }
        return chars.joinToString()
    }

    private fun initialBoard(): Board {
        val pieces = arrayListOf<Piece>()

        for (x in squares.distinct().indices) {
            val occupiedSquares = arrayListOf<Int>()
            squares.forEachIndexed { index, i ->
                if (i == x) {
                    occupiedSquares.add(index)
                }
            }
            pieces.add(Piece(x, occupiedSquares))
        }

        return Board(pieces, squares)
    }
}