package org.example.chess.domain.usecase

import org.example.chess.data.repository.ChessBoardRepository
import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.Square

class ValidateAndMakeMove(
    private val chessBoardRepository: ChessBoardRepository,
    private val validator: MoveValidator,
) {
    sealed class Result {
        data object Success : Result()
        data object Illegal : Result()
    }

    operator fun invoke(selectedPiece: Piece, from: Square, to: Square): Result {
        return if (validator.isLegal(selectedPiece, from, to)) {
            chessBoardRepository.movePiece(from.row, from.col, to.row, to.col)
            Result.Success
        } else {
            Result.Illegal
        }
    }
}
