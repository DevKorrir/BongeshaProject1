package dev.korryr.bongesha.commons

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.korryr.bongesha.screens.NotificationList
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.Category

// Helper Composable for AppBar
@Composable
fun TopBar(showNotifications: Boolean, unreadCount: Int, onNotificationClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = "", tint = orange28)
        Text(
            text = "Bongesha",
            fontSize = 36.sp,
            fontWeight = FontWeight.W700,
            color = green99
        )
        Box(
            modifier = Modifier.clickable { onNotificationClick() },
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onNotificationClick) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications", tint = orange28)
                if (unreadCount > 1) {
                    BadgeBox(unreadCount)
                }
            }
        }
        if (showNotifications) {
            NotificationList(notificationViewModel = viewModel())
        }
    }
}

// Helper Composable for Badge Box
@Composable
fun BadgeBox(unreadCount: Int) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .background(orange28, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = unreadCount.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

// Helper Composable for Category Box
@Composable
fun CategoryBox(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) orange28 else Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .background(
                    if (isSelected) orange28 else Color.White,
                    shape = RoundedCornerShape(12.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = category.imageResId),
                contentDescription = category.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.name,
                color = if (isSelected) Color.White else Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}

// Bottom App Bar
@Composable
fun BottomAppBart(navController: NavController, cartItemsCount: Int) {
    // Define content for the Bottom App Bar
    BottomAppBar(
        containerColor = Color.White,
        modifier = Modifier
            .height(65.dp)
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        // Bottom navigation items, e.g., Home, Cart, Wishlist, etc.
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.Gray) },
            label = "Home",
            isSelected = false,
            onClick = {}
        )
        // Add Cart Icon with badge for cart item count
        BottomNavigationItem(
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemsCount > 0) {
                            Badge(containerColor = Color.Red, contentColor = Color.White) {
                                Text(text = cartItemsCount.toString())
                            }
                        }
                    }
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.checkout_icon),
                        contentDescription = "Cart",
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }
            },
            label = "Cart",
            isSelected = false,
            onClick = {
                navController.navigate(Route.Home.Cart)
            }
        )
        // Continue adding other items like Wishlist, Orders, Profile, etc.
    }
}