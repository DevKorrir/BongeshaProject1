//package dev.korryr.bongesha.screens.willbedeleted
//
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
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
//}
