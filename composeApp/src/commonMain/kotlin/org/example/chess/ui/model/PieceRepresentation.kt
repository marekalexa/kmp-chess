package org.example.chess.ui.model

import kmp_chess.composeapp.generated.resources.Res
import kmp_chess.composeapp.generated.resources.black_bishop
import kmp_chess.composeapp.generated.resources.black_king
import kmp_chess.composeapp.generated.resources.black_knight
import kmp_chess.composeapp.generated.resources.black_pawn
import kmp_chess.composeapp.generated.resources.black_queen
import kmp_chess.composeapp.generated.resources.black_rook
import kmp_chess.composeapp.generated.resources.white_bishop
import kmp_chess.composeapp.generated.resources.white_king
import kmp_chess.composeapp.generated.resources.white_knight
import kmp_chess.composeapp.generated.resources.white_pawn
import kmp_chess.composeapp.generated.resources.white_queen
import kmp_chess.composeapp.generated.resources.white_rook
import org.example.chess.domain.model.Piece
import org.example.chess.domain.model.PieceColor
import org.example.chess.domain.model.PieceType
import org.jetbrains.compose.resources.DrawableResource

data class PieceRepresentation(
    val name: String,
    val drawable: DrawableResource
)

fun Piece.getDrawableRes(): DrawableResource {
    return when (this.color to this.type) {
        PieceColor.BLACK to PieceType.ROOK -> Res.drawable.black_rook
        PieceColor.BLACK to PieceType.KNIGHT -> Res.drawable.black_knight
        PieceColor.BLACK to PieceType.BISHOP -> Res.drawable.black_bishop
        PieceColor.BLACK to PieceType.QUEEN -> Res.drawable.black_queen
        PieceColor.BLACK to PieceType.KING -> Res.drawable.black_king
        PieceColor.BLACK to PieceType.PAWN -> Res.drawable.black_pawn

        PieceColor.WHITE to PieceType.ROOK -> Res.drawable.white_rook
        PieceColor.WHITE to PieceType.KNIGHT -> Res.drawable.white_knight
        PieceColor.WHITE to PieceType.BISHOP -> Res.drawable.white_bishop
        PieceColor.WHITE to PieceType.QUEEN -> Res.drawable.white_queen
        PieceColor.WHITE to PieceType.KING -> Res.drawable.white_king
        PieceColor.WHITE to PieceType.PAWN -> Res.drawable.white_pawn

        else -> error("Unknown piece: $this")
    }
}

fun Piece.toRepresentation(): PieceRepresentation {
    val name = "${color.name.lowercase()} ${type.name.lowercase()}"
    val drawable = getDrawableRes() // same as before

    return PieceRepresentation(name = name, drawable = drawable)
}
