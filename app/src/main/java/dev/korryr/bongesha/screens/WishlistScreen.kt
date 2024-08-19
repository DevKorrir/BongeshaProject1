import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.korryr.bongesha.commons.Item
import dev.korryr.bongesha.commons.WishlistItems
import dev.korryr.bongesha.viewmodels.WishlistViewModel

@Composable
fun WishlistScreen(
    wishlistViewModel: WishlistViewModel = viewModel()
) {
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Wishlist",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(wishlistItems) { item ->
                WishlistItemRow(
                    wishlistItems = item,
                    onRemoveClicked = {
                        wishlistViewModel.removeItemFromWishlist(item.id)
                    })
            }
        }
    }
}

@Composable
fun WishlistItemRow(
    wishlistItems: WishlistItems,
    onRemoveClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = wishlistItems.name)
            Text(text = wishlistItems.description)
            Text(text = "Ksh. ${wishlistItems.price}")
        }
        Image(
            painter = painterResource(id = wishlistItems.image),
            contentDescription = wishlistItems.name,
            modifier = Modifier.size(64.dp)
        )
        Button(
            onClick = {
                onRemoveClicked()
            }) {
            Text(text = "Remove")
        }
    }
}
