package org.example.chess.di

import org.example.chess.data.repository.ChessBoardRepository
import org.example.chess.ui.viewmodel.ChessViewModel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val appDI = DI {
    bindSingleton<ChessBoardRepository> { ChessBoardRepository() }
    bindSingleton<ChessViewModel> { ChessViewModel(instance()) }
}
