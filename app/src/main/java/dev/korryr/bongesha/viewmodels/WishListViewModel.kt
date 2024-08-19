package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.korryr.bongesha.commons.WishlistItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistViewModel : ViewModel() {

    private val _wishlistItems = MutableStateFlow<List<WishlistItems>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItems>> get() = _wishlistItems

    fun addItemToWishlist(wishlistItem: WishlistItems) {
        viewModelScope.launch {
            _wishlistItems.update { currentItems ->
                if (currentItems.any { it.id == wishlistItem.id }) {
                    currentItems
                } else {
                    currentItems + wishlistItem
                }
            }
        }
    }

    fun removeItemFromWishlist(itemId: String) {
        viewModelScope.launch {
            _wishlistItems.update { currentItems ->
                currentItems.filterNot { it.id == itemId }
            }
        }
    }

    fun isItemInWishlist(wishlistItem: WishlistItems): Boolean {
        return _wishlistItems.value.any { it.id == wishlistItem.id }
    }

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
