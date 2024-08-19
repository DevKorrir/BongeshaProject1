package dev.korryr.bongesha.viewmodels

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import dev.korryr.bongesha.commons.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    init {
        fetchItems()
    }

    private fun fetchItems() {
        // Firestore collection reference
        val db = FirebaseFirestore.getInstance()
        db.collection("items")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<Item>()
                for (document in result) {
                    val item = document.toObject(Item::class.java)
                    itemList.add(item)
                }
                _items.value = itemList
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }
}



class SearchViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _items = MutableStateFlow<List<Item>>(listOf(
        //add items here

    ))
    val items: StateFlow<List<Item>> = _items
    private val _searchResults = MutableStateFlow<List<Item>>(listOf())
    val searchResults: StateFlow<List<Item>> = _searchResults

    init {
        fetchItems()
    }

    fun searchItems(query: String) {
        _searchResults.value = if (query.isEmpty()) {
            listOf()
        }else {
            _items.value.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }

    private fun fetchItems() {
        viewModelScope.launch {
            db.collection("items")
                .get()
                .addOnSuccessListener { result ->
                    val itemList = result.documents.mapNotNull { it.toObject(Item::class.java) }
                    _items.value = itemList
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                }
        }
    }
}


@Composable
fun ItemListScreen(
    serchViewModel: SearchViewModel = viewModel(),
    onItemClick: (Item) -> Unit
) {
    val items by serchViewModel.items.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(items) { item ->
            ItemRow(item, onItemClick)
        }
    }
}

@Composable
private fun ItemRow(item: Item, onItemClick: (Item) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) }
    ) {
        Image(
            painter = rememberImagePainter(item.image),
            contentDescription = item.name,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = item.name)
            Text(text = item.description)
            Text(text = "$${item.price}")
        }
    }
}

//data class Item(
//    val id: String = "",
//    val name: String = "",
//    val description: String = "",
//    val imageUrl: String = "",
//    val price: Double = 0.0
//    // Add other fields as needed
//)