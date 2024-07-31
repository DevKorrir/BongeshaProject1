package dev.korryr.bongesha.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.ui.theme.orange28

@Composable
fun BottomNavigationItem(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    painter: Painter? = null,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color = Color.Black, // Replace ani tyme
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
            .clickable(onClick = onClick),
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            //.padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        //Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) selectedColor else unselectedColor
        )
    }
}}

@Composable
fun BottomNavigationBar() {
    var isHomeClicked by remember { mutableStateOf(false) }

    BottomAppBar(
        content = {
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = if (isHomeClicked) Color(0xFFFFA500) else Color.Black // Replace with your desired color (e.g., orange28)
                    )
                },
                label = "Home",
                isSelected = isHomeClicked,
                onClick = { isHomeClicked = !isHomeClicked }
            )
            // Add more BottomNavigationItem here if needed

            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = if (isHomeClicked) Color(0xFFFFA500) else Color.Black // Replace with your desired color (e.g., orange28)
                    )
                },
                label = "cart",
                isSelected = isHomeClicked,
                onClick = {
                    isHomeClicked = !isHomeClicked
                }
            )
        }
    )
}