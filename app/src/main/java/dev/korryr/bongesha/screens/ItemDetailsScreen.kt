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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.WishlistItems
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.SelectedItemViewModel
import dev.korryr.bongesha.viewmodels.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    navController: NavController,
    itemId: String,
    selectedItemViewModel: SelectedItemViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
    wishlistViewModel: WishlistViewModel = viewModel(), // Add this line
    onClick: () -> Unit
) {
    val categories by categoryViewModel.categories.collectAsState()
    val selectedItem by selectedItemViewModel.selectedItem.collectAsState()
    var quantity by remember { mutableIntStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }
    val context = LocalContext.current

    //val wishlistViewModel by wishlistViewModel.wishlistItems.collectAsState()


    LaunchedEffect(itemId) {
        val item = categories.flatMap { it.items }.find { it.id == itemId }
        selectedItemViewModel.selectedItem(item ?: return@LaunchedEffect)

    }

    selectedItem?.let { item ->
        val isItemInWishlist = wishlistViewModel.isItemInWishlist(wishlistItem = WishlistItems(
            item.id,
            item.name,
            item.description,
            item.image,
            item.price
            ))

        val modalBottomSheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var isShowBottomSheet by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(
                    gray01
                )
                .fillMaxSize()
                .padding(top = 2.dp, start = 16.dp, end = 16.dp, bottom = 50.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                IconButton(
                    onClick = {
                        isShowBottomSheet = false

                        //navController.navigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Close",
                        tint = orange28
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
                    Spacer(modifier = Modifier.weight(1f))
                Box (
                    modifier = Modifier
                        .clickable {
                            wishlistViewModel.toggleItemInWishlist(
                                WishlistItems(
                                    item.id,
                                    item.name,
                                    item.description,
                                    item.image,
                                    item.price
                                )
                            )
                            isFavorite = !isFavorite
                            // Add to favorite list in your ViewModel
                            wishlistViewModel.addItemToWishlist(
                                WishlistItems(
                                    item.id,
                                    item.name,
                                    item.description,
                                    item.image,
                                    item.price
                                )
                            )
                            // Show a toast
                            Toast
                                .makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .size(30.dp)
                        .background(
                            color = gray01,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Transparent,
                            shape = CircleShape
                        )
                        .clip(
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(id = if (isItemInWishlist) R.drawable.heart_icon else R.drawable.heart_icon_fave),
                        contentDescription = "wishlist",
                        contentScale = ContentScale.Fit,
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                            if (isItemInWishlist) Color.Red else Color.Gray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Item details content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
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
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Deviation",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                    )
                }
                Row {
                    // variation goes here
                }
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,

                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Ksh. ${item.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

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
                                if (quantity > 0) quantity--
                            }) {
                            Image(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.minus),
                                contentDescription = "Remove"
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
                                quantity++
                            }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }
                    }
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
                            //add to wishlist
                            wishlistViewModel.addItemToWishlist(
                                WishlistItems(
                                    item.id,
                                    item.name,
                                    item.description,
                                    item.image,
                                    item.price
                                )
                            )
                        }
                    )
//                    {
//                        //add to wish list
//                        //wishlistViewModel.addToWishlist(item.id)
//
//
//                        // Show a toast
//                        Toast
//                            .makeText(context, "Added to Buy Later", Toast.LENGTH_SHORT)
//                            .show()
//                    }
                    BongaButton(
                        modifier = Modifier
                            .width(150.dp)
                            .height(60.dp),
                        label = "Buy Now",
                        color = Color.White,
                        buttonColor = orange28
                    ) {
                        cartViewModel.addToCart(item)
                        // Show a toast or snackbar
                        Toast
                            .makeText(context, "Added to Cart", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }

        }
    } ?: run {
        // Show a loading or error message if selectedItem is null
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Loading item details...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
