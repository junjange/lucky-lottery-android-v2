package junjange.feature.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import junjange.core.ui.base.BaseViewModel

class MainViewModel : BaseViewModel() {
        private val _uiState = MutableStateFlow(MainState())
        val uiState: StateFlow<MainState> = _uiState.asStateFlow()
    }
