package dev.korryr.bongesha.viewmodels
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//class CartViewModelFactory(
//    private val context: Context
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
//            return CartViewModel(context) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//
//    }
//}