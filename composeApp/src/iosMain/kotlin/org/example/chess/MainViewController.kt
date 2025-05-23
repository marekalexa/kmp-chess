package org.example.chess

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import org.example.chess.di.initDI
import org.example.chess.ui.viewmodel.ChessViewModel
import org.kodein.di.compose.withDI
import org.kodein.di.direct
import org.kodein.di.instance
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val di = initDI()
    return ComposeUIViewController {
        withDI(di) {
            val viewModel = remember { di.direct.instance<ChessViewModel>() }
            DisposableEffect(Unit) {
                onDispose { viewModel.clear() }
            }
            App(viewModel)
        }
    }
}
