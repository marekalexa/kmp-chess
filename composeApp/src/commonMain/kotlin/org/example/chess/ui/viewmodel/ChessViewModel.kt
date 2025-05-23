package org.example.chess.ui.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.chess.data.repository.ChessBoardRepository
import org.example.chess.ui.model.AnimatedPiece
import org.example.chess.ui.model.toRepresentation

class ChessViewModel(
    private val repository: ChessBoardRepository,
) : BaseViewModel() {
    val uiPieces: Flow<List<AnimatedPiece>> = repository.board.map { board ->
        board.flatMapIndexed { row, rowArray ->
            rowArray.mapIndexedNotNull { col, piece ->
                piece?.let {
                    AnimatedPiece(
                        id = "${it.type}_${it.color}",
                        row = row,
                        col = col,
                        representation = it.toRepresentation()
                    )
                }
            }
        }
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
