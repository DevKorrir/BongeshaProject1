package dev.korryr.bongesha.screens

import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.commons.LocationPickerBottomSheet
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.blue88
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@Composable
fun CheckOut(
    navController: NavController,
    cartViewModel: CartViewModel
){
    var selectedDeliveryMethod by remember { mutableStateOf("HomeDelivery") }
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
                        label = "Place Order",
                        color = Color.White,
                        onClick = {
                            //navController.navigate(Route.Home.CHECKOUT)
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
                        HomeDeliveryScreen()
                    } else {
                        PickupScreen()
                    }
                }




            }
        }
    }
}

@Composable
fun KenyanPhoneNumberField(
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
        errorContainerColor = Color.Red,
        unfocusedTextColor = Color.Black,
        disabledTextColor = Color.Black,
        errorTextColor = Color.Red,
        errorIndicatorColor = Color.Red,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = orange28,
        focusedLeadingIconColor = orange28,
        unfocusedLeadingIconColor = Color.Black,
        cursorColor = orange28,
        focusedLabelColor = orange28
    )
) {
    var phoneNo by remember { mutableStateOf("+254") }
    val context = LocalContext.current
    val kenyanPhonePattern = "^\\+254\\d{9}$".toRegex()

    Column {
        OutlinedTextField(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(),
            colors = colors,
            value = phoneNo,
            onValueChange = { newInput ->
                // Only update if it starts with +254 and doesn't exceed 13 characters
                if (newInput.startsWith("+254") && newInput.length <= 13) {
                    phoneNo = newInput
                } else if (newInput.isEmpty()) {
                    // Allow clearing the input
                    phoneNo = "+254"
                }
                if (newInput.length == 13) {
                    if (newInput.matches(kenyanPhonePattern)) {
                        Toast.makeText(context, "Correct format", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Invalid number format", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )
    }
}


@Composable
fun PickupScreen() {
    // not yet impliemented
    Text(
        text = "Delivered to Pickup-Station",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()

    ){

        Spacer(Modifier.height(50.dp))
        Text(
            textAlign = TextAlign.Center ,
            text = "Sorry coming soon...",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = blue88
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDeliveryScreen() {
    var deliverName by remember { mutableStateOf("") }
    var landMark by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLocationSheetOpen by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    // Date and Time Picker state variables
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    Text(
        text = "Home Delivery",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    Column {
        Bongatextfield(
            input = deliverName,
            hint = " Full Name*",
            fieldDescription = "",
            onChange = {
                deliverName = it.capitalize()
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(8.dp))

        KenyanPhoneNumberField()

        Spacer(Modifier.height(8.dp))

        Bongatextfield(
            input = landMark,
            hint = "Building/House*",
            fieldDescription = "",
            onChange = {
                landMark = it.capitalize()
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        Spacer(Modifier.height(8.dp))

        BongaButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            label = "Choose Your Location",
            color = Color.White,
            onClick = {
                isLocationSheetOpen = true

            },
            buttonColor = orange28
        )

        Spacer(Modifier.height(8.dp))

        if (selectedLocation?.latitude != null && selectedLocation?.longitude != null) {
            Row (
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        clip = true,
                        spotColor = blue88,
                        ambientColor = blue88
                    )
                    .background(
                        color = gray01,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
                    .fillMaxWidth()
            ){
                Text(
                    text = "Lat: ${selectedLocation?.latitude}\nLng: ${selectedLocation?.longitude}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Calendar
        Text(
            text = "Schedule Delivery Date",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Date Picker Button
        BongaButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            onClick = {
                val datePicker = android.app.DatePickerDialog(context)
                datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                }
                datePicker.show()
            },
            label = selectedDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "Choose Delivery Date",
            color = Color.White,
            buttonColor = orange28,
        )

        Spacer(Modifier.height(12.dp))

        BongaButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            onClick = {
                // Time picker dialog implementation
                val timePicker = TimePickerDialog(context, { _, hourOfDay, minute ->
                    selectedTime = LocalTime.of(hourOfDay, minute)
                }, 12, 0, false)
                timePicker.show()
            },
            label = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Choose Delivery Time",
            color = Color.White,
            buttonColor = orange28,
        )

        Spacer(Modifier.height(16.dp))

        // Display selected date and time
        if (selectedDate != null && selectedTime != null) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    //.height(50.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        clip = true,
                        spotColor = blue88,
                        ambientColor = blue88
                    )
                    .background(
                        color = gray01,
                        shape = RoundedCornerShape(8.dp)
                    )
            ){
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Will be delivered on: \n ${selectedDate!!.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}, " +
                            "${selectedTime!!.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = blue88,
                    textAlign = TextAlign.Center
                )
            }
        }

        //Spacer(Modifier.height(16.dp))






}
    // bootomsheet
    if (isLocationSheetOpen) {
        ModalBottomSheet(
            dragHandle = {
                //Icon(Icons.Default.toString(), contentDescription = null)
            },
            containerColor = gray01,
            onDismissRequest = {
                isLocationSheetOpen = false
            },
            sheetState = sheetState,
        ){
            Column (
                modifier = Modifier
                    .heightIn(max = 650.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    ElevatedButton(
                        modifier = Modifier,
                        onClick = {
                            isLocationSheetOpen = false
                        },
                        colors = ButtonColors(
                            containerColor = gray01,
                            contentColor = Color.Gray,
                            disabledContainerColor = gray01,
                            disabledContentColor = Color.Gray
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            tint = Color.Red,
                            imageVector = Icons.Default.Close,
                            contentDescription = ""
                        )
                    }

                }

                LocationPickerBottomSheet(
                    onConfirmLocation = { location ->
                        selectedLocation = location
                        locationText = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                        isLocationSheetOpen = false // Close BottomSheet
                    },
                    onClose = { isLocationSheetOpen = false }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(selectedDate: LocalDate?, onDateSelected: (LocalDate) -> Unit) {
    val today = LocalDate.now()
    val currentMonth = YearMonth.of(today.year, today.month)
    val daysInMonth = currentMonth.lengthOfMonth()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(daysInMonth) { dayIndex ->
            val day = dayIndex + 1
            val date = currentMonth.atDay(day)  // Create a valid date with the day number
            val isSelected = date == selectedDate

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onDateSelected(date) }
            ) {
                Text(
                    text = day.toString(),
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerView(selectedTime: LocalTime?, onTimeSelected: (LocalTime) -> Unit) {
    val timeSlots = listOf(
        LocalTime.of(9, 0),
        LocalTime.of(11, 0),
        LocalTime.of(13, 0),
        LocalTime.of(15, 0),
        LocalTime.of(17, 0)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(timeSlots) { time ->
            val isSelected = time == selectedTime

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .width(80.dp)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onTimeSelected(time) }
            ) {
                Text(
                    text = time.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

