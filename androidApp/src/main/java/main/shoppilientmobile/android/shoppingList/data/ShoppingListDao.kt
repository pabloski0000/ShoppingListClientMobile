package main.shoppilientmobile.android.shoppingList.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import main.shoppilientmobile.domain.Product

@Dao
interface ShoppingListDao {
    fun getProducts(): List<Product> {
        val productEntities = getProductsRoom()
        return productEntities.map { Product(it.description) }
    }

    @Query("SELECT * FROM ProductEntity")
    fun getProductsRoom(): List<ProductEntity>

    fun insertProduct(product: Product) {
        val productEntity = ProductEntity(
            description = product.description
        )
        insertProductRoom(productEntity)
    }

    @Insert
    fun insertProductRoom(product: ProductEntity)

    fun updateProductDescription(oldProduct: Product, newProduct: Product) {
        updateProductDescriptionRoom(
            oldProduct.description,
            newProduct.description,
        )
    }

    @Query("UPDATE ProductEntity SET description = :newProductDescription " +
            "WHERE ProductEntity.description = :oldProductDescription")
    fun updateProductDescriptionRoom(oldProductDescription: String, newProductDescription: String)

    fun deleteProducts(products: List<Product>) {
        products.map { product ->
            deleteProductRoom(product.description)
        }
    }

    @Query("DELETE FROM ProductEntity WHERE description = :productDescription")
    fun deleteProductRoom(productDescription: String)

    fun repopulate(products: List<Product>) {
        deleteAllProductsRoom()
        products.map { insertProduct(it) }
    }

    @Query("DELETE FROM ProductEntity")
    fun deleteAllProductsRoom()
}