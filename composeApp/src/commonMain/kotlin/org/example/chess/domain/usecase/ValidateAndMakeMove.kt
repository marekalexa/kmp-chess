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

    operator fun invoke(selectedPiece: Piece, from: Square, to: Square, promotion: org.example.chess.domain.model.PieceType? = null): Result {
        val board = chessBoardRepository.board.value
        val moveHistory = chessBoardRepository.moveHistory
        return if (validator.isLegal(board, selectedPiece, from, to, moveHistory)) {
            chessBoardRepository.movePiece(from.row, from.col, to.row, to.col, promotion)
            Result.Success
        } else {
            Result.Illegal
        }
    }
}
