package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.CartItem
import dev.korryr.bongesha.commons.Category
import dev.korryr.bongesha.commons.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BongaCategoryViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> get() = _cart

    private val _selectedItem = MutableStateFlow<Item?>(null)
    val selectedItem: StateFlow<Item?> get() = _selectedItem

    init {
        // Initialize with some sample data
        _categories.value = listOf(
            Category(
                id = "1",
                name = "Beverages",
                items = listOf(
                    Item(
                        id = "a1",
                        name = "Coca cola",
                        description = "500ml pet",
                        image = R.drawable.coke,
                        price = 70.00
                    ),
                    Item(
                        id = "a2",
                        name = "Fanta Orange",
                        description = "500ml pet",
                        image = R.drawable.fanta_orange_pet,
                        price = 70.00
                    ),
                    Item(
                        id = "a3",
                        name = "Fanta Blackcurrant",
                        description = "500ml pet",
                        image = R.drawable.fanta_blackcurrant_pet,
                        price = 70.00
                    ),
                    Item(
                        id = "a4",
                        name = "Fanta Blackcurrant",
                        description = "350ml pet",
                        image = R.drawable.fanta_blackcurrant_pet,
                        price = 50.00
                    ),
                    Item(
                        id = "a5",
                        name = "Sprite Pet",
                        description = "500ml pet",
                        image = R.drawable.sprite_pet,
                        price = 70.00
                    ),
                    Item(
                        id = "a6",
                        name = "Stoney Class",
                        description = "300ml pet",
                        image = R.drawable.stoney_class,
                        price = 40.00
                    ),
                    Item(
                        id = "a7",
                        name = "Minute Maid Tropical juice",
                        description = "400ml pet",
                        image = R.drawable.minute_maid_tropical,
                        price = 75.00
                    ),
                    Item(
                        id = "a8",
                        name = "Minute Maid Apple Juice",
                        description = "400ml pet",
                        image = R.drawable.minute_maid_apple,
                        price = 75.00
                    )
                ),
                icon = R.drawable.beverages
            ),
            Category(
                id = "2",
                name = "Home Care",
                items = listOf(
                    Item(
                        id = "b1",
                        name = "Dettol Original",
                        description = "Antibacterial handwash 60g",
                        image = R.drawable.dettol_original,
                        price = 90.00
                    ),
                    Item(
                        id = "b2",
                        name = "Geisha",
                        description = "Soothing Aloe Vera & Honey Soap 60g",
                        image = R.drawable.geisha_green,
                        price = 50.00
                    ),
                    Item(
                        id = "b3",
                        name = "Geisha",
                        description = "Fragrant Rose & Honey Soap 60g",
                        image = R.drawable.geisha_pink,
                        price = 50.00
                    )
                ),
                icon = R.drawable.home_care
            ),
            Category(
                id = "3",
                name = "Personal Care",
                items = listOf(
                    Item(
                        id = "c1",
                        name = "Vaseline Lotion",
                        description = "Cocoa Radiant 100ml",
                        image = R.drawable.vaseline_coco,
                        price = 130.00
                    ),
                    Item(
                        id = "c2",
                        name = "Vaseline Jelly",
                        description = "Perfumed 95ml",
                        image = R.drawable.vaseline_jelly_perfumed,
                        price = 85.00
                    ),
                    Item(
                        id = "c3",
                        name = "Vaseline Jelly",
                        description = "Cocoa Butter Perfumed 95ml",
                        image = R.drawable.vaseline_jelly_coco,
                        price = 85.00
                    ),
                    Item(
                        id = "c4",
                        name = "Vaseline Lotion",
                        description = "Perfumed 100ml",
                        image = R.drawable.vaseline_pink,
                        price = 130.00
                    )
                ),
                icon = R.drawable.personal_care
            ),
            Category(
                id = "4",
                name = "Home & Kitchen",
                items = listOf(
                    Item(
                        id = "d1",
                        name = "Steel wool",
                        description = "Sokoni wool",
                        image = R.drawable.steel_wool,
                        price = 10.00
                    ),
                    Item(
                        id = "d2",
                        name = "Steel wire",
                        description = "Strong With Rust resistance",
                        image = R.drawable.stell_wired,
                        price = 20.00
                    ),
                    Item(
                        id = "d3",
                        name = "Sugura Sponge",
                        description = "Scouring power",
                        image = R.drawable.sugura_green,
                        price = 15.00
                    )
                ),
                icon = R.drawable.home_kitchen
            ),
            Category(
                id = "5",
                name = "Spread & Bread",
                items = listOf(
                    Item(
                        id = "e1",
                        name = "Blue Band",
                        description = "Original 250g",
                        image = R.drawable.blue_band,
                        price = 150.00
                    ),
                    Item(
                        id = "e2",
                        name = "Prestige",
                        description = "Original 500g",
                        image = R.drawable.prestige_spread,
                        price = 200.00
                    )
                ),
                icon = R.drawable.bread_spread
            ),
            Category(
                id = "6",
                name = "Rice & Pasta",
                items = listOf(
                    Item(
                        id = "f1",
                        name = "Daawati Rice",
                        description = "Long Grain Traditional 1kg",
                        image = R.drawable.daawati_rice,
                        price = 180.00
                    ),
                    Item(
                        id = "f2",
                        name = "Pazani Spaghetti",
                        description = "Nutritional 400g",
                        image = R.drawable.spaghetti_pazani,
                        price = 80.00
                    ),
                    Item(
                        id = "f3",
                        name = "Noodles",
                        description = "Kuku Flavour 12g",
                        image = R.drawable.indomie_noodles,
                        price = 45.00
                    )
                ),
                icon = R.drawable.rice
            ),
            Category(
                id = "7",
                name = "Snacks",
                items = listOf(
                    Item(
                        id = "g1",
                        name = "Kit Kat",
                        description = "Milk Chocolate 20g",
                        image = R.drawable.kit_kat_biscuit,
                        price = 20.00
                    ),
                    Item(
                        id = "g2",
                        name = "Choco",
                        description = "Creame chocolate 20g",
                        image = R.drawable.choco_biscuit,
                        price = 10.00
                    )
                ),
                icon = R.drawable.snacks
            )
        )
    }

    private fun getItemById(itemId: String): Item? {
        for (category in _categories.value) {
            val item = category.items.find { it.id == itemId }
            if (item != null) return item
        }
        return null
    }

    fun selectItem(itemId: String) {
        _selectedItem.value = getItemById(itemId)
    }

    fun addToCart(item: Item, quantity: Int = 1) {
        val currentCart = _cart.value.toMutableList()
        val existingItem = currentCart.find { it.item.id == item.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentCart.add(CartItem(item, quantity))
        }
        _cart.value = currentCart
    }
}
