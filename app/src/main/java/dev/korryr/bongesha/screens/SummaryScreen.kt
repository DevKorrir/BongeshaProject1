package dev.korryr.bongesha.screens

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.*
import dev.korryr.bongesha.viewmodels.CartViewModel

@Composable
fun SummaryScreen(
    cartViewModel: CartViewModel,
    navController: NavController
) {
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("Mpesa") }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(color = gray01)
            .fillMaxSize()
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(40.dp),
                )
            }
            Spacer(Modifier.width(50.dp))
            Text(
                text = "Order Summary",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(8.dp))

        // Payment Method Section
        PaymentMethodSection(selectedPaymentMethod) { method ->
            selectedPaymentMethod = method
        }

        Spacer(Modifier.height(16.dp))

        // Order Review Section
        OrderReviewSection(cartViewModel)

        Spacer(Modifier.height(16.dp))

        // Delivery Address Section
        Text(
            text = "Delivery Address",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = gray01,
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = cartViewModel.getUserDeliveryAddress() ?: "No address provided",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(Modifier.height(16.dp))

        // Confirm Button
        Button(
            onClick = {
                cartViewModel.confirmOrder(selectedPaymentMethod, userLocation)
                navController.navigate(Route.Home.THANK_YOU)
                      },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = orange28)
        ) {
            Text(
                text = "Confirm Order",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun PaymentMethodSection(
    selectedPaymentMethod: String,
    onPaymentMethodSelected: (String) -> Unit
) {
    Text(
        text = "Payment Method",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(0.dp))

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
            val paymentMethods = listOf("Mpesa", "Cash")

            paymentMethods.forEach { method ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(vertical = 4.dp)
                        .clickable { onPaymentMethodSelected(method) }
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedPaymentMethod == method) green99 else gray01
                        )
                        .background(
                            color = if (selectedPaymentMethod == method) green99 else gray01,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = if (method == "Mpesa") R.drawable.e_payment else R.drawable.cash_method

                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(
                                if (selectedPaymentMethod == method) orange28 else Color.LightGray
                            )
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = if (method == "Mpesa") "M-pesa" else "Cash on delivery",
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            color = if (selectedPaymentMethod == method) Color.White else Color.Black
                        )
                        RadioButton(
                            selected = selectedPaymentMethod == method,
                            onClick = { onPaymentMethodSelected(method) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = orange28,
                                unselectedColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    }
    Column {

    }
}

@Composable
fun OrderReviewSection(cartViewModel: CartViewModel) {
    Text(
        text = "Order Review",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = gray01,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subtotal:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Ksh. ${cartViewModel.calculateSubtotal()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Offer:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Ksh. ${cartViewModel.calculateTotalOffer()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tax:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Ksh. ${cartViewModel.calculateTotalTax()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount Payable:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = orange28
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Ksh. ${cartViewModel.calculateAmountPayable()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = orange28
                )
            }
        }
    }
}
