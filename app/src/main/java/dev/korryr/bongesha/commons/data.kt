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
    val price: Double
)

data class CartItem(
    val item: Item,
    var quantity: Int
)
