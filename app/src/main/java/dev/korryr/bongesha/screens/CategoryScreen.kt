package dev.korryr.bongesha.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.ItemRow
import dev.korryr.bongesha.ui.theme.blue88
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.NotificationViewModel

@Composable
fun CategoryTab(
    categoryViewModel: CategoryViewModel,
    navController: NavController,
    notificationViewModel: NotificationViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
) {
    val products by categoryViewModel.products.collectAsState()
    val isLoading by categoryViewModel.isLoading.collectAsState()

    val categories = listOf(
        "Audio & Sound Systems",
        "Phones & Accessories",
        "Computers & Accessories",
        "Home Appliances",
        "Lighting & Electrical",
        "Televisions & Accessories",
        "Portable Electronics",
        "Mobile Network Accessories",
        "Cables & Connectors",
        "Office & Stationery Electronics",
        "Smart Home & Security",
        "Gaming & Entertainment",
        "Energy & Power Solutions"
    )
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    LaunchedEffect(selectedCategory) {
        if (selectedCategory.isNotEmpty()) {
            categoryViewModel.fetchProductsForCategory(selectedCategory)
        } else {
            println("Selected category is empty.")
        }
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            categoryViewModel.fetchProductsForCategory(selectedCategory)
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            var textOffset by remember { mutableFloatStateOf(0f) }
            val animatedOffset by animateFloatAsState(
                targetValue = textOffset,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            LaunchedEffect(Unit) {
                textOffset = -100f
            }

            Text(
                text = "Categories",
                fontSize = 36.sp,
                fontWeight = FontWeight.W700
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
            ) {
                items(categories) { category ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                if (selectedCategory == category) orange28 else Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
                            .clickable {
                                selectedCategory = category
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    if (selectedCategory == category) orange28 else Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selectedCategory == category) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.baby),
                                    contentDescription = category,
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = category,
                                color = if (selectedCategory == category) Color.White else Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .offset(x = animatedOffset.dp)
                                    .width(90.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                CircularProgressIndicator(color = orange28)
            } else {
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pull down to refresh",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 60.dp)
                    ) {
                        item {
                            Text(
                                text = selectedCategory,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W700,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(products) { product ->
                            ItemRow(
                                product = product,
                                navController = navController
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
