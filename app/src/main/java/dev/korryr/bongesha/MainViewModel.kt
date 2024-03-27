package dev.korryr.bongesha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {

    private val  _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _products = MutableStateFlow(allproducts)
    val products = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_products) { text, products ->
            if (text.isBlank()){
                products
            } else {
                delay(2000L)
                products.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false }}
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _products.value
        )
    fun  onSearchTextChange(text: String) {
        _searchText.value = text
    }
}

data class Product(
    val firstName: String,
    val lastName: String
) {
    fun doesMatchSearchQuery(query: String): Boolean{
        val matchingCombainations = listOf(
            "$firstName$lastName"
        )

        return matchingCombainations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

private val allproducts = listOf(
    Product(
        firstName = "tecno",
        lastName = "camon"
    )
)