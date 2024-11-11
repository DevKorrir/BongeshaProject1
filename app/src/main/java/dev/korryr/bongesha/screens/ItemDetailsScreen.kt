package dev.korryr.bongesha.screens

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartItem
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.Product
import dev.korryr.bongesha.viewmodels.WishlistItems
import dev.korryr.bongesha.viewmodels.WishlistViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    cartItem: CartItem,
    product: Product,
    onClick: () -> Unit,
    cartViewModel: CartViewModel = viewModel(),
    wishlistViewModel: WishlistViewModel = viewModel()

) {
    // Collect the state of whether the item is in the wishlist
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()

    // Check if the current product is in the wishlist
    val isInWishlist = wishlistItems.any { it.id == product.id }

    //var quantity by remember { mutableIntStateOf(1) }
    var quantity by remember { mutableIntStateOf(cartViewModel.getCartItemQuantity(product).takeIf { it > 0 } ?: 1) }
    var isFavorite by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isInCart by remember { mutableStateOf(cartViewModel.isItemInCart(product)) }  // New state to track if item is in the cart
    var quantityCount by remember { mutableIntStateOf(product.quantityCount) }

    Scaffold (
        containerColor = gray01,
        topBar = {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Box (
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Transparent,
                            shape = CircleShape
                        )
                        .clip(
                            shape = CircleShape
                        )
                        .clickable {
                            isFavorite = !isFavorite
                            if (isFavorite) {
                                wishlistViewModel.addItemToWishlist(
                                    WishlistItems(
                                        id = product.id,
                                        name = product.name,
                                        price = product.price,
                                        imageUrl = product.images.toString()
                                    )
                                )
                            } else {
                                wishlistViewModel.removeItemFromWishlist(product.id)
                            }
                        },

                    contentAlignment = Alignment.Center
                ){
                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    wishlistViewModel.toggleItemInWishlist(
                                        WishlistItems(
                                            id = product.id,
                                            name = product.name,
                                            price = product.price,
                                            imageUrl = product.images.toString()
                                        )
                                    )
                                },
                            painter = painterResource(id = if (isInWishlist)R.drawable.heart_icon else R.drawable.heart_icon_fave),
                            contentDescription = "wishlist",
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                if (isInWishlist) Color.Red else Color.Gray
                            )
                        )

                }
            }
        },

        bottomBar = {
            BottomAppBar (
                containerColor = gray01,
            ){
                //buy now buy later
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    BongaButton(
                        modifier = Modifier
                            .width(150.dp)
                            .height(60.dp),
                        label = "Buy Later",
                        color = Color.White,
                        buttonColor = orange28,
                        onClick = {
                            wishlistViewModel.addItemToWishlist(
                                wishlistItems = WishlistItems(
                                    id = product.id,
                                    name = product.name,
                                    price = product.price,
                                    imageUrl = product.images.toString()
                                )
                            )
                            Toast.makeText(context,"Added to Wishlist", Toast.LENGTH_SHORT).show()
                        },
                    )

                    BongaButton(
                        modifier = Modifier
                            .width(150.dp)
                            .height(60.dp),
                        label = if (isInCart)"In Cart" else "Buy Now",
                        color = Color.White,
                        buttonColor = if (isInCart) Color.Gray else orange28,
                        enabled = !isInCart,
                        onClick = {
                            if (!isInCart) {
                                if (quantity <= product.quantityCount) {
                                    // Add the product to the cart with the specified quantity
                                    cartViewModel.addToCart(
                                        product = product, // Keep the original product details intact
                                        quantity = quantity // Pass the desired quantity to the ViewModel
                                    )

                                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()

                                    // Save product to Firestore under the user's account
                                    cartViewModel.saveProductToUserAccount(context, product, quantity)
                                } else {
                                    Toast.makeText(context, "Only ${product.quantityCount} items available", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    )
                }
            }
        },
    ){ innerPadding ->
        // Content for the screen
        Column(
            modifier = Modifier
                .background(
                    gray01
                )
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Item details content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            // Handle image click if needed
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val imagePainter =
                        rememberAsyncImagePainter(model = product.images.firstOrNull())
                    Image(
                        painter = imagePainter,
                        contentDescription = product.name,
                        contentScale = ContentScale.FillBounds
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
//                    Text(
//                        text = "Deviation",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 26.sp,
//                    )
                }
                Row {
                    // variation goes here
                }
//                Text(
//                    text = item.name,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 26.sp,
//                )

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                ){
                    Text(
                        text = "Ksh ${product.price}",
                        fontSize = 26.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W700
                    )
                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "Ksh ${product.markPrice}",
                        textDecoration = TextDecoration.LineThrough
                    )

                    Spacer(Modifier.weight(1f))

                    product.offerPercentage?.let {
                        Text(
                            text = "Offer: $it% off",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                    ){
                        Text(
                            text = "${product.quantityCount}",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = product.description ?: "No description available",
                    style = MaterialTheme.typography.bodySmall
                )

                //Quantity counter
                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .size(50.dp),
                        onClick = {
                            if (quantity > 0){
                                quantity--
                            }
                        }
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.minus),
                            contentDescription = "Decrease"
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(
                        text = "$quantity"
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    IconButton(
                        modifier = Modifier
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .size(50.dp),
                        onClick = {
                            if (quantity < quantityCount ){
                                quantity++
                            } else {
                                Toast.makeText(context, "Only ${product.quantityCount} items available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase"
                        )
                    }
                }

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }

        }
    }


}

