package dev.korryr.bongesha.commons

import androidx.compose.ui.unit.dp
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import android.Manifest
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dev.korryr.bongesha.ui.theme.orange28

@Composable
fun DeliveryLocationPicker(
    onConfirmLocation: (LatLng) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    // State to hold selected location
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val mapProperties = MapProperties(isMyLocationEnabled = true)
    val mapUiSettings = MapUiSettings(zoomControlsEnabled = true)
    val cameraPositionState = rememberCameraPositionState()

    // Permission request launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Location permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    // Check permission status
    val permissionGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!permissionGranted) {
        LaunchedEffect(Unit) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Layout with GoogleMap
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Select Delivery Location", modifier = Modifier.padding(8.dp))

        if (permissionGranted) {
            Box(modifier = Modifier.height(300.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    properties = mapProperties,
                    uiSettings = mapUiSettings,
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng // Update the selected location
                    }
                ) {
                    selectedLocation?.let { location ->
                        Marker(
                            state = MarkerState(position = location),
                            title = "Delivery Location",
                            snippet = "Selected location for delivery"
                        )
                    }
                }
            }

            // Display selected location coordinates
            selectedLocation?.let {
                Text(text = "Selected Location: ${it.latitude}, ${it.longitude}", modifier = Modifier.padding(8.dp))
            }

            // Confirm Button
            Button(
                onClick = {
                    Toast.makeText(context, "Location Confirmed", Toast.LENGTH_SHORT).show()
                    // Save or process the location as needed
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Confirm Location")
            }
        } else {
            Text(text = "Please grant location permissions to pick a delivery location.")
        }
    }
}


@Composable
fun LocationPickerBottomSheet(
    onConfirmLocation: (LatLng) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) } // Initialize Places client

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var searchResults by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    val initialPosition = LatLng(-0.6, 35.1936) // Kericho, Kenya

    // State to hold selected location
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val mapUiSettings = MapUiSettings(zoomControlsEnabled = true)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 10f)
    }
    val mapProperties = MapProperties(
        isMyLocationEnabled = true,
        minZoomPreference = 6f, // Minimum zoom level for detailed view
        maxZoomPreference = 15f  // Maximum zoom level to prevent zooming out too far
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Delivery Location: Drag with two fingers for easy navigation",
            modifier = Modifier.padding(8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold

        )

        // Search TextField
        OutlinedTextField(
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                fetchAutocompletePredictions(it.text, context, placesClient) { predictions ->
                    searchResults = predictions
                }
            },
            label = { Text("Search Location") },
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    fetchAutocompletePredictions(searchQuery.text, context, placesClient) { predictions ->
                        searchResults = predictions
                    }
                },)

        )

        // Display search results in a column
        searchResults.forEach { prediction ->
            Text(
                text = prediction.getFullText(null).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val placeId = prediction.placeId
                        fetchPlaceCoordinates(placeId, context, placesClient) { latLng ->
                            selectedLocation = latLng
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 100f)
                            searchResults = emptyList() // Clear results after selection
                        }
                    }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Request location permission
        RequestLocationPermission {
            // Only show GoogleMap if permission is granted
            Box(modifier = Modifier.height(400.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    properties = mapProperties,
                    uiSettings = mapUiSettings,
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng // Update the selected location
                    }
                ) {
                    selectedLocation?.let { location ->
                        Marker(
                            state = MarkerState(position = location),
                            title = "Delivery Location",
                            snippet = "Confirm if this is correct"
                        )
                    }
                }
            }

            // Display selected location coordinates
            selectedLocation?.let {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Text(
                        text = "Delivery Location:Lat: ${it.latitude},\nLng: ${it.longitude}",
                        modifier = Modifier.padding(8.dp))
                }
            }

            // Confirm Button
            Button(
                onClick = {
                    selectedLocation?.let {
                        onConfirmLocation(it) // Pass the location back to the main screen
                        Toast.makeText(context, "Location Confirmed", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange28,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text ="Confirm Location",
                    fontWeight = FontWeight.W700,
                    color = Color.White
                )
            }
        }
    }
}


fun fetchAutocompletePredictions(
    query: String,
    context: Context,
    placesClient: PlacesClient,
    onPredictionsFetched: (List<AutocompletePrediction>) -> Unit
) {
    if (query.isBlank()) {
        onPredictionsFetched(emptyList())
        return
    }

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            onPredictionsFetched(response.autocompletePredictions)
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to fetch locations: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
}

fun fetchPlaceCoordinates(
    placeId: String,
    context: Context,
    placesClient: PlacesClient,
    onCoordinatesFetched: (LatLng) -> Unit
) {
    val request = FetchPlaceRequest.builder(placeId, listOf(
        Place.Field.LAT_LNG)).build()
    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            response.place.latLng?.let { onCoordinatesFetched(it) }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to fetch coordinates", Toast.LENGTH_SHORT).show()
        }
}

