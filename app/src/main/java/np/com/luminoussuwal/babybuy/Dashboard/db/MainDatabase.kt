package np.com.luminoussuwal.babybuy.Dashboard.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities =  [Product::class],
    version = 1
)

abstract class MainDatabase: RoomDatabase() {
    abstract fun getProductDao(): ProductDAO

    abstract fun getUserDao(): UserDAO

    companion object{
        private var INSTANCE: MainDatabase? = null

        fun getInstance(applicationContext: Context): MainDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    applicationContext,
                    MainDatabase::class.java, "main_db"
                ).build()
            }

            return INSTANCE!!
        }
    }
}