package np.com.luminoussuwal.babybuy.Dashboard.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Define the database with Room
@Database(
    entities = [Product::class],  // List of entity classes included in the database
    version = 1  // Version number of the database
)
abstract class MainDatabase : RoomDatabase() {
    // Abstract function to get the ProductDAO
    abstract fun getProductDao(): ProductDAO

    // Abstract function to get the UserDAO
    abstract fun getUserDao(): UserDAO

    companion object {
        // Singleton instance of MainDatabase
        private var INSTANCE: MainDatabase? = null

        // Function to get the singleton instance of the database
        fun getInstance(applicationContext: Context): MainDatabase {
            // If the instance is null, create a new database instance
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    applicationContext,
                    MainDatabase::class.java, "main_db"  // Specify the database name
                ).build()
            }

            // Return the database instance
            return INSTANCE!!
        }
    }
}