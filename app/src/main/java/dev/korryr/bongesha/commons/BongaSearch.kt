package dev.korryr.bongesha.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.korryr.bongesha.MainViewModel
import dev.korryr.bongesha.R

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
        hint = "Search",
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
