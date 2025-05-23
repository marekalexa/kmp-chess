package org.example.chess.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class BaseViewModel {
    private val job = SupervisorJob()
    actual val myViewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    /** Call from UI when the VM is no longer needed */
    actual open fun clear() {
        job.cancel()
    }
}