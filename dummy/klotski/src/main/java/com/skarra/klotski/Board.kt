package com.skarra.klotski


class Board(
    private val pieces: List<Piece>,
    val squares: Array<Int>,
    var previousBoard: Board? = null
) {

    companion object {
        const val WIDTH = 4
        const val HEIGHT = 5
    }

    fun nextBoards(): List<Board> {
        val nextBoards = arrayListOf<Board>()
        for (piece in this.pieces) {
            val nextPieces = piece.nextPieces(this.squares)
            for (nextPiece in nextPieces) {
                nextBoards.add(makeMove(piece, nextPiece))
            }
        }
        return nextBoards
    }

    fun solved(trappedPieceLabel: Int, exitIndexes: Array<Int>): Boolean {
        var solved = true
        loop@ for (index in exitIndexes) {
            if (this.squares[index] != trappedPieceLabel) {
                solved = false
                break@loop
            }
        }
        return solved
    }

    private fun makeMove(piece: Piece, nextPiece: Piece): Board {
        val label = piece.label
        val newPieces = arrayListOf<Piece>()
        newPieces.addAll(pieces)
        val newSquares = arrayListOf<Int>()
        newSquares.addAll(squares)

        newPieces[label] = nextPiece

        for (occupiedPiece in piece.occupiedSquares) {
            newSquares[occupiedPiece] = -1
        }

        for (occupiedPiece in nextPiece.occupiedSquares) {
            newSquares[occupiedPiece] = label
        }

        return Board(newPieces, newSquares.toTypedArray())
    }
}