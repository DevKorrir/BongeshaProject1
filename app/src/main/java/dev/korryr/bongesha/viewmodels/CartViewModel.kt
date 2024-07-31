package dev.korryr.bongesha.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import dev.korryr.bongesha.commons.CartItem
import dev.korryr.bongesha.commons.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CartViewModel : ViewModel() {


    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get()= _cartItems

    fun addToCart(
        item: Item,
        quantity: Int
    ) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.item.id == item.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentItems.add(CartItem(item, quantity))

        }
        _cartItems.value = currentItems
    }
}