package np.com.luminoussuwal.babybuy.Dashboard.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userid: Long = 0,
    var name : String,
    var phone : String,
    var email : String,
    var password: String
)
