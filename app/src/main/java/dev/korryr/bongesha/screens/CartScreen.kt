package dev.korryr.bongesha.screens

import android.location.Location
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange100
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.AuthViewModel
import dev.korryr.bongesha.viewmodels.CartItem
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.fetchUserLocation
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {
    BackHandler {
        navController.navigate(Route.Home.HOME) {
            popUpTo(Route.Home.CART) { inclusive = true }
        }
    }

    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState()
    var userLocation by remember { mutableStateOf<Location?>(null) }
    val feet = cartViewModel.calculateDeliveryFee(userLocation)


    // Update the delivery fee in ViewModel
    LaunchedEffect(Unit) {
        userLocation = fetchUserLocation(context)
        val fee = cartViewModel.calculateDeliveryFee(userLocation).toFloat()
        cartViewModel.updateDeliveryFee(fee)
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = CircleShape
                    )
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(Route.Home.HOME)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = "My Cart",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(24.dp))

        if (cartItems.isNotEmpty()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
//                        onQuantityChange = { newQuantity ->
//                            cartViewModel.updateQuantity(cartItem, newQuantity)
//                        },
                        onRemoveItem = {
                            cartViewModel.removeFromCart(cartItem)
                            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
                        },
                        cartViewModel = cartViewModel,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = orange28,
                        ambientColor = orange28,
                        clip = true,
                    )
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .background(
                        color = gray01,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Subtotal:",
                            style = MaterialTheme.typography.bodyLarge,
                            //fontWeight = FontWeight.W700,
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Ksh.${cartViewModel.calculateSubtotal()}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
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
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Ksh.${cartViewModel.calculateTotalOffer()}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
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
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Ksh.${cartViewModel.calculateTotalTax()}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    }

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
                            text = "Ksh ${cartViewModel.deliveryFee}",
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
                            text = "Ksh. ${cartViewModel.calculateAmountPayable()}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = orange28,
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            BongaButton(
                modifier = Modifier.fillMaxWidth(),
                label = "Checkout",
                color = Color.White,
                onClick = {
                    navController.navigate(Route.Home.CHECKOUT)
                },
                buttonColor = orange28
            )
        } else {
            EmptyCartView(navController)
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    cartViewModel: CartViewModel,
    onRemoveItem: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    var cartQuantity by remember { mutableIntStateOf(cartItem.quantity) }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(
                RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(4.dp)
                .border(
                    1.dp,
                    color = gray01,
                    shape = RoundedCornerShape(12.dp)
                )
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
        ) {
            val imagePainter = rememberAsyncImagePainter(model = cartItem.images.firstOrNull() ?: "")

            Image(
                painter = imagePainter,
                contentDescription = cartItem.name,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(100.dp),
                contentScale = ContentScale.FillBounds
            )

            // Show loading indicator if the image is in loading state
            if (imagePainter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    color = Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }

        }

        Spacer(Modifier.width(6.dp))

        Column {

            Text(
                text = cartItem.name,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ksh ${cartItem.price}",
                color = green99
            )

            Text(
                text = "Total: ${cartItem.price * cartQuantity}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }

        Spacer(Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            spotColor = orange28,
                            ambientColor = orange28,
                            clip = true,
                        )
                        .background(
                            color = orange28,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = orange28,
                            shape = CircleShape
                        )
                        .size(36.dp),
                ) {
                    Text(
                        text = "${cartItem.quantityCount}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        cartViewModel.removeFromCart(cartItem)
                        Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove item",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .height(32.dp)
                    .padding(end = 4.dp)
                    .background(
                        color = orange100,
                        shape = RoundedCornerShape(24.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var showDeleteIcon by remember { mutableStateOf(false) }
                val currentQuantity = cartItems.find { it.id == cartItem.id }?.quantity ?: 1
                IconButton(
                    onClick = {
                        if (currentQuantity > 1){
                            cartViewModel.updateCartQuantity(cartItem,currentQuantity - 1)
                        } else {
                            showDeleteIcon = currentQuantity == 0
                        }
                    }
                ) {
                    Box {
                        Image(
                            modifier = Modifier.size(12.dp),
                            painter = painterResource(id = R.drawable.minus),
                            contentDescription = "Decrease quantity"
                        )
                    }
                }
                if (showDeleteIcon) {
                    IconButton(
                        onClick = {
                            // Handle delete action here (e.g., remove item from cart)
                            cartViewModel.removeFromCart(cartItem)
                            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
                            showDeleteIcon = false  // Hide delete icon after deleting
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove item",
                            tint = Color.Gray
                        )
                    }
                }


                Text(
                    text = "x${currentQuantity}",
                    modifier = Modifier
                )

                IconButton(
                    onClick = {
                        if (currentQuantity < cartItem.quantityCount) {
                            cartViewModel.updateCartQuantity(cartItem, currentQuantity + 1)
                        } else {
                            Toast.makeText(context, "Only ${cartItem.quantityCount} items available", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Box {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyCartView(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your cart is empty",
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.bongesha_sec_icon),
            contentDescription = "Empty cart",
            modifier = Modifier.size(200.dp)
        )

        Spacer(Modifier.height(24.dp))

        BongaButton(
            label = "Continue Shopping now!",
            onClick = {
                navController.navigate(Route.Home.HOME)
            },
            color = Color.White,
            buttonColor = orange28
        )
    }
}
