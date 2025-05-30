package org.example.chess.domain.model

enum class SpecialMove { NONE, CASTLING_KINGSIDE, CASTLING_QUEENSIDE, EN_PASSANT }

data class Move(
    val from: Square,
    val to: Square,
    val piece: Piece,
    val captured: Piece? = null,
    val promotion: PieceType? = null,
    val special: SpecialMove = SpecialMove.NONE
) 