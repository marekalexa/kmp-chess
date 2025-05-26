package org.example.chess.domain.usecase

import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.Square

class MoveValidator(
    private val moveGenerator: MoveGenerator,
) {

    /**
     * Very naive example — just prevents moving to the same square
     * and blocks moving a null piece. Expand with full chess rules later.
     */
    fun isLegal(
        board: Array<Array<Piece?>>,
        piece: Piece,
        from: Square,
        to: Square,
    ): Boolean {
        if (from == to) return false
        val validMoves =
            moveGenerator.generateMoves(board, piece, from, null) // todo: handle en-passant
        return validMoves.contains(to)
    }
}
