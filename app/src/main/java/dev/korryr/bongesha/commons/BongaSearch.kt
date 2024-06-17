package dev.korryr.bongesha.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.korryr.bongesha.MainViewModel
import dev.korryr.bongesha.R
import dev.korryr.bongesha.ui.theme.gray01

@Composable
fun BongaSearch(
    //query: String,
    onChange:(String) -> Unit,
    onSearchClick: @Composable () -> Unit,
){

    val viewModel = viewModel<MainViewModel>()
    val searchText by viewModel.searchText.collectAsState()
    val persons by viewModel.products.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    var query by remember {
        mutableStateOf("")
    }
    val current = LocalFocusManager.current
    val softwareKeyboard = LocalSoftwareKeyboardController.current
    Bongatextfield(
        label = "",
        fieldDescription = "",
        input = query,
        hint = "What are you looking for",
        onChange = viewModel::onSearchTextChange,
        leading = painterResource(id = R.drawable.search_icon),
       /* keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClick()
            }
        )
        ,
        */
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        )

    )
}



@Composable
fun SearchyBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    val viewModel = viewModel<MainViewModel>()
    val searchText by viewModel.searchText.collectAsState()
    val persons by viewModel.products.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    OutlinedTextField(
        value = query,
        onValueChange = viewModel::onSearchTextChange,
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.apple_icon),
                contentDescription = null,
                tint = Color.Gray
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick() }
        ),
        modifier = Modifier.fillMaxWidth(),
    )

    if(isSearching) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                //.weight(1f)
        ) {/*
            items(products) { product ->
                Text(
                    text = "${product.firstName} ${product.lastName}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
            */
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BongaSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
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
                containerColor = Color.White
            )
        )
        if (active) {
            Text(text = "Search Results")
        }
    }
}

