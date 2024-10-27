package dev.korryr.bongesha.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class CartViewModel : ViewModel() {
    private val _cart = mutableStateListOf<CartItem>()
    val cart: List<CartItem> get() = _cart

    // Track the total number of items in the cart
    var itemCount by mutableStateOf(0)
    private set

    fun addToCart(product: Product) {
        val existingItem = _cart.find { it.product.id == product.id }
        if (existingItem ==null) {
            _cart.add(CartItem(product, 1)) //add new product by i
        } else {
            existingItem.quantity++ //Increment Quantitty if already in the cart
        }
        updateItemCount()
     }

    fun removeFromCart(product: Product) {
        val existingItem = _cart.find { it.product.id == product.id }
        if (existingItem != null) {
            _cart.remove(existingItem)
        }
        updateItemCount()
    }

    // Toggle item in the cart (add or remove)
    fun toggleItemInCart(product: Product) {
        val existingItem = _cart.find { it.product.id == product.id }
        if (existingItem != null) {
            _cart.remove(existingItem)
        } else {
            _cart.add(CartItem(product, 1)) // Add new product with quantity 1
        }
        updateItemCount()
    }

    // Check if item is already in the cart
    fun isInCart(product: Product): Boolean {
        return _cart.any { it.product.id == product.id }
    }

    private fun updateItemCount() {
        itemCount = _cart.sumOf { it.quantity }
    }

    fun updateQuantity(product: Product, newQuantity: Int) {
        val cartItem = _cart.find { it.product.id == product.id }
        cartItem?.let {
            it.quantity = newQuantity
            if (it.quantity <= 0) {
                _cart.remove(it) // so item be remove if it is 0
            }
        }
    }


    fun calculateTotalPrice(): Int {
        return _cart.sumOf { (it.product.price * it.quantity).roundToInt() }
    }


}

data class CartItem(
    val product: Product,
    var quantity: Int
)
