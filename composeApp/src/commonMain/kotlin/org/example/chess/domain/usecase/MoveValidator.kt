package org.example.chess.domain.usecase

import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.Square

class MoveValidator {

    /**
     * Very naive example — just prevents moving to the same square
     * and blocks moving a null piece. Expand with full chess rules later.
     */
    fun isLegal(
        piece: Piece,
        from: Square,
        to: Square
    ): Boolean {
        if (from == to) return false

        return true
    }
}
