package dev.korryr.bongesha.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class CartViewModel : ViewModel() {
    private val _cart = mutableStateListOf<CartItem>()
    val cart: List<CartItem> get() = _cart

    // Track the total number of items in the cart
    var itemCount by mutableStateOf(0)
    private set

    fun addToCart(product: Product, quantity: Int) {
        val existingItem = _cart.find { it.product.id == product.id }
        if (existingItem ==null) {
            if (quantity <= product.itemCount) {
                _cart.add(CartItem(product, quantity))
            }
        } else {
            val newQuantity = existingItem.quantity + quantity
            if (newQuantity <= product.itemCount) {
                existingItem.quantity = newQuantity
            }
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
            if (newQuantity <= product.itemCount) {
                it.quantity = newQuantity
            }
            if (it.quantity <= 0) {
                _cart.remove(it) // so item be remove if it is 0
            }
        }
        updateItemCount()
    }

    fun calculateTotalPrice(): Int {
        return _cart.sumOf { (it.product.price * it.quantity).roundToInt() }
    }

    fun saveProductToUserAccount(context: Context, product: Product, quantity: Int) {
        val firestore = Firebase.firestore
        val userId = "userID" // Replace with the actual user ID from authentication
        val cartItem = mapOf(
            "id" to product.id,
            "name" to product.name,
            "price" to product.price,
            "quantity" to quantity,
            "totalPrice" to product.price * quantity,
            "timestamp" to System.currentTimeMillis(),
            "images" to product.images
        )

        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .add(cartItem)
            .addOnSuccessListener {
                Toast.makeText(context, "Product saved to user account", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to save product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}

data class CartItem(
    val product: Product,
    var quantity: Int
)
