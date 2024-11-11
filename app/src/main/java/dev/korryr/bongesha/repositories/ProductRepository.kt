package dev.korryr.bongesha.repositories

import dev.korryr.bongesha.viewmodels.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductRepository {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun clearData() {
        _products.value = emptyList()  // Clear all product data
    }
}