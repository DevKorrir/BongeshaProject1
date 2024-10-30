package dev.korryr.bongesha.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BottomNavigationItem
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel

enum class BottomTab { Home, Cart, Wishlist, Order,  Profile }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BongaHome(
    categoryViewModel: CategoryViewModel,
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
) {
    var selectedTab by remember { mutableStateOf(BottomTab.Home) }
    var itemCount by remember { mutableIntStateOf(0) }
    val cartItemsCount = cartViewModel.quantityCount

    Scaffold(
        containerColor = gray01,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(
                        color = orange28,
                        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                    )
                    .height(50.dp),
                title = {
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

                        //notify
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
                                                text = "",
                                                color = Color.White,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }

                                }
                            }
                        }
                    }
                }
            )
        },

        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                modifier = Modifier
                    .height(65.dp)
                    .clip(
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth(),
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    BottomNavigationItem(//home icon
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = if (selectedTab == BottomTab.Home) orange28 else Color.Gray

                            )
                        },
                        label = "Home",
                        isSelected = selectedTab == BottomTab.Home,
                        onClick = { selectedTab = BottomTab.Home }
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
                                            containerColor = orange28,
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
                                    colorFilter = ColorFilter.tint(if (selectedTab == BottomTab.Cart) orange28 else Color.Gray)
                                )
                            }
                        },
                        label = "Cart",
                        isSelected = selectedTab == BottomTab.Cart,
                        onClick = { selectedTab = BottomTab.Cart }
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
                                    colorFilter = ColorFilter.tint(if (selectedTab == BottomTab.Wishlist) orange28 else Color.Gray)
                                )
                            }
                        },
                        label = "Wishlist",
                        isSelected = selectedTab == BottomTab.Wishlist,
                        onClick = { selectedTab = BottomTab.Wishlist }
                    )

                    //orders icon
                    BottomNavigationItem(
                        badgeCount = cartItemsCount,
                        icon = {
                            Image(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.order),
                                contentDescription = "orders",
                                colorFilter = ColorFilter.tint(if (selectedTab == BottomTab.Order) orange28 else Color.Gray)
                            )
                        },
                        label = "Orders",
                        isSelected = selectedTab == BottomTab.Order,
                        onClick = {
                            navController.navigate(Route.Home.ORDER)
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
                                colorFilter = ColorFilter.tint(if (selectedTab == BottomTab.Profile) orange28 else Color.Gray)
                            )
                        },
                        label = "Profile",
                        onClick = {
                            selectedTab = BottomTab.Profile
                            //navController.navigate(Route.Home.Profile)
                        },
                        isSelected = selectedTab == BottomTab.Profile
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomTab.Home ->
                    CategoryTab(
                        categoryViewModel = categoryViewModel,
                        navController = navController
                    )

                BottomTab.Cart ->
                    CartScreen(navController)

                BottomTab.Wishlist -> Text("Wishlist Content")

                BottomTab.Order -> Text("Order Content")

                BottomTab.Profile ->
                        UserProfile(
                            navController,
                            onSignOut = {}
                        )


                    //Text("Profile Content")
            }
        }
    }
}
//
//
//    Box(
//        modifier = Modifier
//            .padding(
//                top = 8.dp,
//                start = 12.dp,
//                end = 12.dp,
//                bottom = 4.dp
//            )
//            //.background(color = Color.DarkGray)
//            .fillMaxSize()
//    ) {




//
//        Column(
//            modifier = Modifier
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Menu,
//                    contentDescription = "",
//                    tint = orange28
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = "Bongesha",
//                    fontSize = 36.sp,
//                    fontWeight = FontWeight.W700,
//                    color = green99
//                )
//
//                Spacer(modifier = Modifier.weight(1f))
//
////Notification button
//                Box(
//                    modifier = Modifier
//                        .clickable {
//                            //navController.navigate(Route.Home.Notification)
//                        }
//                        .background(
//                            color = Color.Transparent,
//                            shape = CircleShape
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    IconButton(
//                        onClick = {
//                            showNotifications = !showNotifications
//                        }
//                    ) {
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .size(32.dp)
//                        ){
//                            Icon(
//                                imageVector = Icons.Default.Notifications,
//                                contentDescription = "Notifications",
//                                tint = orange28
//                            )
//                            if (unreadCount > 1) {
//                                Box(
//                                    modifier = Modifier
//                                        .size(14.dp)
//                                        .background(
//                                            orange28,
//                                            shape = CircleShape
//                                        )
//                                        .align(
//                                            Alignment.TopEnd
//                                        )
//                                ) {
//                                    Text(
//                                        text = unreadCount.toString(),
//                                        color = Color.White,
//                                        style = MaterialTheme.typography.bodySmall,
//                                        modifier = Modifier.align(Alignment.Center)
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//                if (showNotifications) {
//                     NotificationList(notificationViewModel = notificationViewModel)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            var active by remember { mutableStateOf(false) }
//            var query by remember { mutableStateOf("") }
//
//            Row (
//                modifier = Modifier
//                    //.height(40.dp)
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ){
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





///////////////////////////////////////////////////////////////////////////
// Beginning of bottom navigation bar
///////////////////////////////////////////////////////////////////////////


//            }
//        )
//    }
//}







