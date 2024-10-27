package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import dev.korryr.bongesha.viewmodels.CartItem
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.Product

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = viewModel(),
    navController: NavController
) {
    val items = cartViewModel.cart // No need for state, observe the list directly
    val totalPrice = cartViewModel.calculateTotalPrice()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Cart Items",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )

        if (items.isNotEmpty()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items) { item ->
                    CartItemRow(
                        item = item,
                        onQuantityChange = { newQuantity ->
                            cartViewModel.updateQuantity(item.product, newQuantity)
                        },
                        onRemoveItem = {
                            cartViewModel.removeFromCart(item.product)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total price and checkout button
            Text(
                text = "Total: Ksh. $totalPrice",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                modifier = Modifier.align(Alignment.End)
            )

            Button(
                onClick = { /* Handle checkout */ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text(text = "Checkout")
            }
        } else {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
//        Button(
//            onClick = { /* Handle checkout logic */ },
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        ) {
//            Text(text = "Checkout")
//        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = item.product.images),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.product.name,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ksh. ${item.product.price}"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                    if (item.quantity > 1) onQuantityChange(item.quantity - 1)
                }
                ) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Decrease quantity")
                }
                Text(
                    text = "${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(
                    onClick = {
                        onQuantityChange(item.quantity + 1)
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase quantity"
                    )
                }
            }
        }

        // Remove button
        IconButton(onClick = onRemoveItem) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Remove item")
        }
    }
}
