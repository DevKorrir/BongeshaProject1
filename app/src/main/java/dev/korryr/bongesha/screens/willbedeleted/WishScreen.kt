//package dev.korryr.bongesha.screens.willbedeleted
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
////noinspection UsingMaterialAndMaterial3Libraries
//import androidx.compose.material.*
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.rememberImagePainter
//
//@Composable
//fun WishlistScreen(
//    wishlistItems: List<WishlistItems>,
//    onRemoveFromWishlist: (WishlistItems) -> Unit,
//    onContinueShopping: () -> Unit,
//    onProceedToCheckout: () -> Unit
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Your Wishlist") },
//                backgroundColor = MaterialTheme.colors.primary,
//                contentColor = Color.White
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(Color.White)
//        ) {
//            if (wishlistItems.isEmpty()) {
//                // Empty wishlist message
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "Your wishlist is empty!",
//                        fontSize = 18.sp,
//                        color = Color.Gray
//                    )
//                }
//            } else {
//                // Wishlist items
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(wishlistItems) { item ->
//                        WishlistItemCard(item, onRemoveFromWishlist)
//                    }
//                }
//
//                // Buttons at the bottom
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(
//                        onClick = onContinueShopping,
//                        modifier = Modifier.weight(1f).padding(end = 8.dp),
//                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
//                    ) {
//                        Text("Continue Shopping")
//                    }
//
//                    Button(
//                        onClick = onProceedToCheckout,
//                        modifier = Modifier.weight(1f).padding(start = 8.dp),
//                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
//                    ) {
//                        Text("Proceed to Checkout")
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun WishlistItemCard(
//    item: WishlistItems,
//    onRemoveFromWishlist: (WishlistItems) -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = 4.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Product Image
//            Image(
//                painter = rememberImagePainter(data = item.productImage),
//                contentDescription = item.productName,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(64.dp)
//                    .background(Color.LightGray)
//                    .padding(4.dp)
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            // Product Details
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = item.productName,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//                Text(
//                    text = "Price: $${item.productPrice}",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//            }
//
//            // Remove Button
//            IconButton(onClick = { onRemoveFromWishlist(item) }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_delete),
//                    contentDescription = "Remove from wishlist",
//                    tint = Color.Red
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun PreviewWishlistScreen() {
//    // Sample wishlist items for preview
//    val sampleWishlistItems = listOf(
//        WishlistItems("1", "Smartphone", "https://example.com/product-image1.jpg", 799.99f),
//        WishlistItems("2", "Laptop", "https://example.com/product-image2.jpg", 1199.99f),
//        WishlistItems("3", "Headphones", "https://example.com/product-image3.jpg", 199.99f)
//    )
//
//    WishlistScreen(
//        wishlistItems = sampleWishlistItems,
//        onRemoveFromWishlist = {},
//        onContinueShopping = {},
//        onProceedToCheckout = {}
//    )
//}
