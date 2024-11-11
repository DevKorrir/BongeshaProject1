package dev.korryr.bongesha.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.korryr.bongesha.R
import dev.korryr.bongesha.screens.BottomTab
import dev.korryr.bongesha.ui.theme.orange28

@Composable
fun BottomNavigationItem(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    painter: Painter? = null,
    label: String,
    badgeCount: Int = 0,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color = orange28,
    unselectedColor: Color = Color.Gray
) {
    Box(
        modifier = Modifier
            .clip(
                shape = CircleShape
            )
            .background(
                if (isSelected) Color.Transparent else Color.White,
                shape = CircleShape
            )
            .border(
                1.dp,
                if (isSelected) Color.Transparent else Color.White,
                shape = CircleShape
            )
            .padding(8.dp)
            .clickable(onClick = onClick)
            .size(52.dp),
        contentAlignment = Alignment.Center
    ) {
        BadgedBox(
            badge = {
                // Only show the badge if cart has items
                if (badgeCount > 0) {
                    Badge(
                        containerColor = selectedColor,
                        contentColor = Color.White
                    ) {
                        Text(text = badgeCount.toString())
                    }
                }
            }
        ) {
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            ){
            icon()
            Text(
                text = label,
                fontSize = 11.sp,
                color = if (isSelected) selectedColor else unselectedColor
            )
        }
    }
}

