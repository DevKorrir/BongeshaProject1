package dev.korryr.bongesha.viewmodels

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CartItem(
    val id: String = "",
    val name: String = "",
    val price: Float = 0f,
    val quantity: Int = 0,//current number of items in cart
    val quantityCount: Int = 0,//stock available
    val images: List<String> = emptyList(),
    val offer: Float = 0f, // Discount or offer on the item
    val tax: Float = 0f // Applicable tax for the item
){
    val totalPrice: Float
        get() = price * quantity - offer * quantity + tax * quantity
}

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    private val firestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var quantityCount by mutableIntStateOf(0)
    private set
    //private var deliveryFee: Int by mutableIntStateOf(0)
    var deliveryFee: Float = 0f



    init {
        fetchCartItems()
    }

//    fun updateDeliveryFee(fee: Int) {
//        deliveryFee = fee
//    }

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
            1000
        }
    }

    // Add or update a product in the cart
    fun addToCart(product: Product, quantity: Int) {
        val updatedCartItems = _cartItems.value.toMutableList()
        val existingItemIndex = updatedCartItems.indexOfFirst { it.id == product.id }

        if (existingItemIndex >= 0) {
            val existingItem = updatedCartItems[existingItemIndex]
            val newQuantity = (existingItem.quantity + quantity).coerceAtMost(product.quantityCount)
            updatedCartItems[existingItemIndex] = existingItem.copy(quantity = newQuantity)
        } else if (product.quantityCount > 0) {
            updatedCartItems.add(
                CartItem(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = quantity.coerceAtMost(product.quantityCount),
                    quantityCount = product.quantityCount,
                    images = product.images
                )
//                        updatedCartItems.add(newCartItem)
//                        saveCartItemToFirestore(newCartItem)
            )
        }
        _cartItems.value = updatedCartItems
        updateProductCount()
    }

    fun updateCartQuantity(cartItem: CartItem, newQuantity: Int) {
        val updatedCartItems = _cartItems.value.toMutableList()
        val index = updatedCartItems.indexOfFirst { it.id == cartItem.id }

        if (index >= 0) {
            val existingItem = updatedCartItems[index]

            if (newQuantity in 1..existingItem.quantityCount && newQuantity != existingItem.quantity) {
                // Update quantity in local state
                updatedCartItems[index] = existingItem.copy(quantity = newQuantity)
                _cartItems.value = updatedCartItems

                // Sync quantity change to Firestore
                updateQuantityInFirestore(existingItem.id, newQuantity)
            }

            // If the new quantity is zero, remove the item from both local state and Firestore
            if (newQuantity <= 0) {
                updatedCartItems.removeAt(index)
                _cartItems.value = updatedCartItems
            }

            // Update the total count of items in the cart
            updateProductCount()
        }
    }


    private fun updateQuantityInFirestore(itemId: String, quantity: Int) {
        val user = auth.currentUser
        user?.let {
            val userName = it.displayName ?: "unknown_user"

            firestore.collection("users")
                .document(userName)
                .collection("cart")
                .document(itemId)
                .update("quantity", quantity)  // Update only the quantity field
                .addOnFailureListener { e ->
                    Log.e("CartViewModel", "Failed to update quantity in Firestore: ${e.message}")
                }
        }
    }


    // Remove an item from the cart
    fun removeFromCart(cartItem: CartItem) {
        // Remove item from local state
        _cartItems.value = _cartItems.value.filter { it.id != cartItem.id }
        updateProductCount()

        // Remove item from Firestore
        val user = auth.currentUser
        if (user != null) {
            val userName = user.displayName ?: "unknown_user" // Use UID instead of display name

            firestore.collection("users")
                .document(userName)
                .collection("cart")
                .document(cartItem.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("CartViewModel", "Successfully removed item ${cartItem.id} from Firestore")
                }
                .addOnFailureListener { e ->
                    Log.e("CartViewModel", "Failed to delete cart item from Firestore: ${e.message}")
                }
        } else {
            Log.e("CartViewModel", "User is not authenticated; cannot delete from Firestore")
        }
    }


    // Toggle an item in the cart (add or remove)
    fun toggleItemInCart(product: Product) {
        if (isItemInCart(product)) {
            removeFromCart(cartItem = CartItem(id = product.id))
        } else {
            addToCart(product, quantity = 1)
        }
    }

    // Check if an item is already in the cart
    fun isItemInCart(product: Product): Boolean = _cartItems.value.any { it.id == product.id }

    // Update the product quantity count
    private fun updateProductCount() {
        quantityCount = _cartItems.value.sumOf { it.quantity }
    }

    // Calculate total price including delivery fee
//    fun calculateTotalPricet(): Float {
//        return _cartItems.value.sumOf { it.totalPrice.toDouble() }.toFloat() + deliveryFee
//    }


    fun saveProductToUserAccount(context: Context, product: Product, quantity: Int) {
        val user = auth.currentUser
        user?.let {
            val userName = it.displayName ?: "unknown_user"

            val cartItem = mapOf(
                "id" to product.id,
                "name" to product.name,
                "price" to product.price,
                "quantity" to quantity,
                "quantityCount" to product.quantityCount,
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

    // Fetch cart items from Firestore
    fun fetchCartItems() {
        val user = auth.currentUser
        user?.let {
            val userName = it.displayName ?: "Anonymous-user"

            firestore.collection("users")
                .document(userName)
                .collection("cart")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("CartViewModel", "Fetch failed: ${e.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val cartList = snapshot.documents.mapNotNull { document ->
                            document.toObject(CartItem::class.java)
                        }
                        _cartItems.value = cartList  // Update local cart state
                        updateProductCount()
                        Log.d("CartViewModel", "Cart items updated from Firestore")
                    }
                }
        }
    }

    // Observe changes in products (e.g., stock updates)
    fun observeProductUpdates() {
        firestore.collection("products")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                val updatedCartItems = _cartItems.value.map { cartItem ->
                    snapshot?.documents?.find { it.getString("id") == cartItem.id }?.let { document ->
                        cartItem.copy(
                            name = document.getString("name") ?: cartItem.name,
                            price = document.getDouble("price")?.toFloat() ?: cartItem.price,
                            quantityCount = document.getLong("quantityCount")?.toInt() ?: cartItem.quantityCount
                        )
                    } ?: cartItem
                }
                _cartItems.value = updatedCartItems
            }
    }

    // Calculate subtotal
    fun calculateSubtotal(): Float {
        var subtotal = 0f
        for (item in _cartItems.value) {
            subtotal += item.price * item.quantity
        }
        return subtotal
    }

    // Calculate total offers (assuming each CartItem has an `offer` field)
    fun calculateTotalOffer(): Float {
        var totalOffer = 0f
        for (item in _cartItems.value) {
            totalOffer += item.offer * item.quantity
        }
        return totalOffer
    }

    // Calculate total tax (assuming each CartItem has a `tax` field)
    fun calculateTotalTax(): Float {
        var totalTax = 0f
        for (item in _cartItems.value) {
            totalTax += item.tax * item.quantity
        }
        return totalTax
    }

    // Update amount payable: subtotal + delivery fee - total offer - tax
    fun calculateAmountPayable(): Float {
        val subtotal = calculateSubtotal()
        val totalOffer = calculateTotalOffer()
        val totalTax = calculateTotalTax()
        return subtotal + deliveryFee - totalOffer - totalTax
    }

    // Existing delivery fee calculation logic...
    fun updateDeliveryFee(fee: Float) {
        deliveryFee = fee
    }




}




