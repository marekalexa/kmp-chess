package org.example.chess.ui.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.example.chess.data.repository.ChessBoardRepository
import org.example.chess.domain.model.Square
import org.example.chess.domain.usecase.ValidateAndMakeMove
import org.example.chess.ui.model.AnimatedPiece
import org.example.chess.ui.model.toRepresentation

class ChessViewModel(
    private val chessBoardRepository: ChessBoardRepository,
    private val validateAndMakeMove: ValidateAndMakeMove,
) : BaseViewModel() {

    private val _selected = MutableStateFlow<Square?>(null)
    val selected: StateFlow<Square?> = _selected

    val uiPieces: Flow<List<AnimatedPiece>> = chessBoardRepository.board.map { board ->
        board.flatMapIndexed { row, rowArray ->
            rowArray.mapIndexedNotNull { col, piece ->
                piece?.let {
                    AnimatedPiece(
                        id = it.id,
                        row = row,
                        col = col,
                        representation = it.toRepresentation()
                    )
                }
            }
        }
    }

    fun onSquareClicked(square: Square) {
        val currentSel = _selected.value
        val board = chessBoardRepository.board.value
        when (currentSel) {
            null -> {
                // 1st tap: select piece if any
                if (board[square.row][square.col] != null) _selected.value = square
            }

            square -> {
                // Tap the same square again → do nothing
            }

            else -> {
                // Tap another square -> try to move or deselect
                board[currentSel.row][currentSel.col]?.let { selectedPiece ->
                    val moveResult = validateAndMakeMove(selectedPiece, currentSel, square)
                    if (moveResult == ValidateAndMakeMove.Result.Success) {
                        // add markers for "last move" here?
                        // add taken piece to a collection?
                    }
                }
                _selected.value = null
            }
        }
    }

    fun reset() {
        chessBoardRepository.reset()
    }

    fun scramble() {
        chessBoardRepository.scramble()
    }
}
