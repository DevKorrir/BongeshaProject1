package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaSearchBar
import dev.korryr.bongesha.commons.BottomNavigationItem
import dev.korryr.bongesha.commons.Category
import dev.korryr.bongesha.commons.ItemRow
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.NotificationViewModel
import dev.korryr.bongesha.viewmodels.SearchViewModel
import dev.korryr.bongesha.viewmodels.WishlistViewModel

@Composable
fun BongaCategory(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    categoryViewModel: CategoryViewModel = viewModel(),
    navController: NavController,
    notificationViewModel: NotificationViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    //onClick: (Int) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val categories by categoryViewModel.categories.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val wishlistViewModel = viewModel<WishlistViewModel>()
    var selectedCategory by remember { mutableStateOf<Category?>(categories.firstOrNull()) }
    val context = LocalContext.current

    // State variables to track icon clicks
    var isHomeClicked by remember { mutableStateOf(true) }
    var isCartClicked by remember { mutableStateOf(false) }
    var isFavoriteClicked by remember { mutableStateOf(false) }
    var isProfileClicked by remember { mutableStateOf(false) }
    var isOrderClicked by remember { mutableStateOf(false) }
    var isChatsClicked by remember { mutableStateOf(false) }
    val unreadCount by notificationViewModel.unreadCount.collectAsState()
    var showNotifications by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .padding(
                top = 8.dp,
                start = 12.dp,
                end = 12.dp,
                bottom = 4.dp
            )
            //.background(color = Color.DarkGray)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "",
                    tint = orange28
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Bongesha",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.W700,
                    color = green99
                )

                Spacer(modifier = Modifier.weight(1f))

//Notification button
                Box(
                    modifier = Modifier
                        .clickable {
                            //navController.navigate(Route.Home.Notification)
                        }
                        .background(
                            color = Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            showNotifications = !showNotifications
                        }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                        ){
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = orange28
                            )
                            if (unreadCount > 1) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(
                                            orange28,
                                            shape = CircleShape
                                        )
                                        .align(
                                            Alignment.TopEnd
                                        )
                                ) {
                                    Text(
                                        text = unreadCount.toString(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
                if (showNotifications) {
                     NotificationList(notificationViewModel = notificationViewModel)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            var active by remember { mutableStateOf(false) }
            var query by remember { mutableStateOf("") }
            val searchResults by viewModel.searchResults.collectAsState()

            Row (
                modifier = Modifier
                    //.height(40.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                BongaSearchBar(
                    query = query,
                    onQueryChange = { newQuery ->
                        query = newQuery
                        viewModel.searchItems(newQuery)
                                    },
                    onSearchClick = {
                        viewModel.searchItems(query)
                        active = true
                    },
                    active = active,
                    onActiveChange = { newActive ->
                        active = newActive
                    },
                    searchResults = searchResults,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category",
                fontSize = 36.sp,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    //Beginning of the category ui boxes


                    categories.forEach { category ->
                        Box(
                            modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (selectedCategory == category) orange28 else Color.White,
                                    shape = RoundedCornerShape(12.dp)

                                )
                                .background(
                                    if (selectedCategory == category) orange28 else Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                                .clickable {
                                    selectedCategory = category
                                },
                            contentAlignment = Alignment.Center
                        ){

                            Column(
                                modifier = Modifier
                                    .padding(4.dp)
//                                    .clickable {
//                                        selectedCategory =
//                                            if (selectedCategory == category) null else category
//                                    }
                                    .background(
                                        if (selectedCategory == category) orange28 else Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(
                                            RoundedCornerShape(12.dp)
                                        )
                                        .background(
                                            if (selectedCategory == category) Color.White else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(id = category.icon),
                                        contentDescription = category.name,
                                        modifier = Modifier
                                            .size(64.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = category.name)
                            }

                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))


            //beginning of item row ui

            selectedCategory?.let { category ->
                Column(
                    modifier = Modifier
                        .padding(bottom = 60.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = category.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W700
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    category.items.forEach { item ->
                        ItemRow(
                            modifier = Modifier,
                            item = item,
                            viewModel = CartViewModel(),
                            navController = navController,
                            wishlistViewModel = viewModel(),
                            onAddToCart = {
                                cartViewModel.addToCart(item)
                            },
//                            onRemoveFromCart = {
//                                viewModel.removeFromCart(item)
//
//                            }
                        )
                    }
                }
            }
        }

///////////////////////////////////////////////////////////////////////////
// Beginning of bottom navigation bar
///////////////////////////////////////////////////////////////////////////

        BottomAppBar(
            containerColor = Color.White,
            modifier = Modifier
                .height(65.dp)
                .clip(
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            content = {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ){

                    BottomNavigationItem(//home icon
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = if (isHomeClicked) Color.Black else Color.Gray
                            )
                        },
                        label = "Home",
                        isSelected = currentScreen == Screen.Home,
                        onClick = {
                            onScreenSelected(Screen.Home)
                        }
                    )

                    //cart icon

                    BottomNavigationItem(
                        icon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        Color.Transparent,
                                        shape = CircleShape
                                    )
                            ){
                            Image(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.checkout_icon),
                                contentDescription = "Cart",
                                colorFilter = ColorFilter.tint(if (isCartClicked) Color.Black else Color.Gray)
                            )
                            if (cartItems.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(
                                            orange28,
                                            shape = CircleShape
                                        )
                                        .align(
                                            Alignment.TopEnd
                                        )
                                ) {
                                    Text(
                                        text = cartItems.size.toString(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }}
                        },
                        label ="Cart",
                        isSelected = currentScreen == Screen.Cart,
                        onClick = {
                            onScreenSelected(Screen.Cart)
                            navController.navigate(Route.Home.Cart)
                        }
                    )


                    //wishlist icon
                    BottomNavigationItem(
                        icon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(
                                        Color.Transparent,
                                        shape = CircleShape
                                    )
                            ){
                                Image(
                                    modifier = Modifier
                                        .size(24.dp),
                                    painter = painterResource(id = R.drawable.shopping_wishlish),
                                    contentDescription = "wishlist",
                                    colorFilter = ColorFilter.tint(if (isFavoriteClicked) Color.Black else Color.Gray)
                                )

                                val wishlistCount = wishlistViewModel.wishlistItems.collectAsState()
                                if (wishlistCount.value.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .background(
                                                orange28,
                                                shape = CircleShape
                                            )
                                            .align(
                                                Alignment.TopEnd
                                            ),
                                        contentAlignment = Alignment.Center

                                    ){
                                        Text(
                                            text = wishlistCount.value.size.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.align(Alignment.Center)
                                            )
                                    }
                                }
                            }
                        },
                        label = "WishList",
                        isSelected = currentScreen == Screen.Favourite,
                        onClick = {
                            navController.navigate(Route.Home.Wishlist)
                            onScreenSelected(Screen.Favourite)
                        }
                    )
                    BottomNavigationItem(
                        icon = {
                            Image(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.order),
                                contentDescription = "orders",
                                colorFilter = ColorFilter.tint(if (isOrderClicked) Color.Black else Color.Gray)
                            )
                        },
                        label ="Orders",
                        isSelected = currentScreen == Screen.Order,
                        onClick = {
                            onScreenSelected(Screen.Order)
                            navController.navigate(Route.Home.Order)
                        }
                    )
                    //profile icon
                    BottomNavigationItem(
                        icon = {
                            Image(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.information),
                                contentDescription = "Profile",
                                colorFilter = ColorFilter.tint(if (isProfileClicked) Color.Black else Color.Gray)
                            )
                        },
                        label ="Profile",
                        isSelected = currentScreen == Screen.Profile,
                        onClick = {
                            onScreenSelected(Screen.Profile)
                            navController.navigate(Route.Home.Profile)
                        }
                    )
                }
            }
        )
    }
}

enum class Screen {
    Home,
    Cart,
    Favourite,
    Order,
    Profile,

}




