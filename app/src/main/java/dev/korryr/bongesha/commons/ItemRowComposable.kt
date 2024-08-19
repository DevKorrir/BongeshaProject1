package dev.korryr.bongesha.commons

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.screens.ItemDetailsScreen
import dev.korryr.bongesha.ui.theme.blue88
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange01
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.WishlistViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ItemRow(
    modifier: Modifier = Modifier,
    item: Item,
    wishlistViewModel: WishlistViewModel,
    viewModel: CartViewModel,
    navController: NavController,
    onAddToCart: (CartItem) -> Unit


    ) {
    var isFavorite by remember { mutableStateOf(false) }
    val isItemInWishlist = wishlistViewModel.isItemInWishlist(wishlistItem = WishlistItems(
        item.id,
        item.name,
        item.description,
        item.image,
        item.price
    ))
    var isShowBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                isShowBottomSheet = true
                scope.launch {
                    sheetState.show()
                }
            }
            //.padding(4.dp)
            .background(
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = gray01,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
//                    .clickable {
//                        onAddToCart(CartItem(item, 1))
//                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = item.name,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .size(64.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = item.name,
                    //style = if (isClicked) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(textDecoration = TextDecoration.None)
                    style = MaterialTheme.typography.bodyLarge,
                    //style = if (isaddToCart) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(textDecoration = TextDecoration.None)
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Ksh ${item.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = green99
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
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
                        //isFavorite = !isFavorite

                        // Show a toast
                        Toast
                            .makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT)
                            .show()
                    }
//                    .clickable {
//                        viewModel.addToCart(item)
//                        Toast
//                            .makeText(
//                                context,
//                                "${item.name} added to cart successfully",
//                                Toast.LENGTH_SHORT
//                            )
//                            .show()
//                    }
                    .size(50.dp)
                    .background(
                        color = gray01,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),

                    painter = painterResource(id = if (isItemInWishlist) R.drawable.heart_icon else R.drawable.heart_icon_fave),
                    //imageVector = painterResource(id = R.drawable.cart_icon),
                    contentDescription = "",
                    tint = if (isItemInWishlist) Color.Red else Color.Gray
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))


    if (isShowBottomSheet) {
        ModalBottomSheet(
            containerColor = gray01,
            onDismissRequest = {
                isShowBottomSheet = false
                               },
            sheetState = sheetState,
            content = {
                ItemDetailsScreen(
                    categoryViewModel = CategoryViewModel(),
                    itemId = item.id,
                    navController = navController,
                    onClick = {
                        isShowBottomSheet = true
                        scope.launch {
                            sheetState.hide()
                            //navController.navigate(Route.Home.ItemDetails)
                        }
                    }
                )
            }
        )
    }
}


@Composable
private fun Boyy(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "Call our team!",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        Box (
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
                .background(
                    color = orange01,

                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ){
            IconButton(
                modifier = Modifier
                    .size(100.dp),
                onClick = onClick

            ) {
                Image(
                    painter = painterResource(id = R.drawable.call_icon),
                    contentDescription = "Call",
                    colorFilter = ColorFilter.tint(
                        color = Color.Blue
                    ),
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}
