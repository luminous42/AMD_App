package np.com.luminoussuwal.babybuy.Dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import np.com.luminoussuwal.babybuy.Dashboard.db.MainDatabase
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ActivityAddOrUpdateBinding

class AddOrUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrUpdateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityAddOrUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mbAddOrUpdate.setOnClickListener {
            val db = MainDatabase.getInstance(this.applicationContext)
            val dao = db.getProductDao()

            Thread {
                dao.deleteProducts()
            }.start()

        }
    }
}