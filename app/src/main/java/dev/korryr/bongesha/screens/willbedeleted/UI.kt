package dev.korryr.bongesha.screens.willbedeleted

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel
) {
    val products = viewModel.products
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    // LazyColumn to display products
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LazyColumn {
            items(products) { product ->
                ProductItem(product)
            }

            // Handle pagination or load more on scroll
            item {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                } else {
                    Button(
                        onClick = { viewModel.fetchProducts(paginate = true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Refresh")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Category: ${product.category}")
        Text(text = "Price: Ksh${product.price}")
        Text(text = "Sizes: ${product.sizes.joinToString(", ")}")

        if (product.images.isNotEmpty()) {
            // Display the first image (you can also build an image slider here)
            Image(
                painter = rememberImagePainter(data = product.images.first()),
                contentDescription = null,
                modifier = Modifier.size(120.dp).padding(top = 8.dp)
            )
        }
    }
}
