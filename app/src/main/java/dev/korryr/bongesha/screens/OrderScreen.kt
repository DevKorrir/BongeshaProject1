package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import dev.korryr.bongesha.commons.Item
import dev.korryr.bongesha.viewmodels.CategoryViewModel


@Composable
fun OrdersScreeny(

){

}

@Composable
fun OrdersScreen(
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val categories by categoryViewModel.categories.collectAsState(emptyList())

    Column {
        Row {
            Text(text = "nothing yet")
        }
    }
}

@Composable
fun ItemCard(item: Item) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp)
    ) {
        Image(
            painter = rememberImagePainter(item.imageUrl),
            contentDescription = item.name,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = item.name,
            //style = FontStyle.Italic,
            modifier = Modifier.padding(4.dp)
        )
    }
}
