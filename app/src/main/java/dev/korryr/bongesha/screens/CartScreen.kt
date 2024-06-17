package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.korryr.bongesha.commons.CartItem
import dev.korryr.bongesha.commons.Item
import dev.korryr.bongesha.viewmodels.CartItemViewModel


@Composable
fun CartScreen(
    cartItemViewModel: CartItemViewModel = viewModel(),
    navController: NavController,
    cartItems: List<CartItem>
) {
    val cartItems by cartItemViewModel.cart.collectAsState()
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = "Cart",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        if (cartItems.isEmpty()) {
            Text(
                text = "Your cart is empty.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            cartItems.forEach { cartItem ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = cartItem.item.image),
                        contentDescription = cartItem.item.name,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = cartItem.item.name, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "$${cartItem.item.price}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Quantity: ${cartItem.quantity}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun BadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box {
        content()
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
        ) {
            badge()
        }
    }
}