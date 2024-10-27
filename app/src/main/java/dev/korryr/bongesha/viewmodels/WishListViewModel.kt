package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistViewModel : ViewModel() {

    // StateFlow to hold the wishlist items
    private val _wishlistItems = MutableStateFlow<List<WishlistItems>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItems>> get() = _wishlistItems

    // Add item to wishlist if it's not already there
    fun addItemToWishlist(wishlistItems: WishlistItems) {
        viewModelScope.launch {
            _wishlistItems.update { currentItems ->
                if (currentItems.any { it.id == wishlistItems.id }) {
                    currentItems // Do nothing if the item is already in the wishlist
                } else {
                    val updatedItems = currentItems + wishlistItems // Add the item to the wishlist
                    //saveWishlistToUserAccount(updatedItems) // Save the updated wishlist to the user's account
                    updatedItems
                }
            }
        }
    }

    // Remove item from wishlist by product ID
    fun removeItemFromWishlist(itemId: String) {
        viewModelScope.launch {
            _wishlistItems.update { currentItems ->
                val updatedItems = currentItems.filterNot { it.id == itemId }
                //saveWishlistToUserAccount(updatedItems) // Save to account
                updatedItems
            }
        }
    }

    // Check if an item is in the wishlist
    fun isItemInWishlist(wishlistItem: WishlistItems): Boolean {
        return _wishlistItems.value.any { it.id == wishlistItem.id }
    }

    // Toggle the item's presence in the wishlist
    fun toggleItemInWishlist(wishlistItem: WishlistItems) {
        viewModelScope.launch {
            if (isItemInWishlist(wishlistItem)) {
                removeItemFromWishlist(wishlistItem.id)
            } else {
                addItemToWishlist(wishlistItem)
            }
        }
    }
}
data class WishlistItems(
    val id: String,
    val name: String,
    val price: Float,
    val imageUrl: String
)
