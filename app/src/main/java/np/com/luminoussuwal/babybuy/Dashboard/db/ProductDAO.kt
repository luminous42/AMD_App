package np.com.luminoussuwal.babybuy.Dashboard.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Data Access Object (DAO) for the Product entity
@Dao
interface ProductDAO {

    // Insert a single product into the database
    @Insert
    fun insertAProduct(product: Product)

    // Insert a list of products into the database
    @Insert
    fun insertProducts(products: List<Product>)

    // Insert multiple products into the database
    @Insert
    fun insertProducts(vararg products: Product)

    // Update an existing product in the database
    @Update
    fun updateProduct(product: Product)

    // Delete a single product from the database
    @Delete
    fun deleteProduct(product: Product)

    // Delete all products from the database
    @Query("Delete from product")
    fun deleteProducts()

    // Get a list of all products from the database
    @Query("Select * from product")
    fun getAllProducts(): List<Product>

    // Get a list of all products marked as purchased
    @Query("Select * from product where mark_as_purchased = 1")
    fun getAllMarkAsPurchasedProducts(): List<Product>

    // Get a list of products with a specific name
    @Query("Select * from product where name like :names")
    fun getAllProductsWithGivenName(names: String): List<Product>
}