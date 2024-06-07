package np.com.luminoussuwal.babybuy
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import java.io.Serializable
@Parcelize
data class DataClass(
    val variable1: String,
    val variable2: Int
): Parcelable
