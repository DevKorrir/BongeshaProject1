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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

    val categories = listOf(
        //Category("Electronics", R.drawable.heart_icon),
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
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = categoryViewModel.isLoading)

    LaunchedEffect(selectedCategory) {
        if (selectedCategory.isNotEmpty()) {
            categoryViewModel.fetchProductsForCategory(selectedCategory)
        } else {
            println("Selected category is empty.")
        }
    }

    LaunchedEffect(Unit) {
        categoryViewModel.fetchProductsForCategory(selectedCategory)
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            categoryViewModel.fetchProductsForCategory(
                selectedCategory
            )
        }
    ) {
        Column (
            modifier = Modifier
                .padding(8.dp)
        ){
        var textOffset by remember { mutableFloatStateOf(0f) }
        val animatedOffset by animateFloatAsState(
            targetValue = textOffset,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        LaunchedEffect(Unit) {
            textOffset =
                -100f  // Adjust this value based on the width of the text box
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
            //Beginning of the category ui boxes
            items(categories) { category ->
                // Display each category as a button
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Transparent,

                            //if (selectedCategory == category) orange28 else Color.White,
                            shape = RoundedCornerShape(12.dp)

                        )
                        .background(
                            if (selectedCategory == category) orange28 else Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                        .clickable {
                            selectedCategory = category
                            categoryViewModel.fetchProductsForCategory(
                                category
                            )
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
                                .clip(
                                    RoundedCornerShape(12.dp)
                                )
                                .background(
                                    if (selectedCategory == category) Color.White else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baby),
                                contentDescription = category,
                                modifier = Modifier
                                    .size(64.dp),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        // Display the category name
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

        // Display Products or Loading Spinner
        if (categoryViewModel.isLoading) {
            CircularProgressIndicator(
                //modifier = Modifier.align(Alignment.CenterHorizontally),
                color = orange28
            )
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
                    modifier = Modifier
                        .padding(bottom = 60.dp)
                    //.verticalScroll(rememberScrollState())
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

@Composable
fun Categoryexample() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = blue88
            )
    ){

            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(8.dp))

            Column {

            }

    }
}