package main.shoppilientmobile.android.core.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import main.shoppilientmobile.android.shoppingList.data.ProductEntity
import main.shoppilientmobile.android.shoppingList.data.ShoppingListDao

@Database(
    entities = [UserRoleEntity::class, ProductEntity::class, UserEntity::class],
    version = 3,
)
abstract class RoomDb : RoomDatabase() {
    abstract fun userRoleDao(): UserRoleDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun getUserDao(): UserDao
}