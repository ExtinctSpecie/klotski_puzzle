package com.skarra.klotski

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BoardTest {


    private companion object {
        val squares = arrayOf(
            3, 0, 0, 4,
            1, 0, 0, 2,
            1, 9, 9, 2,
            5, 6, 7, 8,
            5, -1, -1, 8
        )

        const val exitPieceLabel = 0
        val exitCoordinates = arrayOf(17, 18)
    }


    lateinit var sut: Board

    @Before
    fun initialize() {
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

        sut = Board(pieces, squares)
    }

    @Test
    fun `nextBoards SHOULD return 4 new boards`() {
        val result = sut.nextBoards()

        assertThat(result.size).isEqualTo(6)
        assertThat(result.get(0).nextBoards().size).isEqualTo(8)
    }

    @Test
    fun `solved SHOULD return false for the initial board`() {
        val result = sut.solved(exitPieceLabel, exitCoordinates)

        assertThat(result).isFalse()
    }

    @Test
    fun `solved SHOULD return true if board is solved`() {
        val result = sut.solved(-1, exitCoordinates)

        assertThat(result).isTrue()
    }
}