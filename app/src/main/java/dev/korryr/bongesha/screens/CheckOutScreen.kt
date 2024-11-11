package dev.korryr.bongesha.screens

import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.commons.LocationPickerBottomSheet
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.orange100
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOut(
    navController: NavController,
    cartViewModel: CartViewModel
){
    var deliverName by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var landMark by remember { mutableStateOf("") }
    val context = LocalContext.current

    var selectedPaymentMethod by remember { mutableStateOf("Mpesa") }
    var isSelectedPaymentMethod by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLocationSheetOpen by remember { mutableStateOf(false) }
    var locationText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    val totalPrice = cartViewModel.calculateTotalPrice()
    var userLocation by remember { mutableStateOf<Location?>(null) }
    val deliveryFee = cartViewModel.calculateDeliveryFee(userLocation)


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
                .verticalScroll(rememberScrollState())
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
                    text = "Delivery Address",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(0.dp))

                Bongatextfield(
                    input = deliverName,
                    label = "",
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

                Spacer(Modifier.height(12.dp))

                KenyanPhoneNumberField()

                Spacer(Modifier.height(0.dp))

                Bongatextfield(
                    input = landMark,
                    label = "",
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

                Row (
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = orange28
                        ),
                        onClick = {
                            isLocationSheetOpen = true
                        }
                    ) {
                        Text(
                            "PickUp Location",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    Row (
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(start = 12.dp, end = 5.dp)
                            //.height(0.dp)
                            .width(200.dp)
                    ){
                        Text(
                            text = "Lat: ${selectedLocation?.latitude}\nLng: ${selectedLocation?.longitude}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Payment Method",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700
                )

                Spacer(Modifier.height(8.dp))

                //payment method
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = true
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                ){
                    Column (
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ){

                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .height(55.dp)
                                .clickable {
                                    selectedPaymentMethod = "Cash"
                                }
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selectedPaymentMethod == "Cash") orange28 else gray01
                                )
                                .background(
                                    color = if (selectedPaymentMethod == "Cash") orange100 else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .fillMaxWidth()
                        ){
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.cash_method),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(if (selectedPaymentMethod == "Cash")
                                        orange28 else Color.LightGray )
                                )

                                Spacer(Modifier.width(8.dp))

                                Text(
                                    text = "Cash",
                                    modifier = Modifier.weight(1f),
                                    fontSize = 16.sp
                                )

                                RadioButton(
                                    selected = selectedPaymentMethod == "Cash",
                                    onClick = {
                                        selectedPaymentMethod = "Cash"
                                              },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = orange28,
                                        unselectedColor = Color.Gray
                                    )
                                )
                            }

                        }

                        Spacer(Modifier.height(10.dp))

                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .height(55.dp)
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selectedPaymentMethod == "Mpesa") orange28 else gray01

                                )
                                .background(
                                    color = if (selectedPaymentMethod == "Mpesa") orange100 else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .fillMaxWidth()
                                .clickable {
                                    selectedPaymentMethod = "Mpesa"
                                }
                        ){
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.e_payment),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(if (selectedPaymentMethod == "Mpesa")
                                        orange28 else Color.LightGray )
                                )

                                Spacer(Modifier.width(8.dp))

                                Text(
                                    text = "M-pesa",
                                    modifier = Modifier.weight(1f),
                                    fontSize = 16.sp
                                )

                                RadioButton(
                                    selected = selectedPaymentMethod == "Mpesa",
                                    onClick = { selectedPaymentMethod = "Mpesa" },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = orange28,
                                        unselectedColor = Color.Gray
                                    )
                                )

                            }

                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                //invoice summary
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = true
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ){
                    Column (
                        modifier = Modifier
                            .padding(8.dp)
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Value",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.W700,
                                color = Color.Black,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "$totalPrice KES",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        HorizontalDivider(
                            color = Color.LightGray,
                            thickness = 1.dp
                        )

                        Spacer(Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Delivery Fee",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "$deliveryFee KES",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        HorizontalDivider(
                            color = Color.LightGray,
                            thickness = 1.dp
                        )

                        Spacer(Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Amount Payable",
                                color = orange28,
                                fontWeight = FontWeight.W700
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "${totalPrice + deliveryFee} KES",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = orange28,
                            )
                        }
                    }

                }



            }
        }
    }

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

