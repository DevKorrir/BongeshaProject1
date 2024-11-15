package dev.korryr.bongesha.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28

@Composable
fun OrdersScreen(
    navController: NavController
) {
    val items = listOf(
        OrderItem(name = "Chapati", quantity = 3, totalPrice = 45.0f),
        OrderItem(name = "Soda", quantity = 2, totalPrice = 100.0f),
        OrderItem(name = "Chipsi", quantity = 1, totalPrice = 150.0f)
    )

    Column {
        // we shall have
        OrderDetailsScreen(
            orderId = "12345",
            orderDate = "15th Nov 2024",
            items = items,
            totalAmount = 295.0f,
            paymentMethod = "M-Pesa",
            deliveryAddress = "123 Example Street, Nairobi",
            navController = navController
        )
        Row {
            Text(text = "nothing yet")
        }
    }
}


@Composable
fun OrderDetailsScreen(
    orderId: String,
    orderDate: String,
    items: List<OrderItem>,
    totalAmount: Float,
    paymentMethod: String,
    deliveryAddress: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Column {
            Text(
                text = "Order Details",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = green99,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Order Summary
            Text(
                text = "Order ID: $orderId",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(
                text = "Order Date: $orderDate",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Items Section
            Text(
                text = "Items",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantity} x ${item.name}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "Ksh ${item.totalPrice}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment and Delivery Details
            Text(
                text = "Payment Method: $paymentMethod",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(
                text = "Delivery Address: $deliveryAddress",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Total Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Amount:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = orange28
                )
                Text(
                    text = "Ksh $totalAmount",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = orange28
                )
            }
        }

        // Back to Home Button
        Button(
            onClick = { navController.navigate("home") },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = green99),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Back to Home",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class OrderItem(
    val name: String,
    val quantity: Int,
    val totalPrice: Float
)

