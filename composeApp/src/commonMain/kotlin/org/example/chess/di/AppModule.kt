package org.example.chess.di

import org.example.chess.data.repository.ChessBoardRepository
import org.example.chess.domain.usecase.MoveGenerator
import org.example.chess.domain.usecase.MoveValidator
import org.example.chess.domain.usecase.ValidateAndMakeMove
import org.example.chess.ui.viewmodel.ChessViewModel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val appDI = DI {
    bindSingleton<ChessBoardRepository> { ChessBoardRepository() }
    bindSingleton<MoveValidator> {
        MoveValidator(
            moveGenerator = instance(),
        )
    }
    bindSingleton<MoveGenerator> {
        MoveGenerator()
    }
    bindSingleton<ValidateAndMakeMove> {
        ValidateAndMakeMove(
            chessBoardRepository = instance(),
            validator = instance()
        )
    }
    bindSingleton<ChessViewModel> {
        ChessViewModel(
            chessBoardRepository = instance(),
            validateAndMakeMove = instance()
        )
    }
}
