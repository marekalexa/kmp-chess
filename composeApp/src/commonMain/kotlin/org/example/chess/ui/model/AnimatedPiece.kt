package org.example.chess.ui.model

data class AnimatedPiece(
    val id: String,
    val row: Int,
    val col: Int,
    val representation: PieceRepresentation,
)
