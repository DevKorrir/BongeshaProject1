//package dev.korryr.bongesha.repositories
//
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import dev.korryr.bongesha.commons.data.CartItemEntity
//import dev.korryr.bongesha.viewmodels.state.CartDao
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.emitAll
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.runBlocking
//
//class CartRepository(private val cartDao: CartDao) {
//
//    private val firestore = Firebase.firestore
//    private val userId = "userID"  // Use actual user ID from authentication
//
//    // Combine local and Firestore cart items into a single flow
//    private val _cartItems = MutableStateFlow<List<CartItemEntity>>(emptyList())
//    val cartItems: Flow<List<CartItemEntity>> = _cartItems
//
//    init {
//        listenToFirestoreChanges()
//    }
//
//    // Sync Firestore changes with local database and update the flow
//    private fun listenToFirestoreChanges() {
//        firestore.collection("users").document(userId).collection("cart")
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    // Log the error
//                    return@addSnapshotListener
//                }
//
//                snapshot?.let {
//                    val firestoreItems = it.documents.mapNotNull { doc ->
//                        doc.toObject(CartItemEntity::class.java)
//                    }
//                    _cartItems.value = firestoreItems
//                    runBlocking {
//                        // Update local database to match Firestore changes
//                        cartDao.clearAndInsert(firestoreItems)
//                    }
//                }
//            }
//    }
//
//    // Add item to local database and Firestore
//    suspend fun addCartItem(item: CartItemEntity) {
//        cartDao.insertCartItem(item)
//        firestore.collection("users").document(userId).collection("cart")
//            .add(item).await()
//    }
//
//    // Remove item from local database and Firestore
//    suspend fun removeCartItem(item: CartItemEntity) {
//        cartDao.deleteCartItem(item)
//        val cartDoc = firestore.collection("users").document(userId)
//            .collection("cart").whereEqualTo("id", item.id).get().await()
//        cartDoc.documents.firstOrNull()?.reference?.delete()
//    }
//
//    // Optional: Additional methods for other cart operations, syncing to Firestore as needed
//}
