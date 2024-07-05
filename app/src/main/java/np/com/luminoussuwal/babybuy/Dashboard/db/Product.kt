package np.com.luminoussuwal.babybuy.Dashboard.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// Data class representing a product entity in the database
@Parcelize
@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,  // Primary key with auto-increment
    var name: String,
    var price: String,
    var description: String,
    var image: String? = null,
    var category: String? = null,
    @ColumnInfo(name = "store_Location_lat")
    var storeLocationLat: String? = null,  // Latitude of the store location (optional)
    @ColumnInfo(name = "store_Location_lng")
    var storeLocationLng: String? = null,  // Longitude of the store location (optional)
    @ColumnInfo(name = "mark_as_purchased")
    var markAsPurchased: Boolean = false,  // Flag to mark the product as purchased
    var quantity: String? = null,
    var delegateTo: String? = null,
    var timeStamp: String? = null
) : Parcelable  // Parcelable implementation for passing objects between components
