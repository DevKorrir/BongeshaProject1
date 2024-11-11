import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import dev.korryr.bongesha.viewmodels.Product
import dev.korryr.bongesha.viewmodels.WishlistItems
import dev.korryr.bongesha.viewmodels.WishlistViewModel

@Composable
fun WishlistScreen(
    product: Product,
    wishlistViewModel: WishlistViewModel = viewModel()
) {
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = " Your Wishlist",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (wishlistItems.isEmpty()) {
            // Show a message if the wishlist is empty
            Text(
                text = "Your wishlist is empty.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            // Show the list of items in the wishlist
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(wishlistItems) { product ->
                    WishlistItemRow(
                        wishlistItems = product,
                        onRemoveClicked = {
                            wishlistViewModel.removeItemFromWishlist(product.id)
                        }
                    )
                }
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
            //product.description?.let { Text(text = it) }
            Text(text = "Ksh. ${wishlistItems.price}")
        }

        Image(
            painter = rememberImagePainter(data = wishlistItems.imageUrl),
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
