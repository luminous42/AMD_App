package np.com.luminoussuwal.babybuy.Dashboard


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import np.com.luminoussuwal.babybuy.Dashboard.db.MainDatabase
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ActivityAddOrUpdateBinding
import np.com.luminoussuwal.babybuy.databinding.ActivityLoginBinding
import java.io.File

class AddOrUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrUpdateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddOrUpdateBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)


        val mapFragment = MyMapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_fragment_container, mapFragment)
            .commit()


        binding.btnAddOrUpdate.setOnClickListener {
            val db = MainDatabase.getInstance(this.applicationContext)
            val dao = db.getProductDao()
          //  val imageFile = File(context.filesDir, "product_image.jpg")

            val selectedLatLng = mapFragment.getSelectedLocation()
            if (selectedLatLng != null) {
                val latitude = selectedLatLng.latitude
                val longitude = selectedLatLng.longitude

                val product = Product(
                    name = binding.tieItemName.text.toString(),
                    price = binding.tieItemPrice.text.toString(),
                    description = binding.tieItemDescription.text.toString(),
                    storeLocationLat = latitude.toString(),
                    storeLocationLng = longitude.toString(),
                    image = imageFile.absolutePath
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    dao.insertAProduct(product)

                    withContext(Dispatchers.Main) {
                        Snackbar.make(binding.root, "Product added successfully!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                Snackbar.make(binding.root, "Please select a location on the map", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Set up the ActionBar
        val actionBar = supportActionBar
        actionBar?.apply {
            title = "New Item"
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {onBackPressedDispatcher.onBackPressed() // Use the recommended way to navigate back
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}