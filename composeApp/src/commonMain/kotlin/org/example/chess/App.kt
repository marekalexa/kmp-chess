package org.example.chess

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.chess.ui.composable.ChessBoardWithLabels
import org.example.chess.ui.viewmodel.ChessViewModel

@Composable
fun App(viewModel: ChessViewModel) {
    val boardState by viewModel.uiPieces.collectAsState(initial = emptyList())

    MaterialTheme {
        Column(
            modifier = Modifier.safeContentPadding().fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = viewModel::scramble) { Text("Scramble pieces") }
            Button(onClick = viewModel::reset) { Text("Reset game") }
            Spacer(Modifier.size(8.dp))
            ChessBoardWithLabels(boardState)
        }
    }
}
