package dev.korryr.bongesha.commons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.orange28


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BongaSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchResults: List<Item>
){
    val showClearIcon = remember {
        mutableStateOf(query.isNotEmpty())
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Column{
        OutlinedTextField(
            shape = RoundedCornerShape(12.dp),
            value = query,
            onValueChange = {
                onQueryChange(it)
                showClearIcon.value = it.isNotEmpty()
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("What are you looking for?") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
            },
            trailingIcon = {
                if (showClearIcon.value) {
                    IconButton(
                        onClick = {
                            onQueryChange("")
                        }
                    ) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = Color.Red,
                        )
                    }
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = gray01,
                focusedBorderColor = Color.Gray,
                containerColor = Color.White,
                cursorColor = orange28,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray
            )
        )
        if (active) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(searchResults) { item ->
                    SearchResultItem(item = item)
                }
            }
            //Text(text = "Search Results")
        }
    }
}

@Composable
private fun SearchResultItem(item: Item) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = item.name,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.description
        )
        // Add more item details as needed
    }
}

