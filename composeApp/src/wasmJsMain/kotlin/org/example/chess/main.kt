package org.example.chess

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.example.chess.di.initDI
import org.example.chess.ui.viewmodel.ChessViewModel
import org.kodein.di.compose.withDI
import org.kodein.di.direct
import org.kodein.di.instance

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val di = initDI()
    ComposeViewport(document.body!!) {
        withDI(di) {
            val viewModel = remember { di.direct.instance<ChessViewModel>() }
            DisposableEffect(Unit) {
                onDispose { viewModel.clear() }
            }
            App(viewModel)
        }
    }
}