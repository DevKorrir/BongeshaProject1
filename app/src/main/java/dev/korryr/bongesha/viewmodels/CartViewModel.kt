package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import dev.korryr.bongesha.commons.CartItem
import dev.korryr.bongesha.commons.Item

class CartItemViewModel : ViewModel() {
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> get() = _cart

    private val _items = MutableStateFlow<List<Item>>(emptyList()) // Add your items list here
    val items: StateFlow<List<Item>> get() = _items

    private val _selectedItem = MutableStateFlow<Item?>(null)
    val selectedItem: StateFlow<Item?> get() = _selectedItem

    fun getItemById(itemId: String): Item? {
        return _items.value.find { it.id == itemId }
    }

    fun selectItem(itemId: String) {
        _selectedItem.value = getItemById(itemId)
    }

    fun addToCart(item: Item, quantity: Int = 1) {
        val currentCart = _cart.value.toMutableList()
        val existingItem = currentCart.find { it.item.id == item.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentCart.add(CartItem(item, quantity))
        }
        _cart.value = currentCart
    }
}
