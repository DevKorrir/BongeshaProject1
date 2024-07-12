package dev.korryr.bongesha.screens.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
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
import dev.korryr.bongesha.commons.BadgedBox
import dev.korryr.bongesha.commons.BongaSearchBar
import dev.korryr.bongesha.commons.Category
import dev.korryr.bongesha.commons.ItemRow
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.screens.NotificationList
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange01
import dev.korryr.bongesha.ui.theme.orange07
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.BongaCategoryViewModel
import dev.korryr.bongesha.viewmodels.NotificationViewModel

@Composable
fun BongaCategory(
    bongaCategoryViewModel: BongaCategoryViewModel = viewModel(),
    navController: NavController,
    notificationViewModel: NotificationViewModel = viewModel(),
    onClick: () -> Unit
) {
    val categories by bongaCategoryViewModel.categories.collectAsState()
    val cartItems by bongaCategoryViewModel.cart.collectAsState()
    var selectedCategory by remember { mutableStateOf<Category?>(categories.firstOrNull()) }
    val context = LocalContext.current

    // State variables to track icon clicks
    var isHomeClicked by remember { mutableStateOf(false) }
    val isCartClicked by remember { mutableStateOf(false) }
    var isFavoriteClicked by remember { mutableStateOf(false) }
    var isProfileClicked by remember { mutableStateOf(false) }
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
            Row {
                BongaSearchBar(
                    query = query,
                    onQueryChange = { newQuery -> query = newQuery },
                    onSearchClick = {},
                    active = active,
                    onActiveChange = { newActive -> active = newActive }
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
                    categories.forEach { category ->
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    selectedCategory =
                                        if (selectedCategory == category) null else category
                                }
                                .background(
                                    if (selectedCategory == category) orange28 else Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = category.icon),
                                contentDescription = category.name,
                                modifier = Modifier.size(64.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = category.name)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

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
                            item = item,
                            viewModel = bongaCategoryViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }

        //Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),
            //contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = orange07,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { isHomeClicked = !isHomeClicked }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "",
                            tint = if (isHomeClicked) orange28 else Color.Black
                        )
                    }
                    //Text(
                        //text = "Home",
                        //color = if (isHomeClicked) orange28 else Color.Black
                    //)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Route.Home.Cart)
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge { Text("${cartItems.size}") }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = if (isCartClicked) orange28 else Color.Black
                            )
                        }
                    }
                    if (isCartClicked) {
                        Text("Cart Content", color = orange28)
                    }
                    //Text(
                        //text = "Cart",
                        //color = if (isCartClicked) orange28 else Color.Black
                    //)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { isFavoriteClicked = !isFavoriteClicked }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "",
                            tint = if (isFavoriteClicked) orange28 else Color.Black
                        )
                    }
                    //Text(
                        //text = "Favourite",
                        //color = if (isFavoriteClicked) orange28 else Color.Black
                    //)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Route.Home.Inbox)
                            isChatsClicked = !isChatsClicked }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "",
                            tint = if (isChatsClicked) orange28 else Color.Black
                        )
                    }
                    //Text(
                        //text = "Inbox",
                       // color = if (isChatsClicked) orange28 else Color.Black
                    //)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Route.Home.Profile)
                            isProfileClicked = !isProfileClicked
                        }
                    ) {
                        Image(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(id = R.drawable.information) ,
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(if (isProfileClicked) orange28 else Color.Black)
                        )
                    }
                    //Text(
                        //text = "Profile",
                        //color = if (isProfileClicked) orange28 else Color.Black
                    //)
                }
            }
        }
    }
}


