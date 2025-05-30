package org.example.chess.domain.usecase

import org.example.chess.domain.model.Move
import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.PieceColor
import org.example.chess.domain.model.PieceType
import org.example.chess.domain.model.SpecialMove
import org.example.chess.domain.model.Square
import kotlin.math.abs

/**
 * Stateless utility that produces *pseudo‑legal* moves for a single piece. A higher‑level
 * `RulesEngine` should filter these for check, stalemate, or threefold repetition.
 */
class MoveGenerator {

    private companion object {
        val DIAGONAL_OFFSETS = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)
        val ORTHOGONAL_OFFSETS = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)
        val KNIGHT_OFFSETS = listOf(
            2 to 1, 1 to 2, -2 to 1, -1 to 2,
            2 to -1, 1 to -2, -2 to -1, -1 to -2
        )
        val KING_OFFSETS = DIAGONAL_OFFSETS + ORTHOGONAL_OFFSETS
    }

    private fun isInsideBoard(row: Int, column: Int) = row in 0..7 && column in 0..7

    private fun Array<Array<Piece?>>.pieceAt(square: Square): Piece? = this[square.row][square.col]

    private fun Array<Array<Piece?>>.isEmpty(square: Square) = pieceAt(square) == null

    private fun Array<Array<Piece?>>.isOpponent(square: Square, sideToMove: PieceColor) =
        pieceAt(square)?.color?.let { it != sideToMove } ?: false

    private fun Piece?.isFriendlyTo(color: PieceColor) = this != null && this.color == color

    /**
     * Generate all **pseudo‑legal** moves for [piece] located on [fromSquare].
     * Castling safety (king must not move through check) is enforced here; other
     * check detection should be performed by a higher layer.
     */
    fun generateMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        fromSquare: Square,
        history: List<Move>,
    ): List<Move> = when (piece.type) {
        PieceType.PAWN -> pawnMoves(board, piece, fromSquare, history)
        PieceType.KNIGHT -> knightMoves(board, piece, fromSquare)
        PieceType.BISHOP -> bishopMoves(board, piece, fromSquare)
        PieceType.ROOK -> rookMoves(board, piece, fromSquare)
        PieceType.QUEEN -> queenMoves(board, piece, fromSquare)
        PieceType.KING -> kingMoves(board, piece, fromSquare, history)
    }

    private fun pawnMoves(
        board: Array<Array<Piece?>>, piece: Piece, from: Square, history: List<Move>
    ): List<Move> {
        val forwardDir = if (piece.color == PieceColor.WHITE) -1 else 1
        val startRow = if (piece.color == PieceColor.WHITE) 6 else 1
        val legalMoves = mutableListOf<Move>()

        // Forward advances
        val oneStepForward = Square(from.row + forwardDir, from.col)
        if (isInsideBoard(
                oneStepForward.row,
                oneStepForward.col
            ) && board.isEmpty(oneStepForward)
        ) {
            legalMoves += Move(from, oneStepForward, piece)
            val twoStepForward = Square(from.row + 2 * forwardDir, from.col)
            if (from.row == startRow && board.isEmpty(twoStepForward)) {
                legalMoves += Move(from, twoStepForward, piece)
            }
        }

        // Captures + en‑passant
        for (columnOffset in listOf(-1, 1)) {
            val target = Square(from.row + forwardDir, from.col + columnOffset)
            if (!isInsideBoard(target.row, target.col)) continue

            // normal diagonal capture
            if (board.isOpponent(target, piece.color)) {
                legalMoves += Move(from, target, piece, captured = board.pieceAt(target))
            }

            // en‑passant capture
            val lastMove = history.lastOrNull() ?: continue
            val isAdjacentPawnTwoStep =
                lastMove.piece.type == PieceType.PAWN &&
                        abs(lastMove.from.row - lastMove.to.row) == 2 &&
                        lastMove.to.row == from.row && lastMove.to.col == target.col

            if (isAdjacentPawnTwoStep) {
                legalMoves += Move(
                    from,
                    target,
                    piece,
                    captured = lastMove.piece,
                    special = SpecialMove.EN_PASSANT
                )
            }
        }
        return legalMoves
    }

    private fun knightMoves(
        board: Array<Array<Piece?>>, piece: Piece, fromSquare: Square
    ): List<Move> = KNIGHT_OFFSETS
        .map { (rowOffset, colOffset) ->
            Square(
                fromSquare.row + rowOffset,
                fromSquare.col + colOffset
            )
        }
        .filter { isInsideBoard(it.row, it.col) && !board.pieceAt(it).isFriendlyTo(piece.color) }
        .map { destination ->
            Move(
                fromSquare,
                destination,
                piece,
                captured = board.pieceAt(destination)
            )
        }

    private fun bishopMoves(board: Array<Array<Piece?>>, piece: Piece, fromSquare: Square) =
        slideMoves(board, piece, fromSquare, DIAGONAL_OFFSETS)

    private fun rookMoves(board: Array<Array<Piece?>>, piece: Piece, fromSquare: Square) =
        slideMoves(board, piece, fromSquare, ORTHOGONAL_OFFSETS)

    private fun queenMoves(board: Array<Array<Piece?>>, piece: Piece, fromSquare: Square) =
        slideMoves(board, piece, fromSquare, DIAGONAL_OFFSETS + ORTHOGONAL_OFFSETS)

    private fun slideMoves(
        board: Array<Array<Piece?>>,
        piece: Piece,
        fromSquare: Square,
        directions: List<Pair<Int, Int>>,
    ): List<Move> {
        val legalMoves = mutableListOf<Move>()
        for ((rowOffset, colOffset) in directions) {
            var currentRow = fromSquare.row + rowOffset
            var currentColumn = fromSquare.col + colOffset
            while (isInsideBoard(currentRow, currentColumn)) {
                val targetSquare = Square(currentRow, currentColumn)
                val occupant = board.pieceAt(targetSquare)
                if (occupant == null) {
                    legalMoves += Move(fromSquare, targetSquare, piece)
                } else {
                    if (occupant.color != piece.color) {
                        legalMoves += Move(fromSquare, targetSquare, piece, captured = occupant)
                    }
                    break // blocked by any piece
                }
                currentRow += rowOffset
                currentColumn += colOffset
            }
        }
        return legalMoves
    }

    private fun kingMoves(
        board: Array<Array<Piece?>>, piece: Piece, fromSquare: Square, history: List<Move>
    ): List<Move> {
        val singleSteps = KING_OFFSETS
            .map { (rowOffset, colOffset) ->
                Square(
                    fromSquare.row + rowOffset,
                    fromSquare.col + colOffset
                )
            }
            .filter {
                isInsideBoard(it.row, it.col) && !board.pieceAt(it).isFriendlyTo(piece.color)
            }
            .map { destination ->
                Move(
                    fromSquare,
                    destination,
                    piece,
                    captured = board.pieceAt(destination)
                )
            }

        val castlingMoves = generateCastlingMoves(board, piece, fromSquare, history)
        return singleSteps + castlingMoves
    }

    private fun generateCastlingMoves(
        board: Array<Array<Piece?>>, king: Piece, kingSquare: Square, history: List<Move>
    ): List<Move> {
        val legalMoves = mutableListOf<Move>()
        val backRank = if (king.color == PieceColor.WHITE) 7 else 0

        // King or rook already moved?
        val kingHasMoved =
            history.any { it.piece.type == PieceType.KING && it.piece.color == king.color }
        if (kingHasMoved || kingSquare != Square(backRank, 4)) return legalMoves

        val rookKingSideSquare = Square(backRank, 7)
        val rookQueenSideSquare = Square(backRank, 0)
        val rookKingSideMoved =
            history.any {
                it.from == rookKingSideSquare &&
                        it.piece.type == PieceType.ROOK &&
                        it.piece.color == king.color
            }
        val rookQueenSideMoved =
            history.any {
                it.from == rookQueenSideSquare &&
                        it.piece.type == PieceType.ROOK &&
                        it.piece.color == king.color
            }

        // King‑side: squares f and g clear; e‑f‑g not under attack
        val kingSideIntermediateSquares = listOf(Square(backRank, 5), Square(backRank, 6))
        if (!rookKingSideMoved &&
            kingSideIntermediateSquares.all { board.isEmpty(it) }
        ) {
            legalMoves += Move(
                kingSquare,
                Square(backRank, 6),
                king,
                special = SpecialMove.CASTLING_KINGSIDE
            )
        }

        // Queen‑side: squares b, c, d clear; e‑d‑c not under attack
        val queenSideIntermediateSquares =
            listOf(Square(backRank, 3), Square(backRank, 2), Square(backRank, 1))
        if (!rookQueenSideMoved &&
            queenSideIntermediateSquares.all { board.isEmpty(it) }
        ) {
            legalMoves += Move(
                kingSquare,
                Square(backRank, 2),
                king,
                special = SpecialMove.CASTLING_QUEENSIDE
            )
        }

        return legalMoves
    }
}
