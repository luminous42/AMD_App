package np.com.luminoussuwal.babybuy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestData(
    val variable1: String,
    val variable2: Int
): Parcelable
