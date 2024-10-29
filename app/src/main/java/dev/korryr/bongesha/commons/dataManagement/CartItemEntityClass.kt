package dev.korryr.bongesha.commons.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: Float,
    val quantity: Int,
    val itemCount: Int, // Available stock count
    val imageUrl: String
)
