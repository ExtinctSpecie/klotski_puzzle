package com.skarra.klotski

import com.skarra.klotski.Board.Companion.HEIGHT
import com.skarra.klotski.Board.Companion.WIDTH

class Piece(
    val label: Int,
    val occupiedSquares: ArrayList<Int>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Piece) return false

        return (other as? Piece?)?.let {
            it.label == this.label
                    && it.occupiedSquares == this.occupiedSquares
        } ?: false
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + label
        result = 31 * result + occupiedSquares.hashCode()
        return result
    }

    fun nextPieces(squares: Array<Int>): List<Piece> {
        val newPieces = arrayListOf<Piece>()
        val offsets = arrayListOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
        for (offset in offsets) {
            if (legalShift(squares, offset)) {
                newPieces.add(shift(offset))
            }
        }
        return newPieces
    }

    private fun shift(offset: Pair<Int, Int>): Piece {
        val newOccupiedSquares = arrayListOf<Int>()
        for (square in this.occupiedSquares) {
            val r = (square / WIDTH)
            val c = square % WIDTH
            val shiftedSquare = WIDTH * (r + offset.first) + (c + offset.second)
            newOccupiedSquares.add(shiftedSquare)
        }
        return Piece(label, newOccupiedSquares)
    }

    private fun legalShift(squares: Array<Int>, offset: Pair<Int, Int>): Boolean {
        for (occupiedSquare in occupiedSquares) {
            val r = occupiedSquare / WIDTH
            val c = occupiedSquare % WIDTH
            val sr = r + offset.first
            val sc = c + offset.second

            if (sr < 0 || sr >= HEIGHT) return false
            if (sc < 0 || sc >= WIDTH) return false
            if (squares[((WIDTH * sr) + sc)] !in listOf(-1, label)) return false
        }
        return true
    }
}