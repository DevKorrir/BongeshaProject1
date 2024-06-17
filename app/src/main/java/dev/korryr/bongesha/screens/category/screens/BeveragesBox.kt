package dev.korryr.bongesha.screens.category.screens

import android.icu.util.ULocale
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.CategoryField

@Composable
fun Beverages(
    navController: NavController
){
    val categories = listOf<ULocale.Category>(/* Populate your categories here */)
    var selectedCategory by remember { mutableStateOf<ULocale.Category?>(null) }

    Column (
        modifier = Modifier.padding(8.dp)
    ){
        CategoryField(
            name = "Dark Apple token 250dp",
            text = "Ksh 8",
            painter = painterResource(id = R.drawable.apple_icon)
        )
        CategoryField(
            name = "Dark Apple pro",
            text = "Ksh 400",
            painter = painterResource(id = R.drawable.apple_icon)
        )
        CategoryField(
            name = "Android java",
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            text = "Ksh 500"
        )
        CategoryField(
            name = "Table Java",
            painter = painterResource(id = R.drawable.working_sticker),
            text = "Ksh 120"
        )
    }
}

/*data class CategoryItem(
    val title: String,
    val description: String
)

 */
