package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import dev.korryr.bongesha.commons.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CartViewModel : ViewModel() {


    private val _cartItems = MutableStateFlow<List<Item>>(emptyList())
    val cartItems: StateFlow<List<Item>> get()= _cartItems

    fun addToCart(
        item: Item,
        ) {
        val currentItems = _cartItems.value.toMutableList() ?: mutableListOf()
        currentItems.add(item)
        _cartItems.value = currentItems
    }

    fun removeItemFromCart(itemId: String) {
        val currentItems = _cartItems.value.toMutableList() ?: mutableListOf()
        currentItems.removeAll { it.id == itemId }
        _cartItems.value = currentItems
    }
}