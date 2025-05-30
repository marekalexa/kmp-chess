class MoveValidator(private val generator: MoveGenerator) {

    /** Returns all fully-legal moves for the side to move. */
    fun legalMoves(
        board: Board,                // your 2D array alias
        side: PieceColor,
        moveHistory: List<Move>
    ): List<Move> =
        board.allPieces(side).flatMap { (piece, square) ->
            generator.generateMoves(board, piece, square, moveHistory)
                .filter { isLegal(board, it, moveHistory) }
        }

    /** Single-move legality test (used by UI when a player attempts a move). */
    fun isLegal(
        board: Board,
        move: Move,
        history: List<Move>
    ): Boolean {
        // 1. must be pseudo-legal
        if (move !in generator.generateMoves(board, move.piece, move.from, history)) return false

        // 2. make the move on a copy of the board
        val boardAfter = board.copy()          // deep copy or make/unmake pattern
        boardAfter.applyMove(move)

        // 3. king may not be in check
        val kingSquare = boardAfter.findKing(move.piece.color) ?: return false
        if (boardAfter.isSquareAttacked(kingSquare, move.piece.color.opponent)) return false

        // 4. castling extras (if move.special == CASTLING)
        if (move.special.isCastling) {
            val throughSquares =
                if (move.special == SpecialMove.CASTLING_KINGSIDE)
                    listOf(move.from, Square(move.from.row, 5), Square(move.from.row, 6))
                else
                    listOf(move.from, Square(move.from.row, 3), Square(move.from.row, 2))
            if (throughSquares.any { board.isSquareAttacked(it, move.piece.color.opponent) })
                return false
        }
        return true
    }
}
