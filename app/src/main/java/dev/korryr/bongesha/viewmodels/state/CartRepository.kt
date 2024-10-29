//package dev.korryr.bongesha.viewmodels.state
//
//import dev.korryr.bongesha.commons.data.CartItemEntity
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.onEach
//
//class CartRepository2(private val cartDao: CartDao) {
//
//    // Expose a Flow for reactive updates from the DAO
//    val localCartItems: Flow<List<CartItemEntity>> = flow {
//        emit(cartDao.getAllCartItems())
//    }.onEach { items ->
//        items.forEach { syncToFirestore(it) } // Sync each item if needed
//    }
//
//    suspend fun addCartItem(item: CartItemEntity) {
//        cartDao.insertCartItem(item)
//        syncToFirestore(item) // Sync with Firestore when added locally
//    }
//
//    private fun syncToFirestore(item: CartItemEntity) {
//        // Add Firestore sync logic here
//    }
//
//    suspend fun removeCartItem(item: CartItemEntity) {
//        cartDao.deleteCartItem(item)
//        // Optionally sync with Firestore if required
//    }
//}
