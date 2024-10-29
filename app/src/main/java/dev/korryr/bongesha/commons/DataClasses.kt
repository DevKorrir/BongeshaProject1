package dev.korryr.bongesha.commons

data class Categoryr(
    val name: String,
    val imageRes: Int
)

data class Item(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    val imageUrl: String = ""
    //val variations: List<Variation>
)


///////////////////////////////////////////////////////////////////////////
// non functional downwards
///////////////////////////////////////////////////////////////////////////


data class CartItemr(
    val item: Item,
    var quantity: Int,
)

data class VerificationCode(
    val email: String,
    val code: String,
    val expiry: Long,
    val verified: Boolean = false
)

data class WishlistItemsw(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
)
