package dev.korryr.bongesha.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class Product(
    val id: String = "",
    var name: String = "",
    var price: Float = 0f,
    val offerPercentage: Float? = null,
    val description: String? = "",
    val colors: List<Int> = emptyList(),
    val sizes: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val product: Product? = null,
    var quantity: Int = 1,
    var quantityCount: Int = 0,
    var markPrice: Float = 0f
)

data class Category(
    val name: String,
    val imageResId: Int
)


sealed class CategoryUiState {
    object Loading : CategoryUiState()
    data class Success(val categories: List<Category>) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}


class CategoryViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Holds the currently selected category
    var selectedCategory by mutableStateOf<String?>(null)
        private set

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    private var isLastPage = false

    init {
        fetchCategories()
    }

//    fun fetchProducts(paginate: Boolean = false) {
//        if (isLoading || isLastPage) return  // Prevent duplicate calls or loading beyond last
//    }

    private fun updateUiState(newState: CategoryUiState) {
        _uiState.value = newState
    }

    private fun fetchCategories() {
        val categoryImages = mapOf(
            "Audio & Sound Systems" to R.drawable.setting,
            "Phones & Accessories" to R.drawable.headphones,
            "Computers & Accessories" to R.drawable.bongesha_sec_icon,
            // Map other categories to drawable resources as needed
        )

        firestore.collection("categories")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CategoryViewModel", "Error fetching categories", error)
                    updateUiState(CategoryUiState.Error("Error loading categories"))
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val categoriesWithItems = mutableListOf<Category>()
                    snapshot.documents.forEach { document ->
                        val categoryName = document.getString("name") ?: return@forEach
                        val imageResId = categoryImages[categoryName] ?: R.drawable.apple_icon
                        categoriesWithItems.add(Category(name = categoryName, imageResId = imageResId))
                    }
                    updateUiState(CategoryUiState.Success(categoriesWithItems))
                } else {
                    updateUiState(CategoryUiState.Error("No categories found"))
                }
            }
    }

    fun fetchProductsForCategory(category: String) {
        _isLoading.value = true
        _uiState.update { CategoryUiState.Loading }

        listenerRegistration = firestore.collection("categories")
            .document(category)
            .collection("items")
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) {
                    Log.e("CategoryViewModel", "Error fetching products", error)
                    _uiState.update { CategoryUiState.Error("Error loading products") }
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
                    _products.update { products }
                } else {
                    _products.update { emptyList() }
                }
            }
    }


    override fun onCleared() {
        super.onCleared()
        // Remove Firestore listener when ViewModel is cleared
        listenerRegistration?.remove()
    }
}
