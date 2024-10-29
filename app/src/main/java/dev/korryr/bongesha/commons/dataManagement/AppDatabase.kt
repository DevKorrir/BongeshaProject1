package dev.korryr.bongesha.commons.dataManagement

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.korryr.bongesha.commons.data.CartItemEntity
import dev.korryr.bongesha.viewmodels.state.CartDao

@Database(entities = [CartItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}

