package org.example.chess.ui.composable


import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.chess.ui.model.AnimatedPiece
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val files = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')
private val ranks = (8 downTo 1).toList()

private const val SQUARE_SIZE = 48  // size of one square
private const val BOARD_DIMENSION = 8 // number of squares per row/column
private const val BOARD_SIZE = BOARD_DIMENSION * SQUARE_SIZE // total board size
private const val ANIMATION_DURATION = 500

@Composable
fun ChessBoardWithLabels(pieces: List<AnimatedPiece>) {
    Row {
        // Ranks on the left side (numbers 8 to 1)
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.height(BOARD_SIZE.dp)
        ) {
            ranks.forEach { rank ->
                Box(
                    modifier = Modifier
                        .size(SQUARE_SIZE.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = rank.toString(), fontSize = 12.sp)
                }
            }
        }

        // The board itself (grid + markers + pieces)
        Column {
            ChessBoard(pieces)

            // Files on the bottom (letters A to H)
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.width(BOARD_SIZE.dp)
            ) {
                files.forEach { file ->
                    Box(
                        modifier = Modifier.size(SQUARE_SIZE.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = file.toString(), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun ChessBoard(pieces: List<AnimatedPiece>) {
    Box(Modifier.size(BOARD_SIZE.dp)) {
        BoardGrid()
        AnimatedPiecesLayer(pieces)
    }
}


@Composable
@OptIn(ExperimentalResourceApi::class)
private fun BoardGrid() {
    val lightSq = Color.White
    val darkSq = Color.Gray
    Column {
        repeat(BOARD_DIMENSION) { row ->
            Row {
                repeat(BOARD_DIMENSION) { col ->
                    val light = (row + col) % 2 == 0
                    Box(
                        Modifier
                            .size(SQUARE_SIZE.dp)
                            .background(if (light) lightSq else darkSq)
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
                val x by animateDpAsState(
                    targetValue = (piece.col * SQUARE_SIZE).dp,
                    animationSpec = TweenSpec(ANIMATION_DURATION)
                )
                val y by animateDpAsState(
                    targetValue = (piece.row * SQUARE_SIZE).dp,
                    animationSpec = TweenSpec(ANIMATION_DURATION)
                )

                Image(
                    painter = painterResource(piece.representation.drawable),
                    contentDescription = null,
                    modifier = Modifier.size(SQUARE_SIZE.dp).absoluteOffset(x, y)
                )
            }
        }
    }
}
