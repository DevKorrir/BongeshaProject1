// dev/korryr/bongesha/viewmodels/CartViewModelFactory.kt
package dev.korryr.bongesha.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//class CartViewModelFactory(
//    private val context: Context,
//    private val locationProvider: LocationProvider
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CartViewModel(context, locationProvider) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
import android.annotation.SuppressLint
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
suspend fun fetchUserLocation(context: Context): Location? {
    // Check for location permissions
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PermissionChecker.PERMISSION_GRANTED
    ) {
        // Return null if permissions are not granted
        return null
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    return try {
        fusedLocationClient.lastLocation.await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


