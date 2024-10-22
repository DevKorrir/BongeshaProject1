package dev.korryr.bongesha.commons

data class Category(
    val id: String,
    val name: String,
    val items: List<Item>,
    val icon: Int
)

data class Item(
    val id: String,
    val name: String,
    val description: String,
    val image: Int,
    val price: Double,
    val imageUrl: String = ""
    //val variations: List<Variation>
)


data class CartItem(
    val item: Item,
    var quantity: Int,
)

data class VerificationCode(
    val email: String,
    val code: String,
    val expiry: Long,
    val verified: Boolean = false
)

data class WishlistItems(
    val id: String,
    val name: String,
    val description: String,
    val image: Int,
    val price: Double,
)
