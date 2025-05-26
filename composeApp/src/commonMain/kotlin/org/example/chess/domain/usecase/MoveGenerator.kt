package org.example.chess.domain.usecase

import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.PieceColor
import org.example.chess.domain.model.PieceType
import org.example.chess.domain.model.Square

class MoveGenerator {

    /** Generate every legal target square for `piece` standing on `pieceSquare`. */
    fun generateMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square,
        enPassant: Square?,
    ): List<Square> = when (piece.type) {
        PieceType.PAWN -> pawnMoves(board, piece, pieceSquare, enPassant)
        PieceType.KNIGHT -> knightMoves(board, piece, pieceSquare)
        PieceType.BISHOP -> bishopMoves(board, piece, pieceSquare)
        PieceType.ROOK -> rookMoves(board, piece, pieceSquare)
        PieceType.QUEEN -> queenMoves(board, piece, pieceSquare)
        PieceType.KING -> kingMoves(board, piece, pieceSquare)
    }

    private val diagonalsDirection = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)
    private val orthogonalsDirection = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

    private fun inBoard(row: Int, col: Int) = row in 0..7 && col in 0..7

    private fun pawnMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square,
        enPassant: Square?,
    ): List<Square> {
        val rowDirection = if (piece.color == PieceColor.WHITE) -1 else 1
        val startRow = if (piece.color == PieceColor.WHITE) 6 else 1
        val moves = mutableListOf<Square>()

        // forward 1/2
        val oneRowStep = pieceSquare.row + rowDirection
        if (inBoard(oneRowStep, pieceSquare.col) && board[oneRowStep][pieceSquare.col] == null) {
            moves.add(Square(oneRowStep, pieceSquare.col))
            val twoRowStep = pieceSquare.row + 2 * rowDirection
            if (pieceSquare.row == startRow && board[twoRowStep][pieceSquare.col] == null) {
                moves.add(Square(twoRowStep, pieceSquare.col))
            }
        }

        // captures & en-passant
        for (columnDirection in listOf(-1, +1)) {
            val column = pieceSquare.col + columnDirection
            if (!inBoard(oneRowStep, column)) continue
            val target = board[oneRowStep][column]
            if (target != null && target.color != piece.color) { // normal capture
                moves += Square(oneRowStep, column)
            }
            // en-passant capture
            if (enPassant != null && enPassant.row == oneRowStep && enPassant.col == column) {
                moves += enPassant
            }
        }
        return moves
    }

    private fun knightMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square
    ): List<Square> {
        val jumps = listOf(
            2 to 1, 1 to 2, -2 to 1, -1 to 2,
            2 to -1, 1 to -2, -2 to -1, -1 to -2,
        )
        return jumps.map { (directionRow, directionColumn) ->
            Square(
                pieceSquare.row + directionRow,
                pieceSquare.col + directionColumn
            )
        }
            .filter { inBoard(it.row, it.col) }
            .filter { board[it.row][it.col]?.color != piece.color }
    }

    private fun bishopMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square,
    ): List<Square> {
        return slideMoves(board, piece, pieceSquare, diagonalsDirection)
    }

    private fun rookMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square,
    ): List<Square> {
        return slideMoves(board, piece, pieceSquare, orthogonalsDirection)
    }

    private fun queenMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square,
    ): List<Square> {
        return slideMoves(board, piece, pieceSquare, diagonalsDirection + orthogonalsDirection)
    }

    // bishop / rook / queen
    private fun slideMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square,
        directions: List<Pair<Int, Int>>
    ): List<Square> {
        val moves = mutableListOf<Square>()
        for ((directionRow, directionColumn) in directions) {
            var row = pieceSquare.row + directionRow
            var col = pieceSquare.col + directionColumn

            // slide in the given direction until hitting the edge of the board or another piece
            while (inBoard(row, col)) {
                val occupant = board[row][col]
                if (occupant == null) {
                    moves += Square(row, col)
                } else {
                    if (occupant.color != piece.color) { // capture
                        moves += Square(row, col)
                    }
                    break
                }
                row += directionRow
                col += directionColumn
            }
        }
        return moves
    }

    private fun kingMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        pieceSquare: Square
    ): List<Square> {
        return (diagonalsDirection + orthogonalsDirection).map { (directionRow, directionColumn) ->
            Square(
                pieceSquare.row + directionRow,
                pieceSquare.col + directionColumn
            )
        }
            .filter { inBoard(it.row, it.col) }
            .filter { board[it.row][it.col]?.color != piece.color }
        // castling not implemented yet
    }
}
