package dev.korryr.bongesha.screens.category

import BongaCategoryViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.korryr.bongesha.commons.BongaSearchBar
import dev.korryr.bongesha.commons.Item
import dev.korryr.bongesha.commons.Category
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green07
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartItemViewModel

@Composable
fun BongaCategory(
    cartItemViewModel: CartItemViewModel = viewModel(),
    bongaCategoryViewModel: BongaCategoryViewModel = viewModel(),
    navController: NavController,
    onClick: () -> Unit
) {
    val categories by bongaCategoryViewModel.categories.collectAsState()
    val cartItems by cartItemViewModel.cart.collectAsState()
    var selectedCategory by remember { mutableStateOf<Category?>(categories.firstOrNull()) }
    val context = LocalContext.current

    // State variables to track icon clicks
    var isHomeClicked by remember { mutableStateOf(false) }
    var isCartClicked by remember { mutableStateOf(false) }
    var isFavoriteClicked by remember { mutableStateOf(false) }
    var isProfileClicked by remember { mutableStateOf(false) }
    var isChatsClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "",
                tint = orange28
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "",
                tint = orange28
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        var active by remember { mutableStateOf(false) }
        var query by remember { mutableStateOf("") }
        Row(

        ){
            BongaSearchBar(
                query = query,
                onQueryChange = { newQuery -> query = newQuery },
                onSearchClick = {},
                active = active,
                onActiveChange = { newActive -> active = newActive }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Category",
            fontSize = 36.sp,
            fontWeight = FontWeight.W700
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                                selectedCategory = if (selectedCategory == category) null else category
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

        Spacer(modifier = Modifier.height(24.dp))

        Divider()

        Spacer(modifier = Modifier.height(12.dp))

        selectedCategory?.let { category ->
            Column(
                modifier = Modifier
                    //.padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = category.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700
                )
                Spacer(modifier = Modifier.height(24.dp))
                category.items.forEach { item ->
                    ItemRow(
                        item = item,
                        viewModel = cartItemViewModel,
                        navController = navController
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
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
                Text(
                    text = "Home",
                    color = if (isHomeClicked) orange28 else Color.Black
                )
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
                Text(
                    text = "Cart",
                    color = if (isCartClicked) orange28 else Color.Black
                )
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
                Text(
                    text = "Favourite",
                    color = if (isFavoriteClicked) orange28 else Color.Black
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { isChatsClicked = !isChatsClicked }
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "",
                        tint = if (isChatsClicked) orange28 else Color.Black
                    )
                }
                Text(
                    text = "Inbox",
                    color = if (isChatsClicked) orange28 else Color.Black
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { isProfileClicked = !isProfileClicked }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "",
                        tint = if (isProfileClicked) orange28 else Color.Black
                    )
                }
                Text(
                    text = "Profile",
                    color = if (isProfileClicked) orange28 else Color.Black
                )
            }
        }
    }
}

@Composable
fun ItemRow(
    item: Item,
    viewModel: CartItemViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(8.dp)
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = gray01,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp)
                    .clickable {
                        navController.navigate(Route.Home.ItemDetails)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = item.name,
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge
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
                    .clickable {
                        viewModel.addToCart(item)
                        Toast.makeText(context, "${item.name} added to cart successfully", Toast.LENGTH_SHORT).show()
                    }
                    .size(32.dp)
                    .background(
                        color = green07,
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = green99
                )
            }
        }
    }
}

@Composable
fun BadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box {
        content()
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
        ) {
            badge()
        }
    }
}