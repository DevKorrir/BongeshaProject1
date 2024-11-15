package dev.korryr.bongesha.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.orange28
import java.time.LocalDate
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckOut(
    navController: NavController,
){
    // Input states
    var deliverName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("+254") }
    var landMark by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var selectedDeliveryMethod by remember { mutableStateOf("HomeDelivery") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var landmarkError by remember { mutableStateOf<String?>(null) }
    var locationError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }
    var timeError by remember { mutableStateOf<String?>(null) }

    // Validation function
    fun validateFields(): Boolean {
        var isValid = true

        nameError = if (deliverName.isBlank()) {
            isValid = false
            "Name cannot be empty"
        } else null

        phoneError = if (phoneNumber.isBlank() || phoneNumber.length != 13 || !phoneNumber.startsWith("+254")) {
            isValid = false
            "Enter a valid phone number"
        } else null

        landmarkError = if (landMark.isBlank()) {
            isValid = false
            "Building/House cannot be empty"
        } else null

        locationError = if (selectedLocation == null) {
            isValid = false
            "Location must be selected"
        } else null

        dateError = if (selectedDate == null) {
            isValid = false
            "Please select a delivery date"
        } else null

        timeError = if (selectedTime == null) {
            isValid = false
            "Please select a delivery time"
        } else null

        return isValid
    }
    //var areFieldsValid by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//        areFieldsValid = validateFields()
//    }

    // Enable button only if all inputs are valid
    val isButtonEnabled = listOf(
        nameError,
        phoneError,
        landmarkError,
        locationError,
        dateError,
        timeError
    ).all { it == null }

    Scaffold (
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .background(
                gray01
            ),
        bottomBar = {
            BottomAppBar (
                containerColor = gray01,
            ){
                Row (
                    modifier = Modifier.fillMaxWidth()
                ){
                    BongaButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Proceed to payment",
                        color = Color.White,
                        enabled = true,
                        onClick = {
                            //if (validateFields()) {  // Run validation on click
                                navController.navigate(Route.Home.SUMMARY)  // Navigate only if fields are valid
                            //}
                        },
                        buttonColor = orange28
                    )
                }
            }

        }
    ){ innerPadding ->

        Column (
            modifier = Modifier
                //.verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    color = gray01
                )
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Route.Home.CART)
                        }
                        .clip(
                            CircleShape
                        )
                        .border(
                            1.dp,
                            color = Color.Transparent,
                            shape = CircleShape
                        )
                        .size(30.dp)
                        .background(
                            Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Route.Home.CART)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.LightGray
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Text(
                    text = "Checkout",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(Modifier.height(8.dp))

            Column (
                modifier = Modifier
                    .background(
                        color = gray01,
                    )
            ){
                Text(
                    text = "Delivery Method",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(8.dp))

                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Button(
                        modifier = Modifier
                            .width(150.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedDeliveryMethod == "HomeDelivery") orange28 else Color.LightGray
                        ),
                        onClick = {
                            selectedDeliveryMethod = "HomeDelivery"
                        }
                    ) {
                        Text(
                            "Home Delivery",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        modifier = Modifier
                            .width(150.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedDeliveryMethod == "Pickup") orange28 else Color.LightGray
                        ),
                        onClick = {
                            selectedDeliveryMethod = "Pickup"
                        }
                    ) {
                        Text(
                            "Pick Up",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Column (
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(
                            color = gray01
                        )
                ){
                    if (selectedDeliveryMethod == "HomeDelivery"){
                        HomeDeliveryScreen(
                            deliverName = deliverName,
                            phoneNumber = phoneNumber,
                            landMark = landMark,
                            selectedLocation = selectedLocation,
                            selectedDate = selectedDate,
                            selectedTime = selectedTime,
                            onNameChange = { deliverName = it },
                            onPhoneChange = { phoneNumber = it },
                            onLandMarkChange = { landMark = it },
                            onDateChange = { selectedDate = it },
                            onTimeChange = { selectedTime = it },
                            onLocationChange = { selectedLocation = it }
                        )
                    } else {
                        PickupScreen()
                    }

                    Spacer(Modifier.height(8.dp))


                }




            }
        }
    }
}



