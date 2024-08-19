package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import dev.korryr.bongesha.commons.CartItem
import dev.korryr.bongesha.viewmodels.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = viewModel(),
    navController: NavController
) {
    val items by cartViewModel.cartItems.collectAsState()

    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = "Cart Items",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )

        if (items.isNotEmpty()) {
            LazyColumn {
                items(items) { item ->
                    CartItemRow(CartItem(item, 1))
                }
            }
        } else {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Handle checkout */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Checkout")
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = item.item.image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.item.name,
                fontWeight = FontWeight.Bold
            )
            Text(text = "$${item.item.price}")
            //Text(text = "Quantity: ${item.quantity}")
        }
    }
}
