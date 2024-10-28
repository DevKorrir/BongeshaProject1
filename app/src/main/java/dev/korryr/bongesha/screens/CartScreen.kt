package dev.korryr.bongesha.screens

import android.location.Location
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartItem
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.LocationProvider
import dev.korryr.bongesha.viewmodels.fetchUserLocation

@Composable
fun CartScreen(
    //cartViewModel: CartViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val locationProvider = LocationProvider(context)
    val cartViewModel: CartViewModel = viewModel()
    val items = cartViewModel.cart
    val totalPrice = cartViewModel.calculateTotalPrice()
    var userLocation by remember { mutableStateOf<Location?>(null) }
    val deliveryFee = cartViewModel.calculateDeliveryFee(userLocation)
    val coroutineScope = rememberCoroutineScope()

    // Update the delivery fee in ViewModel
    LaunchedEffect(Unit) {
        userLocation = fetchUserLocation(context)
        cartViewModel.updateDeliveryFee(deliveryFee)
    }

    Column(
        modifier = Modifier
            //.verticalScroll(rememberScrollState())
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
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
            ){
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
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
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),

            ){
                Column {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W700,
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Ksh $totalPrice",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Delivery Fee",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "Ksh $deliveryFee",
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

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Amount Payable",
                            color = orange28,
                            fontWeight = FontWeight.W700
                        )

                        Spacer(Modifier.weight(1f))


                        Text(
                            text = "Ksh. $totalPrice",
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
                onClick = {},
                buttonColor = orange28
            )

        } else {
            Column (
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
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
                        navController.navigateUp()
                    },
                    color = Color.White,
                    buttonColor = orange28
                )
            }
        }
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
            modifier = Modifier
                .padding(4.dp)
                .border(
                    1.dp,
                    color = gray01,
                    shape = RoundedCornerShape(12.dp)
                )
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            //contentAlignment = Alignment.Center
        ) {
            val imagePainter = rememberImagePainter(data = item.product.images.firstOrNull())
            Image(
                painter = imagePainter,
                contentDescription = item. product.name,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(100.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(Modifier.width(6.dp))

        Column(
            modifier = Modifier,
        ) {
            Text(
                text = item.product.name,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ksh ${item.product.price}",
                color = green99
            )
        }

        Spacer(Modifier.weight(1f))

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = onRemoveItem
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove item",
                    tint = Color.Gray
                )
            }
            Row(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .background(
                        color = Color.Magenta,
                        shape = RoundedCornerShape(24.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (item.quantity > 1) onQuantityChange(item.quantity - 1)
                    }
                ) {
                    Box{
                        Image(
                            modifier = Modifier
                                .size(16.dp),
                            painter = painterResource( id = R.drawable.minus),
                            contentDescription = "Decrease quantity"
                        )
                    }
                }
                Text(
                    text = "${item.quantity}",
                    modifier = Modifier
                        //.padding(horizontal = 4.dp)
                )

                IconButton(
                    onClick = {
                        onQuantityChange(item.quantity + 1)
                    }
                ) {
                    Box {
                        Icon(
                            modifier = Modifier
                                .size(16.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity"
                        )
                    }
                }
            }
        }

    }
}

//fun calculateDeliveryFeeBasedOnLocation(location: Location?): Int {
//    // Example calculation logic here
//    return if (location != null) {
//        val distanceInKm = /* calculate distance */
//            when {
//                distanceInKm <= 10 -> 50
//                distanceInKm <= 50 -> (distanceInKm * 1.5).toInt()
//                else -> 300
//            }
//    } else {
//        100 // Default fee
//    }
//}