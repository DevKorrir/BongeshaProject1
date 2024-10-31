package dev.korryr.bongesha.viewmodels

import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.repositories.ProductRepository
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CartViewModel : ViewModel() {
    private val _cart = mutableStateListOf<Product>()
    val cart: List<Product> get() = _cart
    var quantityCount by mutableIntStateOf(0)
    private set
    private var deliveryFee: Int by mutableIntStateOf(0)

    private val firestore = Firebase.firestore

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        fetchCartItems()
    }

    fun updateDeliveryFee(fee: Int) {
        deliveryFee = fee
    }

    fun calculateDeliveryFee(location: Location?): Int {
        // Define a base location, such as the warehouse/store location
        val baseLatitude = -0.64
        val baseLongitude = 35.18
        val baseLocation = Location("base").apply {
            latitude = baseLatitude
            longitude = baseLongitude
        }

        return if (location != null) {
            // Calculate distance in kilometers
            val distanceInKm = location.distanceTo(baseLocation) / 1000

            // Calculate fee based on distance with a base and cap
            when {
                distanceInKm <= 10 -> 50 // Base fee for short distances
                distanceInKm <= 50 -> (distanceInKm * 1.5).toInt() // Medium distance fee
                else -> 300 // Cap fee for long distances
            }
        } else {
            // Default fee if location is unavailable
            100
        }
    }

    fun addToCart(product: Product, quantity: Int) {
        val existingItem = _cart.find { it.id == product.id }
        if (product.quantityCount <= 0) {
            // Item is out of stock; skip adding to cart and display an appropriate message in the UI.
            return
        }
        if (existingItem != null) {
            // Update the quantity only if it won't exceed available stock
            val newQuantity = (existingItem.quantity + quantity).coerceAtMost(product.quantityCount)
            existingItem.quantity = newQuantity
        } else {
            // Add the item if not already in cart
            _cart.add(product.copy(quantity = quantity.coerceAtMost(product.quantityCount)))
        }
        updateProductCount()
    }



    fun addToCartttttt(product: Product, quantity: Int) {
        val existingItem = _cart.find { it.id  == product.id }
        if (existingItem != null) {
            // If the product is already in the cart, update the quantity
            val newQuantity = existingItem.quantity + quantity
            if (newQuantity <= product.quantityCount) {
                existingItem.quantity = newQuantity
            } else {
                // Optional: Handle case if newQuantity exceeds available stock
            }
        } else {
            // If the product is not in the cart, add it as a new item
            if (quantity <= product.quantityCount) {
                _cart.add(product.copy(quantity = quantity))
            }
        }
        updateProductCount()
    }

    fun removeFromCart(product: Product) {
        val existingItem = _cart.find { it.id == product.id }
        if (existingItem != null) {
            _cart.remove(existingItem)
        }
        updateProductCount()
    }

    // Toggle item in the cart (add or remove)
    fun toggleItemInCart(product: Product) {
        val existingItem = _cart.find { it.id == product.id }
        if (existingItem != null) {
            _cart.remove(existingItem)
        } else {
            _cart.add(product.copy(quantity = 1)) // Add new product with quantity 1
        }
        updateProductCount()
    }

    // Check if item is already in the cart
    fun isItemInCart(product: Product): Boolean {
        return _cart.any { it.id == product.id }
    }

    fun getCartItemQuantity(product: Product): Int {
        return _cart.find { it.id == product.id }?.quantity ?: 0
    }

    private fun updateProductCount() {
        quantityCount = _cart.sumOf { it.quantity }
    }

    fun updateQuantity(product: Product, newQuantity: Int) {
        val cartItem = _cart.find { it.id == product.id }
        cartItem?.let {
            if (newQuantity <= product.quantityCount) {
                it.quantity = newQuantity
            }
            if (it.quantity <= 0) {
                _cart.remove(it) // so item be remove if it is 0
            }
        }
        updateProductCount()
    }

    fun calculateTotalPrice(): Int {
        val carTotal = _cart.sumOf { (it.price.toDouble() * it.quantity).roundToInt() }
        return carTotal + deliveryFee
    }

    fun saveProductToUserAccount(context: Context, product: Product, quantity: Int) {
        val user = auth.currentUser
        user?.let {
            val userName = it.displayName ?: "unknown_user"

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
                .document(userName)
                .collection("cart")
                .add(cartItem)
                .addOnSuccessListener {
                    //Toast.makeText(context, "Product saved to user account", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to add to cloud: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }



    private fun fetchCartItems() {

        val user = auth.currentUser
        user?.let {
            val userName = it.displayName ?: "Anonymous-user"

            firestore.collection("users")
                .document(userName)
                .collection("cart")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    _cart.clear()
                    snapshot?.documents?.forEach { document ->
                        val product = Product(
                            id = document.getString("id") ?: "",
                            name = document.getString("name") ?: "",
                            price = (document.getDouble("price") ?: 0.0).toFloat(),
                            images = (document.get("images") as? List<String>) ?: listOf(),
                            quantity = document.getLong("quantity")?.toInt() ?: 1,
                            quantityCount = document.getLong("quantityAvailable")?.toInt() ?: 0
                        )
                        //val quantity = document.getLong("quantity")?.toInt() ?: 1
                        _cart.add(product)
                    }
                    updateProductCount()
                }
        }

    }

    // Function to observe changes in products and update the cart accordingly
    private fun observeProductUpdates() {
        firestore.collection("products")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                snapshot?.documents?.forEach { document ->
                    val updatedProduct = Product(
                        id = document.getString("id") ?: "",
                        name = document.getString("name") ?: "",
                        price = (document.getDouble("price") ?: 0.0).toFloat(),
                        images = (document.get("images") as? List<String>) ?: listOf(),
                        quantityCount = document.getLong("quantityCount")?.toInt() ?: 0
                    )

                    // Update product information in cart if it exists
                    val cartItem = _cart.find { it.id == updatedProduct.id }
                    cartItem?.let {
                        it.name = updatedProduct.name
                        it.price = updatedProduct.price
                        it.quantityCount = updatedProduct.quantityCount
                    }
                }

                updateProductCount()
            }
    }





}

