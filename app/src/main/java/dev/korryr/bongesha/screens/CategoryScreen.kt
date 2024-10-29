package dev.korryr.bongesha.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BottomNavigationItem
import dev.korryr.bongesha.commons.ItemRow
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.NotificationViewModel


//@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun BongaCategory(
    categoryViewModel: CategoryViewModel,
    navController: NavController,
    notificationViewModel: NotificationViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
) {
    // State variables to track icon clicks
    var isHomeClicked by remember { mutableStateOf(true) }
    var isCartClicked by remember { mutableStateOf(false) }
    var isFavoriteClicked by remember { mutableStateOf(false) }
    var isProfileClicked by remember { mutableStateOf(false) }
    var isOrderClicked by remember { mutableStateOf(false) }
    val unreadCount by notificationViewModel.unreadCount.collectAsState()
    var showNotifications by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(0) }
    val uiState by categoryViewModel.uiState.collectAsState()
    val products by categoryViewModel.products.collectAsState()
    //val products by remember { mutableStateOf(categoryViewModel.products) }
    val isLoading by remember { mutableStateOf(categoryViewModel.isLoading) }

    val cartItemsCount = cartViewModel.quantityCount

    val categories = listOf(
        //Category("Electronics", R.drawable.heart_icon),
        "Audio & Sound Systems",
        "Phones & Accessories",
        "Computers & Accessories",
        "Home Appliances",
        "Lighting & Electrical",
        "Televisions & Accessories",
        "Portable Electronics",
        "Mobile Network Accessories",
        "Cables & Connectors",
        "Office & Stationery Electronics",
        "Smart Home & Security",
        "Gaming & Entertainment",
        "Energy & Power Solutions"
    )
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory.isNotEmpty()) {
            categoryViewModel.fetchProductsForCategory(selectedCategory)
        } else {
            println("Selected category is empty.")
        }
    }


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

            Row (
                modifier = Modifier
                    //.height(40.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
//                BongaSearchBar(
//                    query = query,
//                    onQueryChange = { newQuery ->
//                        query = newQuery
//                        viewModel.searchItems(newQuery)
//                                    },
//                    onSearchClick = {
//                        viewModel.searchItems(query)
//                        active = true
//                    },
//                    active = active,
//                    onActiveChange = { newActive ->
//                        active = newActive
//                    },
//                    searchResults = searchResults,
//                )
            }

            //Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Categories",
                fontSize = 36.sp,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                var textOffset by remember { mutableFloatStateOf(0f) }
                val animatedOffset by animateFloatAsState(
                    targetValue = textOffset,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )

                LaunchedEffect(Unit) {
                    textOffset = -100f  // Adjust this value based on the width of the text box
                }


//                when (uiState) {
//                    is CategoryUiState.Loading -> CircularProgressIndicator()
//                    is CategoryUiState.Error -> Text((uiState as CategoryUiState.Error).message)
//                    is CategoryUiState.Success -> {
//                        val categories = (uiState as CategoryUiState.Success).categories
                        LazyRow(
                            modifier = Modifier
                        ) {
                            //Beginning of the category ui boxes
                            items(categories) { category ->
                                // Display each category as a button
                                Box(
                                    modifier = Modifier
                                        .clip(
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.Transparent,

                                            //if (selectedCategory == category) orange28 else Color.White,
                                            shape = RoundedCornerShape(12.dp)

                                        )
                                        .background(
                                            if (selectedCategory == category) orange28 else Color.White,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(8.dp)
                                        .clickable {
                                            categoryViewModel.fetchProductsForCategory(category)
                                            selectedCategory = category
                                        },
                                    contentAlignment = Alignment.Center
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .padding(4.dp)
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
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.baby),
                                                contentDescription = category,
                                                modifier = Modifier
                                                    .size(64.dp),
                                                contentScale = ContentScale.FillBounds
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        // Display the category name
                                        Text(
                                            text = category,
                                            color = if (selectedCategory == category) Color.White else Color.Black,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .offset(x = animatedOffset.dp)
                                                .width(90.dp),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 12.sp
                                        )
                                    }

                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

                // Display Products or Loading Spinner
                if (categoryViewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = orange28
                    )
                } else {
                    if (products.isEmpty()){
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Out Of Stock, Wait for Updates...",
                                fontSize = 18.sp,
                                color = Color.Gray
                            )
                        }
                    } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(bottom = 60.dp)
                            //.verticalScroll(rememberScrollState())
                    ) {
                        item {
                            Text(
                                text = selectedCategory,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W700,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(products) { product ->
                            ItemRow(
                                product = product,
                                navController = navController
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    }
                }


//            // Loading indicator
//            if (isLoading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.CenterHorizontally),
//                        color = orange28
//                    )
//            } else {
//
//            }
//            //beginning of itemrow ui
//            selectedCategory.let { category ->
//                Column(
//                    modifier = Modifier
//                        .padding(bottom = 60.dp)
//                        .verticalScroll(rememberScrollState())
//                ) {
//                    Text(
//                        text = category,
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.W700
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    //loop through items in the category
//                    items(products) { product ->
//                        ItemRow(
//                            product = product,
//                            navController = navController
//                        )
//                    }
//
//                }
//            }
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
                        isSelected = false,
                        onClick = {}
                    )

                    //cart icon
                        BottomNavigationItem(
                            icon = {
                                // Badge with item count
                                BadgedBox(
                                    badge = {
                                        // Only show the badge if cart has items
                                        if (cartItemsCount > 0) {
                                            Badge(
                                                containerColor = Color.Red,
                                                contentColor = Color.White
                                            ) {
                                                Text(text = cartItemsCount.toString())
                                            }
                                        }
                                    }
                                ) {
                                    // Cart Icon with conditional color based on whether the item is in the cart
                                    Image(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(id = R.drawable.checkout_icon),
                                        contentDescription = "Cart",
                                        colorFilter = ColorFilter.tint(if (isCartClicked) Color.Black else Color.Gray)
                                    )
                                }
                            },
                            label = "Cart",
                            isSelected = false,
                            onClick = {
                                isCartClicked = !isCartClicked
                                navController.navigate(Route.Home.Cart)
                            }
                        )


                    //wishlist icon
                    BottomNavigationItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (itemCount > 0) {
                                        Badge(
                                            containerColor = orange28,
                                            contentColor = Color.White
                                        ) {
                                            Text(text = itemCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(R.drawable.shopping_wishlish),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint( if (isFavoriteClicked) Color.Black else Color.Gray)
                                )
                            }
                        },
                        label = "Wishlist",
                        isSelected = true,
                        onClick = {
                            isFavoriteClicked = !isFavoriteClicked
                            navController.navigate(Route.Home.Wishlist)
                        }
                    )
//                        label = {
//                            Text("WishList")
//                        },
//                        selected = currentScreen == Screen.Favourite,
//                        onClick = {
//                            // Toggle the item in the cart
//                            cartViewModel.toggleItemInCart(item)
//
//                            // Navigate to Wishlist Screen if desired
//                            navController.navigate(Route.Home.Wishlist)
//                            onScreenSelected(Screen.Favourite)
//                        }
//                    )

                    //orders icon
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
                        isSelected = false,
                        onClick = {

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
                        onClick = {
                            navController.navigate(Route.Home.Profile)
                        },
                        isSelected = true
                    )
                }
            }
        )
    }
}


data class Categoryyyy(
    val category: String,
    val imageResId: Int
)





