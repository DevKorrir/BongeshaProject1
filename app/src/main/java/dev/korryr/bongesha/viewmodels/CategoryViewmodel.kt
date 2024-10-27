package dev.korryr.bongesha.viewmodels



///////////////////////////////////////////////////////////////////////////
// copied
///////////////////////////////////////////////////////////////////////////

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class Product(
    val id: String = "",
    val name: String = "",
    val price: Float = 0f,
    val offerPercentage: Float? = null,
    val description: String? = "",
    val colors: List<Int> = emptyList(),
    val sizes: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val product: Product? = null,
    var quantity: Int = 1,
    var itemCount: Int = 0,
    var markPrice: Float = 0f
)

data class Category(
    val name: String,
    val imageResId: Int // Resource ID for the local image
)


sealed class CategoryUiState{
    object Loading : CategoryUiState()
    data class Success(val categories: List<String>) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}

class CategoryViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    // Holds the currently selected category
    var selectedCategory by mutableStateOf<String?>(null)
        private set

    // Holds the list of products for the selected category
    var products = mutableStateListOf<Product>()
        private set

    // Loading state
    var isLoading by mutableStateOf(false)
        private set

    private var categories = mutableStateListOf<String>()

    // Firestore listener to track real-time updates
    private var listenerRegistration: ListenerRegistration? = null

    private var lastDocumentSnapshot: QuerySnapshot? = null
    private var isLastPage = false

    init {
        // Fetch categories when ViewModel is created
        fetchCategories()
    }

    fun fetchProducts(paginate: Boolean = false) {
        if (isLoading || isLastPage) return  // Prevent duplicate calls or loading beyond last
    }

    private fun updateUiState(newState: CategoryUiState) {
        _uiState.value = newState
    }

    // Function to fetch categories from Firestore
    private fun fetchCategories() {
        val categoryImages = mapOf(
            "Audio & Sound Systems" to R.drawable.setting,
            "Phones & Accessories" to R.drawable.headphones,
            "Computers & Accessories" to R.drawable.bongesha_sec_icon,
            // Continue mapping category names to drawable resources...
        )
        // Function to fetch categories from Firestore
            firestore.collection("categories")
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && !snapshot.isEmpty) {
                        val categoriesWithItems = mutableListOf<Category>()
                        snapshot.documents.forEach { document ->
                            val categoryName = document.getString("name") ?: return@forEach

                            // Check if the category has items
                            firestore.collection("categories")
                                .document(document.id)
                                .collection("items")
                                .get()
                                .addOnSuccessListener { itemsSnapshot ->
                                    if (!itemsSnapshot.isEmpty) {
                                        // Add category if it has items and map the image
                                        val imageResId = categoryImages[categoryName] ?: R.drawable.apple_icon
                                        categoriesWithItems.add(Category(name = categoryName, imageResId = imageResId))
                                        updateUiState(CategoryUiState.Success(categories))
                                    }
                                }
                                .addOnFailureListener {
                                    Log.e("CategoryViewModel", "Error checking items for category: $categoryName", it)
                                }
                        }

                        if (categoriesWithItems.isEmpty()) {
                            updateUiState(CategoryUiState.Error("No categories with items found"))
                        }
                    } else {
                        updateUiState(CategoryUiState.Error("No categories found"))
                    }
                }
                .addOnFailureListener { error ->
                    Log.e("CategoryViewModel", "Error fetching categories", error)
                    updateUiState(CategoryUiState.Error("Error loading categories"))
                }
        }

        // Function to fetch products for a selected category
        fun fetchProductsForCategory(category: String) {
            if (isLoading) return
            isLoading = true

            firestore.collection("categories")
                .document(category)
                .collection("items")
                .get()
                .addOnSuccessListener { snapshot ->
                    isLoading = false
                    products.clear()
                    snapshot.documents.mapNotNullTo(products) { it.toObject(Product::class.java) }
                }
                .addOnFailureListener { error ->
                    isLoading = false
                    Log.e("CategoryViewModel", "Error fetching products", error)
                }
        }


    override fun onCleared() {
        super.onCleared()
        // Remove Firestore listener when ViewModel is cleared
        listenerRegistration?.remove()
    }
}









//package dev.korryr.bongesha.screens.willbedeleted
//
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.QuerySnapshot
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import java.lang.Exception
//
//data class Product(
//    val id: String = "",
//    val name: String = "",
//    val category: String = "",
//    val price: Float = 0f,
//    val offerPercentage: Float? = null,
//    val description: String? = "",
//    val images: List<String> = emptyList(),
//    val sizes: List<String> = emptyList()
//)
//
//class ProductViewModel : ViewModel() {
//
//    // Firestore instance
//    private val firestore = FirebaseFirestore.getInstance()
//
//    // State for product list
//    var products by mutableStateOf<List<Product>>(emptyList())
//        private set
//
//    // State to manage loading, errors, etc.
//    var isLoading by mutableStateOf(false)
//        private set
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//
//    // Optional: Pagination or Lazy Loading States
//    private var lastDocumentSnapshot: QuerySnapshot? = null
//    private var isLastPage = false
//
//    // Fetch products from Firestore
//    fun fetchProducts(paginate: Boolean = false) {
//        if (isLoading || isLastPage) return  // Prevent duplicate calls or loading beyond last page
//
//        viewModelScope.launch {
//            try {
//                isLoading = true
//                errorMessage = null
//
//                // Firestore query
//                val query = firestore.collection("Products")
//                    .orderBy("name")  // You can adjust this to order by price, category, etc.
//
//                // Pagination handling
//                val currentQuery = if (paginate && lastDocumentSnapshot != null) {
//                    query.startAfter(lastDocumentSnapshot?.documents?.last())
//                } else {
//                    query.limit(10)  // Load the first 10 items initially
//                }
//
//                val snapshot = currentQuery.get().await()
//
//                // Update last document snapshot for pagination
//                if (snapshot.isEmpty) {
//                    isLastPage = true
//                } else {
//                    lastDocumentSnapshot = snapshot
//                }
//
//                // Parse fetched data into Product list
//                val fetchedProducts = snapshot.documents.map { document ->
//                    document.toObject(Product::class.java)?.copy(id = document.id) ?: Product()
//                }
//
//                // If paginating, append the new products, else set fresh list
//                products = if (paginate) {
//                    products + fetchedProducts
//                } else {
//                    fetchedProducts
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                errorMessage = "Failed to fetch products: ${e.localizedMessage}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    // Optional: Function to refresh product list (without pagination)
//    fun refreshProducts() {
//        lastDocumentSnapshot = null
//        isLastPage = false
//        fetchProducts(paginate = false)
//    }
//
//    private lateinit var database: DatabaseReference
//
//
//
//    fun fetchProducts() {
//        val usersRef = database.child("users")
//    }
//}







//package dev.korryr.bongesha.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.firestore.FirebaseFirestore
//import dev.korryr.bongesha.R
//import dev.korryr.bongesha.commons.Category
//import dev.korryr.bongesha.commons.Item
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class CategoryViewModel : ViewModel() {
//    private val _categories = MutableStateFlow<List<Category>>(emptyList())
//    val categories: StateFlow<List<Category>> get() = _categories
//
//    private val firestore = FirebaseFirestore.getInstance()
//
//
//
//
//
//    init {
//        // Initialize with some sample data
//        _categories.value = listOf(
//            Category(
//                id = "1",
//                name = "Beverages",
//                items = listOf(
//                    Item(
//                        id = "a1",
//                        name = "Coca cola",
//                        description = "500ml pet",
//                        image = R.drawable.coke,
//                        price = 70.00,
//                    ),
//                    Item(
//                        id = "a2",
//                        name = "Fanta Orange",
//                        description = "500ml pet",
//                        image = R.drawable.fanta_orange_pet,
//                        price = 70.00,
//                    ),
//                    Item(
//                        id = "a3",
//                        name = "Fanta Blackcurrant",
//                        description = "500ml pet",
//                        image = R.drawable.fanta_blackcurrant_pet,
//                        price = 70.00
//                    ),
//                    Item(
//                        id = "a4",
//                        name = "Fanta Blackcurrant",
//                        description = "350ml pet",
//                        image = R.drawable.fanta_blackcurrant_pet,
//                        price = 50.00
//                    ),
//                    Item(
//                        id = "a5",
//                        name = "Sprite Pet",
//                        description = "500ml pet",
//                        image = R.drawable.sprite_pet,
//                        price = 70.00
//                    ),
//                    Item(
//                        id = "a6",
//                        name = "Stoney Class",
//                        description = "300ml pet",
//                        image = R.drawable.stoney_class,
//                        price = 40.00
//                    ),
//                    Item(
//                        id = "a7",
//                        name = "Minute Maid Tropical juice",
//                        description = "400ml pet",
//                        image = R.drawable.minute_maid_tropical,
//                        price = 75.00
//                    ),
//                    Item(
//                        id = "a8",
//                        name = "Minute Maid Apple Juice",
//                        description = "400ml pet",
//                        image = R.drawable.minute_maid_apple,
//                        price = 75.00
//                    )
//                ),
//                icon = R.drawable.beverages
//            ),
//            Category(
//                id = "2",
//                name = "Home Care",
//                items = listOf(
//                    Item(
//                        id = "b1",
//                        name = "Dettol Original",
//                        description = "Antibacterial handwash 60g",
//                        image = R.drawable.dettol_original,
//                        price = 90.00
//                    ),
//                    Item(
//                        id = "b2",
//                        name = "Geisha",
//                        description = "Soothing Aloe Vera & Honey Soap 60g",
//                        image = R.drawable.geisha_green,
//                        price = 50.00
//                    ),
//                    Item(
//                        id = "b3",
//                        name = "Geisha",
//                        description = "Fragrant Rose & Honey Soap 60g",
//                        image = R.drawable.geisha_pink,
//                        price = 50.00
//                    )
//                ),
//                icon = R.drawable.home_care
//            ),
//            Category(
//                id = "3",
//                name = "Personal Care",
//                items = listOf(
//                    Item(
//                        id = "c1",
//                        name = "Vaseline Lotion",
//                        description = "Cocoa Radiant 100ml",
//                        image = R.drawable.vaseline_coco,
//                        price = 130.00
//                    ),
//                    Item(
//                        id = "c2",
//                        name = "Vaseline Jelly",
//                        description = "Perfumed 95ml",
//                        image = R.drawable.vaseline_jelly_perfumed,
//                        price = 85.00
//                    ),
//                    Item(
//                        id = "c3",
//                        name = "Vaseline Jelly",
//                        description = "Cocoa Butter Perfumed 95ml",
//                        image = R.drawable.vaseline_jelly_coco,
//                        price = 85.00
//                    ),
//                    Item(
//                        id = "c4",
//                        name = "Vaseline Lotion",
//                        description = "Perfumed 100ml",
//                        image = R.drawable.vaseline_pink,
//                        price = 130.00
//                    )
//                ),
//                icon = R.drawable.personal_care
//            ),
//            Category(
//                id = "4",
//                name = "Home & Kitchen",
//                items = listOf(
//                    Item(
//                        id = "d1",
//                        name = "Steel wool",
//                        description = "Sokoni wool",
//                        image = R.drawable.steel_wool,
//                        price = 10.00
//                    ),
//                    Item(
//                        id = "d2",
//                        name = "Steel wire",
//                        description = "Strong With Rust resistance",
//                        image = R.drawable.stell_wired,
//                        price = 20.00
//                    ),
//                    Item(
//                        id = "d3",
//                        name = "Sugura Sponge",
//                        description = "Scouring power",
//                        image = R.drawable.sugura_green,
//                        price = 15.00
//                    )
//                ),
//                icon = R.drawable.home_kitchen
//            ),
//            Category(
//                id = "5",
//                name = "Spread & Bread",
//                items = listOf(
//                    Item(
//                        id = "e1",
//                        name = "Blue Band",
//                        description = "Original 250g",
//                        image = R.drawable.blue_band,
//                        price = 150.00
//                    ),
//                    Item(
//                        id = "e2",
//                        name = "Prestige",
//                        description = "Original 500g",
//                        image = R.drawable.prestige_spread,
//                        price = 200.00
//                    )
//                ),
//                icon = R.drawable.bread_spread
//            ),
//            Category(
//                id = "6",
//                name = "Rice & Pasta",
//                items = listOf(
//                    Item(
//                        id = "f1",
//                        name = "Daawati Rice",
//                        description = "Long Grain Traditional 1kg",
//                        image = R.drawable.daawati_rice,
//                        price = 180.00
//                    ),
//                    Item(
//                        id = "f2",
//                        name = "Pazani Spaghetti",
//                        description = "Nutritional 400g",
//                        image = R.drawable.spaghetti_pazani,
//                        price = 80.00
//                    ),
//                    Item(
//                        id = "f3",
//                        name = "Noodles",
//                        description = "Kuku Flavour 12g",
//                        image = R.drawable.indomie_noodles,
//                        price = 45.00
//                    )
//                ),
//                icon = R.drawable.rice
//            ),
//            Category(
//                id = "7",
//                name = "Snacks",
//                items = listOf(
//                    Item(
//                        id = "g1",
//                        name = "Kit Kat",
//                        description = "Milk Chocolate 20g",
//                        image = R.drawable.kit_kat_biscuit,
//                        price = 20.00
//                    ),
//                    Item(
//                        id = "g2",
//                        name = "Choco",
//                        description = "Creame chocolate 20g",
//                        image = R.drawable.choco_biscuit,
//                        price = 10.00
//                    )
//                ),
//                icon = R.drawable.snacks
//            )
//        )
//    }
//
//    fun getItemById(itemId: String): Item? {
//        for (category in _categories.value) {
//            val item = category.items.find { it.id == itemId }
//            if (item != null) return item
//        }
//        return null
//    }
//
//    fun loadCategories() {
//        // Fetch categories from Firestore
//        val categoriesRef = FirebaseFirestore.getInstance().collection("categories")
//
//        categoriesRef.get().addOnSuccessListener { documents ->
//            val categoryList = mutableListOf<Category>()
//
//            for (document in documents) {
//                val categoryName = document.getString("name") ?: ""
//                val categoryId = document.id
//                val itemsRef = document.reference.collection("items")
//
//                itemsRef.get().addOnSuccessListener { itemsSnapshot ->
//                    val itemList = mutableListOf<Item>()
//
//                    for (itemDocument in itemsSnapshot) {
//                        val itemName = itemDocument.getString("name") ?: ""
//                        val imageUrl = itemDocument.getString("image") ?: ""
//
//                        // Create Item object
//                        val item = Item(
//                            name = itemName,
//                            imageUrl = imageUrl,
//                            id = String.toString(),
//                            description = String.toString(),
//                            price = 10.0,
//                            image = R.drawable.coke
//                        )
//                        itemList.add(item)
//                    }
//
//                    // Create Category object and add to list
//                    val category = Category(
//                        id = categoryId,
//                        name = categoryName,
//                        items = itemList,
//                        icon = R.drawable.category_icon
//                    )
//                    categoryList.add(category)
//                }
//            }
//            _categories.value = categoryList
//        }
//    }
//
//}