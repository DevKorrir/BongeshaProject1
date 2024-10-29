//package dev.korryr.bongesha.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import dev.korryr.bongesha.repositories.CartRepository
//
//class CartViewModelFactory(private val cartRepository: CartRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
//            return CartViewModel(cartRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
