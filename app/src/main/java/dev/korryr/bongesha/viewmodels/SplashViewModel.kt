package dev.korryr.bongesha.viewmodels

// SplashViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplashViewModel : ViewModel() {

    // Represents whether the app is loading content
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadContent()
    }

    // Simulate loading content (replace with actual load operations as needed)
    private fun loadContent() {
        viewModelScope.launch {
            delay(3000) // Simulating a 3-second load time, adjust as needed
            _isLoading.value = false // Set to false once loading completes
        }
    }
}
