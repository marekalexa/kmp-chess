package org.example.chess.ui.composable


import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.chess.domain.model.Square
import org.example.chess.ui.model.AnimatedPiece
import org.jetbrains.compose.resources.painterResource

private const val SQUARE_SIZE = 48  // size of one square
private const val PIECE_SIZE = 42  // size of one piece
private const val BOARD_DIMENSION = 8 // number of squares per row/column
private const val BOARD_SIZE = BOARD_DIMENSION * SQUARE_SIZE // total board size
private const val ANIMATION_DURATION_MS = 200

private val files = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
private val ranks = (8 downTo 1).toList()

@Composable
fun ChessBoard(
    pieces: List<AnimatedPiece>,
    selected: Square?,
    onSquareTap: (Square) -> Unit,
) {
    Box(Modifier.size(BOARD_SIZE.dp)) {
        BoardGrid(selected, onSquareTap)
        LabelsOverlay()
        AnimatedPiecesLayer(pieces)
    }
}

@Composable
private fun LabelsOverlay() {
    Box(Modifier.size((BOARD_SIZE).dp)) {
        for (row in 0 until BOARD_DIMENSION) {
            for (col in 0 until BOARD_DIMENSION) {
                val isDark = (row + col) % 2 == 0
                val textColor = if (isDark) {
                    Color.Gray
                } else {
                    Color.White
                }

                val x = (col * SQUARE_SIZE).dp
                val y = (row * SQUARE_SIZE).dp

                Box(
                    modifier = Modifier
                        .absoluteOffset(x = x, y = y)
                        .size(SQUARE_SIZE.dp)
                ) {
                    // rank label on the leftmost column
                    if (col == 0) {
                        Text(
                            text = ranks[row].toString(),
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            color = textColor,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(1.dp, 0.dp),
                        )
                    }
                    // file label on the bottom row
                    if (row == BOARD_DIMENSION - 1) {
                        Text(
                            text = files[col].toString(),
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            color = textColor,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(1.dp, 0.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BoardGrid(
    selected: Square?,
    onTap: (Square) -> Unit,
) {
    val lightSq = Color.White
    val darkSq = Color.Gray
    val selTint = Color.Yellow.copy(0.4f)

    Column {
        repeat(BOARD_DIMENSION) { row ->
            Row {
                repeat(BOARD_DIMENSION) { col ->
                    val isLight = (row + col) % 2 == 0
                    val bg = if (isLight) lightSq else darkSq
                    val highlighted = selected?.row == row && selected.col == col

                    Box(
                        Modifier
                            .size(SQUARE_SIZE.dp)
                            .background(bg)
                            .then(
                                if (highlighted) {
                                    Modifier.background(selTint)
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { onTap(Square(row, col)) }
                    )
                }
            }
        }
    }
}


@Composable
fun AnimatedPiecesLayer(pieces: List<AnimatedPiece>) {
    Box(Modifier.size((BOARD_SIZE).dp)) {
        pieces.forEach { piece ->
            key(piece.id) {
                val pieceOffset = (SQUARE_SIZE - PIECE_SIZE) / 2
                val xTargetValue = ((piece.col * SQUARE_SIZE) + pieceOffset).dp
                val x by animateDpAsState(
                    targetValue = xTargetValue,
                    animationSpec = TweenSpec(ANIMATION_DURATION_MS)
                )
                val yTargetValue = ((piece.row * SQUARE_SIZE) + pieceOffset).dp
                val y by animateDpAsState(
                    targetValue = yTargetValue,
                    animationSpec = TweenSpec(ANIMATION_DURATION_MS)
                )

                Image(
                    painter = painterResource(piece.representation.drawable),
                    contentDescription = null,
                    modifier = Modifier
                        .size(PIECE_SIZE.dp)
                        .absoluteOffset(x, y)
                )
            }
        }
    }
}
