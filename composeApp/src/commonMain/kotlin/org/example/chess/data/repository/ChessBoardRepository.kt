package org.example.chess.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.PieceColor
import org.example.chess.domain.model.PieceType
import kotlin.random.Random
import org.example.chess.domain.model.Move
import org.example.chess.domain.model.Square
import org.example.chess.domain.usecase.MoveGenerator
import org.example.chess.domain.model.SpecialMove

private const val CHESS_BOARD_SIZE = 8

class ChessBoardRepository {

    private val _board = MutableStateFlow(initBoard())
    val board: StateFlow<Array<Array<Piece?>>> = _board

    private val _moveHistory = mutableListOf<Move>()
    val moveHistory: List<Move> get() = _moveHistory

    private val moveGenerator = MoveGenerator()

    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int, promotion: PieceType? = null) {
        val current = _board.value.map { it.copyOf() }.toTypedArray()
        val piece = current[fromRow][fromCol] ?: return
        val moveHistorySnapshot = _moveHistory.toList()
        val possibleMoves = moveGenerator.generateMoves(current, piece, Square(fromRow, fromCol), moveHistorySnapshot)
        val move = possibleMoves.find { it.to == Square(toRow, toCol) && (promotion == null || it.promotion == promotion) } ?: return
        // Handle special moves
        when (move.special) {
            SpecialMove.CASTLING_KINGSIDE -> {
                // Move rook as well
                val rookFromCol = 7
                val rookToCol = 5
                val row = fromRow
                current[row][rookToCol] = current[row][rookFromCol]
                current[row][rookFromCol] = null
            }
            SpecialMove.CASTLING_QUEENSIDE -> {
                val rookFromCol = 0
                val rookToCol = 3
                val row = fromRow
                current[row][rookToCol] = current[row][rookFromCol]
                current[row][rookFromCol] = null
            }
            SpecialMove.EN_PASSANT -> {
                // Remove the captured pawn
                val dir = if (piece.color == PieceColor.WHITE) 1 else -1
                current[toRow + dir][toCol] = null
            }
            else -> {}
        }
        // Normal move
        current[toRow][toCol] = if (promotion != null) piece.copy(type = promotion) else piece
        current[fromRow][fromCol] = null
        _moveHistory.add(move)
        _board.value = current
    }

    fun reset() {
        _board.value = initBoard()
        _moveHistory.clear()
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
