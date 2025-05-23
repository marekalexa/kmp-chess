package org.example.chess.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.viewModelScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class BaseViewModel : ViewModel() {
    actual val myViewModelScope: CoroutineScope = viewModelScope

    override fun onCleared() {
        super.onCleared()
        clear()
    }

    actual open fun clear() = Unit
}
