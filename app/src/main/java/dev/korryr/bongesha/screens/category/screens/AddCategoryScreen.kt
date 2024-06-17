package dev.korryr.bongesha.screens.category.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class AddCategoryScreen {
}
@Composable
fun AddCategoryScreen(categoryViewModel: CategoryViewModel) {
    var newCategory by remember { mutableStateOf("") }

    Column {
        TextField(
            value = newCategory,
            onValueChange = { newCategory = it },
            label = { Text("New Category") }
        )
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            onClick = {
            if (newCategory.isNotEmpty()) {
                //categoryViewModel.addCategory(newCategory)
                newCategory = ""
            }
        }) {

            Text("Add Category")
        }
    }
}

class CategoryViewModel {

}
