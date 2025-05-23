package org.example.chess.ui.viewmodel

import kotlinx.coroutines.flow.map
import org.example.chess.data.repository.ChessBoardRepository
import org.example.chess.ui.model.toRepresentation

class ChessViewModel(
    private val repository: ChessBoardRepository,
): BaseViewModel() {
    val uiBoard = repository.board.map { board ->
        board.map { row -> row.map { it?.toRepresentation() }.toTypedArray() }.toTypedArray()
    }

    fun move(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        repository.movePiece(fromRow, fromCol, toRow, toCol)
    }

    fun reset() {
        repository.reset()
    }

    fun scramble() {
        repository.scramble()
    }
}
