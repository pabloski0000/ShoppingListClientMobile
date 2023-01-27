package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import main.shoppilientmobile.android.shoppingList.data.ProductEntity
import main.shoppilientmobile.android.shoppingList.data.ShoppingListDao

@Database(
    entities = [UserRoleEntity::class, ProductEntity::class],
    version = 2,
)
abstract class RoomDb : RoomDatabase() {
    abstract fun userRoleDao(): UserRoleDao
    abstract fun shoppingListDao(): ShoppingListDao
}