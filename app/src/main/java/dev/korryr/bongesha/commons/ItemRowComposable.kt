package dev.korryr.bongesha.commons

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import dev.korryr.bongesha.screens.ItemDetailsScreen
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.viewmodels.CartItem
import dev.korryr.bongesha.viewmodels.Product
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ItemRow(
    //product: Product,
    product: Product,
    navController: NavController,
    ) {
    var isFavorite by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                showBottomSheet = true
                scope.launch {
                    sheetState.show()
                }
            }
            //.padding(4.dp)
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = gray01,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                val imagePainter =
                    rememberAsyncImagePainter(model = product.images.firstOrNull() ?: "")
                Image(
                    painter = imagePainter,
                    contentDescription = product.name,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .size(64.dp),
                    contentScale = ContentScale.FillBounds
                )
                // Show loading indicator if the image is in loading state
                if (imagePainter.state is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                product.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Text(
                    text = "Ksh ${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = green99
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    Spacer(modifier = Modifier.height(12.dp))


    if (showBottomSheet) {
        ModalBottomSheet(
            dragHandle = {
                //Icon(Icons.Default.toString(), contentDescription = null)
            },
            containerColor = gray01,
            onDismissRequest = {
                showBottomSheet = false
                               },
            sheetState = sheetState,
        ){
            Column (
                modifier = Modifier
                    .heightIn(max = 650.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    ElevatedButton(
                        modifier = Modifier,
                        onClick = {
                            showBottomSheet = false
                        },
                        colors = ButtonColors(
                            containerColor = gray01,
                            contentColor = Color.Gray,
                            disabledContainerColor = gray01,
                            disabledContentColor = Color.Gray
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            tint = Color.Red,
                            imageVector = Icons.Default.Close,
                            contentDescription = ""
                        )
                    }

                }
                ItemDetailsScreen(
                    onClick = { //isShowBottomSheet = true
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    },
                    product = product,
                    cartItem = CartItem()
                )
            }
        }
    }
}

