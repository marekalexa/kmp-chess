package org.example.chess.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.PieceColor
import org.example.chess.domain.model.PieceType
import kotlin.random.Random

private const val CHESS_BOARD_SIZE = 8

class ChessBoardRepository {

    private val _board = MutableStateFlow(initBoard())
    val board: StateFlow<Array<Array<Piece?>>> = _board

    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        val current = _board.value.map { it.copyOf() }.toTypedArray()
        val piece = current[fromRow][fromCol] ?: return
        current[toRow][toCol] = piece
        current[fromRow][fromCol] = null
        _board.value = current
    }

    fun reset() {
        _board.value = initBoard()
    }

    private fun initBoard(): Array<Array<Piece?>> {
        val board = Array(CHESS_BOARD_SIZE) { Array<Piece?>(CHESS_BOARD_SIZE) { null } }

        board[0] = arrayOf(
            Piece(Random.nextInt().toString(),PieceType.ROOK, PieceColor.BLACK),
            Piece(Random.nextInt().toString(),PieceType.KNIGHT, PieceColor.BLACK),
            Piece(Random.nextInt().toString(),PieceType.BISHOP, PieceColor.BLACK),
            Piece(Random.nextInt().toString(),PieceType.QUEEN, PieceColor.BLACK),
            Piece(Random.nextInt().toString(),PieceType.KING, PieceColor.BLACK),
            Piece(Random.nextInt().toString(),PieceType.BISHOP, PieceColor.BLACK),
            Piece(Random.nextInt().toString(),PieceType.KNIGHT, PieceColor.BLACK),
            Piece(Random.nextInt().toString(),PieceType.ROOK, PieceColor.BLACK),
        )
        board[1] = Array(8) { Piece(Random.nextInt().toString(),PieceType.PAWN, PieceColor.BLACK) }

        board[6] = Array(8) { Piece(Random.nextInt().toString(),PieceType.PAWN, PieceColor.WHITE) }
        board[7] = arrayOf(
            Piece(Random.nextInt().toString(),PieceType.ROOK, PieceColor.WHITE),
            Piece(Random.nextInt().toString(),PieceType.KNIGHT, PieceColor.WHITE),
            Piece(Random.nextInt().toString(),PieceType.BISHOP, PieceColor.WHITE),
            Piece(Random.nextInt().toString(),PieceType.QUEEN, PieceColor.WHITE),
            Piece(Random.nextInt().toString(),PieceType.KING, PieceColor.WHITE),
            Piece(Random.nextInt().toString(),PieceType.BISHOP, PieceColor.WHITE),
            Piece(Random.nextInt().toString(),PieceType.KNIGHT, PieceColor.WHITE),
            Piece(Random.nextInt().toString(),PieceType.ROOK, PieceColor.WHITE),
        )
        return board
    }

    fun scramble() {
        val copiedBoard = _board.value.map { it.copyOf() }.toTypedArray()
        val squares = copiedBoard.flatten().shuffled()
        var index = 0

        for (i in 0 until CHESS_BOARD_SIZE) {
            for (j in 0 until CHESS_BOARD_SIZE) {
                copiedBoard[i][j] = squares[index]
                index++
            }
        }
        _board.value = copiedBoard
    }
}
