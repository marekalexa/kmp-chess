package org.example.chess.di

import org.kodein.di.DI

/** Call once per platform and reuse the same DI instance. */
fun initDI(): DI = appDI
