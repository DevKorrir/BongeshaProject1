package dev.korryr.bongesha.screens

import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.commons.LocationPickerBottomSheet
import dev.korryr.bongesha.ui.theme.blue88
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.orange28
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

///////////////////////////////////////////////////////////////////////////
// home delivery screen here, tranfer to mvvm later
///////////////////////////////////////////////////////////////////////////

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDeliveryScreen(
    deliverName: String,
    phoneNumber: String,
    landMark: String,
    selectedLocation: LatLng?,
    selectedDate: LocalDate?,
    selectedTime: LocalTime?,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onLandMarkChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    onLocationChange: (LatLng) -> Unit
) {
    val context = LocalContext.current
    var isLocationSheetOpen by remember { mutableStateOf(false) }
    var locationText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    // Error states
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
                onNameChange(it)
                validateFields() // Trigger validation on field change
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        nameError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(Modifier.height(8.dp))

        KenyanPhoneNumberField(
            phoneNumber = phoneNumber,
            onPhoneChange = {
                onPhoneChange(it)
                validateFields()
            }
        )
        phoneError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(Modifier.height(8.dp))

        Bongatextfield(
            input = landMark,
            hint = "Building/House*",
            fieldDescription = "",
            onChange = {
                onLandMarkChange(it)
                validateFields()
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        landmarkError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

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
        locationError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

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
                    onDateChange(LocalDate.of(year, month + 1, dayOfMonth))
                    validateFields()
                }
                datePicker.show()
            },
            label = selectedDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "Choose Delivery Date",
            color = Color.White,
            buttonColor = orange28,
        )
        dateError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
        Spacer(Modifier.height(12.dp))

        BongaButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            onClick = {
                val timePicker = TimePickerDialog(context, { _, hourOfDay, minute ->
                    onTimeChange(LocalTime.of(hourOfDay, minute))
                    validateFields()
                }, 12, 0, false)
                timePicker.show()
            },
            label = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Choose Delivery Time",
            color = Color.White,
            buttonColor = orange28,
        )
        timeError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(Modifier.height(16.dp))

        // Display selected date and time
        if (selectedDate != null && selectedTime != null) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
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
                        onLocationChange(location)
                        locationText = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                        isLocationSheetOpen = false // Close BottomSheet
                    },
                    onClose = { isLocationSheetOpen = false }
                )
            }
        }
    }
}

@Composable
fun KenyanPhoneNumberField(
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
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
    //var phoneNo by remember { mutableStateOf("+254") }
    val context = LocalContext.current
    val kenyanPhonePattern = "^\\+254\\d{9}$".toRegex()

    Column {
        OutlinedTextField(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(),
            colors = colors,
            value = phoneNumber,
            onValueChange = { newInput ->
                // Only update if it starts with +254 and doesn't exceed 13 characters
                if (newInput.startsWith("+254") && newInput.length <= 13) {
                    onPhoneChange(newInput)
                } else if (newInput.isEmpty()) {
                    // Allow clearing the input
                    onPhoneChange("+254")
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