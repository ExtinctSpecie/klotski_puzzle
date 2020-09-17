package com.skarra.klotski.presentation


val squares = arrayOf(
    3, 0, 0, 4,
    1, 0, 0, 2,
    1, 9, 9, 2,
    5, 6, 7, 8,
    5, -1, -1, 8
)

val map = mapOf(
    -1 to ' ',
    0 to 'S',
    1 to 't',
    2 to 't',
    3 to 's',
    4 to 's',
    5 to 't',
    6 to 's',
    7 to 's',
    8 to 't',
    9 to 'w'
)

const val exitPieceLabel = 0
val exitCoordinates = arrayOf(17, 18)