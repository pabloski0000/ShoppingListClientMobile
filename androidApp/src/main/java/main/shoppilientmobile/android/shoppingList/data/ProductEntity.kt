package main.shoppilientmobile.android.shoppingList.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import main.shoppilientmobile.domain.Product

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
) {
    companion object {
        fun fromProduct(product: Product): ProductEntity {
            return ProductEntity(description = product.description)
        }
    }

    fun toProduct(): Product {
        return Product(description)
    }
}
