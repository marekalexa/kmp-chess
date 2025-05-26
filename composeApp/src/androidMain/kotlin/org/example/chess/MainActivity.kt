package org.example.chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.chess.data.repository.ChessBoardRepository
import org.example.chess.di.initDI
import org.example.chess.domain.usecase.MoveGenerator
import org.example.chess.domain.usecase.MoveValidator
import org.example.chess.domain.usecase.ValidateAndMakeMove
import org.example.chess.ui.viewmodel.ChessViewModel
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI

class MainActivity : ComponentActivity() {
    private val di = initDI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withDI(di) {
                val viewModel by rememberInstance<ChessViewModel>()
                App(viewModel)
            }
        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App(
        ChessViewModel(
            ChessBoardRepository(),
            ValidateAndMakeMove(
                ChessBoardRepository(),
                MoveValidator(MoveGenerator())
            )
        )
    )
}