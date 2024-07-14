package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import dev.korryr.bongesha.commons.CartItem
import dev.korryr.bongesha.viewmodels.BongaCategoryViewModel


@Composable
fun CartScreen(
    bongaCategoryViewModel: BongaCategoryViewModel = viewModel(),
    navController: NavController,
) {
    val cartItems by bongaCategoryViewModel.cart.collectAsState()
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = "Cart",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )



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


@Composable
fun CartItemRow(cartItem: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(data = cartItem.item.image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = cartItem.item.name,
                fontWeight = FontWeight.Bold
            )
            Text(text = "${cartItem.quantity}")
            Text(text = "Quantity: ${cartItem.quantity}")
        }
    }
}
