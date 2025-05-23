package org.example.chess.ui.viewmodel

import kotlinx.coroutines.CoroutineScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect abstract class BaseViewModel() {
    /** Scope tied to the ViewModel’s lifetime */
    val myViewModelScope: CoroutineScope

    /** Call when the ViewModel should be destroyed */
    open fun clear()
}
