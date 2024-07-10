package dev.korryr.bongesha

import android.content.SharedPreferences

//val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
private lateinit var sharedPreferences: SharedPreferences
private fun saveUserDetails(email: String?, displayName: String?) {
    sharedPreferences.edit().apply {
        putString("userEmail", email)
        putString("userDisplayName", displayName)
        apply()
    }
}




private fun clearUserSignInState() {
    sharedPreferences.edit().apply {
        putBoolean("isSignedIn", false)
        remove("userEmail")
        remove("userDisplayName")
        apply()
    }
}



/*class MainVijewModel: ViewModel() {

    private val  _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _products = MutableStateFlow(allproducts)
    val products = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_products) { text, products ->
            if (text.isBlank()){
                products
            } else {
                delay(2000L)
                products.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false }}
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _products.value
        )
    fun  onSearchTextChange(text: String) {
        _searchText.value = text
    }
}

data class Product(
    val firstName: String,
    val lastName: String
) {
    fun doesMatchSearchQuery(query: String): Boolean{
        val matchingCombainations = listOf(
            "$firstName$lastName"
        )

        return matchingCombainations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

private val allproducts = listOf(
    Product(
        firstName = "tecno",
        lastName = "camon"
    )
)
*/
