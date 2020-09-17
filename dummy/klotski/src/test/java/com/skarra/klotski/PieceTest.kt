package com.skarra.klotski

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PieceTest {
    private companion object {
        val squares = arrayOf(
            3, 0, 0, 4,
            1, 0, 0, 2,
            1, 9, 9, 2,
            5, 6, 7, 8,
            5, -1, -1, 8
        )
    }

    @Test
    fun `nextPieces SHOULD return an empty list`() {
        val sut = Piece(4, arrayListOf(1, 2, 5, 6))
        val result = sut.nextPieces(squares)

        assertThat(result).isEmpty()
    }

    @Test
    fun `nextPieces SHOULD return next positions`() {
        val sut = Piece(6, arrayListOf(13))
        val result = sut.nextPieces(squares)

        assertThat(result).isNotEmpty()
        assertThat(result[0].label).isEqualTo(6)
        assertThat(result[0].occupiedSquares).isEqualTo(arrayListOf(17))
    }
}