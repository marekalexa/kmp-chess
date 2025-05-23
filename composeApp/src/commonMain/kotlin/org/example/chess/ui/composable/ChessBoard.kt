package org.example.chess.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.chess.ui.model.PieceRepresentation
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalResourceApi::class)
fun ChessBoard(boardState: Array<Array<PieceRepresentation?>>) {
    Column {
        boardState.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, piece ->
                    val isLightSquare = (rowIndex + colIndex) % 2 == 0

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(if (isLightSquare) Color.White else Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        piece?.let {
                            Image(
                                painter = painterResource(piece.drawable),
                                contentDescription = piece.name
                            )
                        }
                    }
                }
            }
        }

    }
}
