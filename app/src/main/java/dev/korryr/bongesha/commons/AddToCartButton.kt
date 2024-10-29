package dev.korryr.bongesha.commons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.Product

@Composable
fun AddToCartButton(
    product: Product,
    cartViewModel: CartViewModel,
    quantity: Int =1
) {
    val isInCart by remember { derivedStateOf { cartViewModel.isInCart(product) } }
    val itemCount = product.quantityCount

    BongaButton(
        label = if (isInCart) "In Cart" else "Add to Cart",
        buttonColor = if (isInCart) Color.Gray else orange28,
        onClick = {
            if (!isInCart) cartViewModel.addToCart(product, quantity = 1)
        },
        enabled = !isInCart && quantity <= itemCount
    )
}
