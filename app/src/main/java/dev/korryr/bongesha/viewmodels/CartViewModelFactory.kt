//package dev.korryr.bongesha.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import dev.korryr.bongesha.repositories.ProductRepository
//
//class CartViewModelFactory(
//    private val productRepository: ProductRepository,
//    private val authViewModel: AuthViewModel
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CartViewModel(productRepository, authViewModel) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
