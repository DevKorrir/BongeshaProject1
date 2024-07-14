package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import dev.korryr.bongesha.commons.Item

class CartItemViewModel : ViewModel() {

}
class ItemViewModel : ViewModel() {
    private val repository = ItemRepository() // Assume this is your data source

    fun getItemById(itemId: String) = repository.getItemById(itemId)

    fun isItemFavorite(itemId: String) {
        // Check if the item is in the favorite list
    }

    fun toggleFavorite(item: Item) {
        // Add or remove the item from the favorite list
    }
}



class ItemRepository {
    // Implement the data fetching and storing logic here
    fun getItemById(itemId: String) {
        // Fetch the item by ID from your data source
    }

    // Add other data operations as needed
}
