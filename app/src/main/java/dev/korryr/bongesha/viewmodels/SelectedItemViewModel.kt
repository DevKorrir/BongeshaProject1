package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import dev.korryr.bongesha.commons.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SelectedItemViewModel : ViewModel(){
    private val _selectedItem = MutableStateFlow<Item?>(null)
    val selectedItem: StateFlow<Item?> get() = _selectedItem


    fun selectedItem(item: Item) {
        _selectedItem.value = item
    }
}