package np.com.luminoussuwal.babybuy.Dashboard.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var price: String,
    var description: String,
    var image: String? = null,
    var category: String? = null,
    @ColumnInfo(name="store_Location_lat")
    var storeLocationLat: String? = null,
    @ColumnInfo(name="store_Location_lng")
    var storeLocationLng: String? = null,
    @ColumnInfo(name="mark_as_purchased")
    var markAsPurchased: Boolean = false,
    var quantity: String? = null,
    var delegateTo: String? = null
)
