package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.Product

@Composable
fun AllProductsScreen(
    navController: NavController,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val productStates by categoryViewModel.products.collectAsState()
    val products = productStates.map { it.value }

    val isLoading by categoryViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        categoryViewModel.fetchProductsForCategory(
            "Audio & Sound Systems"
        )
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = gray01)
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "Popular Items",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        item {
            ProductGrid(products = products, navController = navController)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "You May Like",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        item {
            ProductGrid(products = products, navController = navController)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Special Offers",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        item {
            ProductGrid(products = products, navController = navController)
        }
    }
}


@Composable
fun ProductGrid(
    products: List<Product>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(500.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product = product, navController = navController)
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ){
            val imagePainter = rememberAsyncImagePainter(model = product.images.firstOrNull() ?: "")
            Image(
                painter =  imagePainter,
                contentDescription = product.name,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            // Show loading indicator if the image is in loading state
            if (imagePainter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    color = Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Ksh ${product.price}",
            color = green99,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}