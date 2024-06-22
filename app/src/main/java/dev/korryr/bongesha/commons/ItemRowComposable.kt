package dev.korryr.bongesha.commons

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green07
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.viewmodels.CartItemViewModel


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
                        Toast
                            .makeText(
                                context,
                                "${item.name} added to cart successfully",
                                Toast.LENGTH_SHORT
                            )
                            .show()
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