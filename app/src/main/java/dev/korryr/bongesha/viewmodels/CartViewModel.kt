package dev.korryr.bongesha.viewmodels
//
//import android.app.Application
//import android.content.Context
//import android.content.SharedPreferences
//import androidx.lifecycle.AndroidViewModel
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import dev.korryr.bongesha.commons.CartItem
//import dev.korryr.bongesha.commons.Item
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//
//class CartViewModel(application: Application) : AndroidViewModel(application) {
//    private val sharedPreferences: SharedPreferences =
//        application.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
//
//    private val _cartItems = MutableStateFlow<List<CartItem>>(loadCartItems())
//    val cartItems: StateFlow<List<CartItem>> = _cartItems
//
//    fun addItemToCart(item: Item, quantity: Int, price: Double) {
//        val newItem = CartItem(item, quantity, price)
//        _cartItems.value = _cartItems.value + newItem
//        saveCartItems(_cartItems.value)
//    }
//
//    private fun loadCartItems(): List<CartItem> {
//        val cartItemsJson = sharedPreferences.getString("cart_items", "[]")
//        val type = object : TypeToken<List<CartItem>>() {}.type
//        return Gson().fromJson(cartItemsJson, type)
//    }
//
//    private fun saveCartItems(cartItems: List<CartItem>) {
//        val cartItemsJson = Gson().toJson(cartItems)
//        sharedPreferences.edit().putString("cart_items", cartItemsJson).apply()
//    }
//
//    fun clearCart() {
//        _cartItems.value = emptyList()
//        sharedPreferences.edit().remove("cart_items").apply()
//    }
//}
//
