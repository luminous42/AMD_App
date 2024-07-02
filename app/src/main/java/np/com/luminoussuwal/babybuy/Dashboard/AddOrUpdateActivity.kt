package np.com.luminoussuwal.babybuy.Dashboard


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import np.com.luminoussuwal.babybuy.AppConstants
import np.com.luminoussuwal.babybuy.Dashboard.db.MainDatabase
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ActivityAddOrUpdateBinding
import np.com.luminoussuwal.babybuy.utility.BitmapScalar
import np.com.luminoussuwal.babybuy.utility.GeoCoding
import np.com.luminoussuwal.babybuy.utility.UiUtility
import java.io.IOException

class AddOrUpdateActivity : AppCompatActivity(), MyMapFragment.OnLocationSelectedListener {

    private lateinit var binding: ActivityAddOrUpdateBinding

    private lateinit var profilepic: ImageView
    private lateinit var fab: FloatingActionButton

    private var selectedImageUri: Uri? = null

    private var receivedProduct: Product? = null
    private var isForUpdate = false
    private var imageUriPath = ""
    private var productLatitude = ""
    private var productLongitude = ""
    private lateinit var startCustomCameraActivityForResult: ActivityResultLauncher<Intent>
    private lateinit var startGalleryActivityForResult: ActivityResultLauncher<Array<String>>
    private lateinit var startMapActivityForResult: ActivityResultLauncher<Intent>

    companion object {
        const val RESULT_CODE_COMPLETE = 1001
        const val RESULT_CODE_CANCEL = 1002
        const val GALLERY_PERMISSION_REQUEST_CODE = 11
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddOrUpdateBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setProductCategories("")
        bindCustomCameraActivityForResult()
        bindMapsActivityForResult()
        bindGalleryActivityForResult()

        updateContentIfProductReceived()



        val mapFragment = MyMapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_fragment_container, mapFragment)
            .commit()


        profilepic = binding.ivAddImage // access ImageView



        binding.fab.setOnClickListener {
                pickImageLauncher.launch("image/*") // Launch the image picker
            }



        binding.ivAddImage.setOnClickListener {
            handleImageAddButtonClicked()
        }

        binding.btnAddOrUpdate.setOnClickListener {
            val productName = binding.tieItemName.text.toString()
            val productPrice = binding.tieItemPrice.text.toString()
            val productDescription = binding.tieItemDescription.text.toString()
            val category = binding.actvSpinnerProductCategory.text.toString()

            if (productName.isBlank()) {
                Toast.makeText(
                    this@AddOrUpdateActivity,
                    "Please enter a product name",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(productPrice.isBlank()) {
                Toast.makeText(
                    this@AddOrUpdateActivity,
                    "Please enter a product price",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (productDescription.isBlank()) {
                Toast.makeText(
                    this@AddOrUpdateActivity,
                    "Please enter a product description",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (category.isBlank()) {
                Toast.makeText(
                    this@AddOrUpdateActivity,
                    "Please enter a product category",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val db = MainDatabase.getInstance(this.applicationContext)
                val dao = db.getProductDao()


                            val product = Product(
                                name = productName,
                                price = productPrice,
                                description = productDescription,
                                storeLocationLat = productLatitude,
                                storeLocationLng = productLongitude,
                                image = imageUriPath,
                                category = category
                            )
                try {
                    Thread {
                                if (isForUpdate) {
                                    product.id = receivedProduct!!.id
                                    product.timeStamp = UiUtility.getCurrentTimeStampWithActionSpecified("Updated at ")
                                    dao.updateProduct(product)
                                } else {
                                    product.timeStamp = UiUtility.getCurrentTimeStampWithActionSpecified("Created at ")
                                    dao.insertAProduct(product)
                                }
                                runOnUiThread  {
                                    Snackbar.make(
                                        binding.root,
                                        "Product added successfully!",
                                        Snackbar.LENGTH_SHORT
                                    ).show()

                                    clearFieldsData()
                                    setResultWithFinish(RESULT_CODE_COMPLETE, product)
                                }
                            }.start()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    runOnUiThread {
                        if (isForUpdate) {
                            UiUtility.showToast(
                                this@AddOrUpdateActivity,
                                "Couldn't update product. Try Again."
                            )
                        } else {
                            UiUtility.showToast(
                                this@AddOrUpdateActivity,
                                "Couldn't insert product. Try Again."
                            )
                        }
                    }
                    }
                }
            //}
        }
        // Set up the ActionBar
        val actionBar = supportActionBar
        actionBar?.apply {
            title = "New Item"
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }


    }
    override fun onLocationSelected(latitude: String, longitude: String) {
        productLatitude = latitude
        productLongitude = longitude
        onLocationDataFetched()
    }
    private fun clearFieldsData() {
        binding.tieItemPrice.text?.clear()
        binding.tieItemPrice.text?.clear()
        binding.tieItemDescription.text?.clear()
        binding.actvSpinnerProductCategory.text.clear()
    }
    private fun setResultWithFinish(resultCode: Int, product: Product?) {
        val intent = Intent()
        intent.putExtra(AppConstants.KEY_PRODUCT, product)
        setResult(resultCode, intent)
        finish()
    }

    private fun handleImageAddButtonClicked() {
        val pickImageBottomSheetDialog = BottomSheetDialog(this)
        pickImageBottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_pick_image)

        val linearLayoutPickByCamera: LinearLayout = pickImageBottomSheetDialog
            .findViewById(R.id.ll_pick_by_camera)!!
        val linearLayoutPickByGallery: LinearLayout = pickImageBottomSheetDialog
            .findViewById(R.id.ll_pick_by_gallery)!!

        linearLayoutPickByCamera.setOnClickListener {
            pickImageBottomSheetDialog.dismiss()
            startCameraActivity()
        }
        linearLayoutPickByGallery.setOnClickListener {
            pickImageBottomSheetDialog.dismiss()
            startGalleryToPickImage()
        }

        pickImageBottomSheetDialog.setCancelable(true)
        pickImageBottomSheetDialog.show()
    }

    private fun startCameraActivity() {
        val intent = Intent(this, CustomCameraActivity::class.java)
        startCustomCameraActivityForResult.launch(intent)
    }

    private fun allPermissionForGalleryGranted(): Boolean {
        var granted = false
        for (permission in getPermissionsRequiredForCamera()) {
            if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                granted = true
            }
        }
        return granted
    }

    private fun getPermissionsRequiredForCamera(): List<String> {
        val permissions: MutableList<String> = ArrayList()
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return permissions
    }

    private fun startGalleryToPickImage() {
        if (allPermissionForGalleryGranted()) {
            startActivityForResultFromGalleryToPickImage()
        } else {
            requestPermissions(
                getPermissionsRequiredForCamera().toTypedArray(),
                GALLERY_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startActivityForResultFromGalleryToPickImage() {
        val intent = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
//        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startGalleryActivityForResult.launch(arrayOf("image/*"))
    }

    private fun loadThumbnailImage(imageUriPath: String) {
        binding.ivAddImage.post {
            var bitmap: Bitmap?
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                    contentResolver,
                    Uri.parse(imageUriPath)
                )
                bitmap = BitmapScalar.stretchToFill(
                    bitmap,
                    binding.ivAddImage.width,
                    binding.ivAddImage.height
                )
                binding.ivAddImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun startMapActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startMapActivityForResult.launch(intent)
    }

    private fun onLocationDataFetched() {
        if (productLatitude.isBlank() || productLongitude.isBlank()) {
            return
        }

        try {
            val lat = productLatitude
            val lng = productLongitude
            val geoCodedAddress = GeoCoding.reverseTheGeoCodeToAddress(this, lat, lng)
            binding.mbProductLocation.text = geoCodedAddress
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
        }
    }


    private fun bindCustomCameraActivityForResult() {
        startCustomCameraActivityForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CustomCameraActivity.CAMERA_ACTIVITY_SUCCESS_RESULT_CODE) {
                imageUriPath = it.data?.getStringExtra(CustomCameraActivity.CAMERA_ACTIVITY_OUTPUT_FILE_PATH)!!
                loadThumbnailImage(imageUriPath)
            } else {
                imageUriPath = "";
                binding.ivAddImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }


    private fun bindGalleryActivityForResult() {
        startGalleryActivityForResult = registerForActivityResult(
            ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                imageUriPath = it.toString()
                contentResolver.takePersistableUriPermission(
                    Uri.parse(imageUriPath),
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                loadThumbnailImage(imageUriPath)
            } else {
                imageUriPath = "";
                binding.ivAddImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }


    private fun bindMapsActivityForResult() {
        startMapActivityForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == MapsActivity.MAPS_ACTIVITY_SUCCESS_RESULT_CODE) {
                productLatitude = it.data?.getStringExtra(AppConstants.KEY_PRODUCT_LATITUDE).toString()
                productLongitude = it.data?.getStringExtra(AppConstants.KEY_PRODUCT_LONGITUDE).toString()
                onLocationDataFetched()
            }
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

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            profilepic.setImageURI(it)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Assuming 'data' is the Intent result containing the Uri
        val uri: Uri? = data?.data

        // Ensure the URI is not null before setting it
        uri?.let {
            profilepic.setImageURI(it)
        }
    }


    private fun setProductCategories(selectedItem: String?) {
        val myAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.product_categories)
        )
        binding.actvSpinnerProductCategory.setAdapter(myAdapter)

        if (!selectedItem.isNullOrBlank()) {
            binding.actvSpinnerProductCategory.setText(selectedItem, false)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResultWithFinish(RESULT_CODE_CANCEL, null)
    }

    //Updating content in page if it is an update case
    private fun updateContentIfProductReceived() {
        receivedProduct = intent.getParcelableExtra(AppConstants.KEY_PRODUCT)
        receivedProduct?.apply {
            binding.btnAddOrUpdate.text = "Update"
            isForUpdate = true
            binding.tieItemName.setText(this.name)
            binding.tieItemPrice.setText(this.price)
            binding.tieItemDescription.setText(this.description)
            binding.actvSpinnerProductCategory.setText(this.category)
            productLatitude = this.storeLocationLat.toString()
            productLongitude = this.storeLocationLng.toString()
            this.image?.apply {
                loadThumbnailImage(this)
                imageUriPath = this
            }
            val lat = this.storeLocationLat
            val lng = this.storeLocationLng
            val geoCodedAddress = GeoCoding.reverseTheGeoCodeToAddress(
                this@AddOrUpdateActivity,
                lat ?: "",
                lng ?: ""
            )
           binding.mbProductLocation.text = geoCodedAddress

        }
    }
}