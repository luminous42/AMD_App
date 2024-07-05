package np.com.luminoussuwal.babybuy.Dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import np.com.luminoussuwal.babybuy.AppConstants
import np.com.luminoussuwal.babybuy.Dashboard.AddOrUpdateActivity.Companion.RESULT_CODE_COMPLETE
import np.com.luminoussuwal.babybuy.Dashboard.db.MainDatabase
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ActivityDetailViewBinding
import np.com.luminoussuwal.babybuy.utility.GeoCoding
import np.com.luminoussuwal.babybuy.utility.UiUtility

class DetailViewActivity : AppCompatActivity() {
    private lateinit var detailViewBinding: ActivityDetailViewBinding
    private var receivedProduct: Product? = null
    private var tieContact: TextInputEditText? = null

    companion object {
        const val RESULT_CODE_CANCEL = 2001
        const val RESULT_CODE_REFRESH = 2002
        const val SMS_PERMISSIONS_REQUEST_CODE = 111
    }

    private lateinit var startAddItemActivity: ActivityResultLauncher<Intent>
    private lateinit var startContactActivityForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewBinding = ActivityDetailViewBinding.inflate(layoutInflater)
        setContentView(detailViewBinding.root)
        bindAddOrUpdateActivityForResult()
        bindContactPickerActivityForResult()

        receivedProduct = intent.getParcelableExtra(AppConstants.KEY_PRODUCT)

        receivedProduct?.apply {
            populateDataToTheViews(this)
        }

        // Set up the ActionBar
        val actionBar = supportActionBar
        actionBar?.apply {
            title = "Product Details"
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        setUpMarkAsPurchasedCheckbox()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.detail_view_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val isSuggestion = intent.getBooleanExtra(AppConstants.KEY_IS_SUGGESTION, false)

        if (isSuggestion) {
            menu.findItem(R.id.action_share).isVisible = false
            menu.findItem(R.id.action_delete).isVisible = false
            menu.findItem(R.id.action_edit).isVisible = false

            setUpAddtoProductCheckbox()
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_edit -> {
                setUpEditButton()
                true
            }

            R.id.action_delete -> {
                setUpDeleteButton()
                true
            }

            R.id.action_share -> {
                setUpShareButton()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSIONS_REQUEST_CODE && areSmsPermissionsGranted(this)) {
            showSmsBottomSheetDialog()
        } else {
            UiUtility.showToast(this, "Please provide permission for SMS")
        }
    }

    private fun populateDataToTheViews(product: Product?) {
        detailViewBinding.tvTimestamp.text = product?.timeStamp
        detailViewBinding.productTitle.text = product?.name
        detailViewBinding.productPrice.text = product?.price
        detailViewBinding.productDescription.text = product?.description
        detailViewBinding.cbPurchased.isChecked = (product?.markAsPurchased == true)

        // Load image using Glide
        product?.image?.let { imageUrl ->
            detailViewBinding.productImage.post {
                Glide.with(applicationContext)
                    .load(Uri.parse(imageUrl))
                    .centerCrop()
                    .into(detailViewBinding.productImage)
            }
        }

        // Reverse geocode to get the actual location from latlng
        try {
            val lat = product?.storeLocationLat
            val lng = product?.storeLocationLng
            val geoCodedAddress = GeoCoding.reverseTheGeoCodeToAddress(
                this,
                lat ?: "",
                lng ?: ""
            )
            detailViewBinding.productLocation.text = geoCodedAddress
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        // Hide mark as purchased checkbox for suggestions
        if (intent.getBooleanExtra(AppConstants.KEY_IS_SUGGESTION, false)) {
            invalidateOptionsMenu()
            detailViewBinding.cbPurchased.visibility = View.GONE
            detailViewBinding.cbAddtoproduct.visibility = View.VISIBLE
        }

            else{
            detailViewBinding.cbAddtoproduct.visibility = View.GONE
        }

    }

    private fun setUpButtons() {
        setUpBackButton()
        setUpEditButton()
        setUpDeleteButton()
        setUpShareButton()
        setUpMarkAsPurchasedCheckbox()
    }

    private fun setUpBackButton() {
        setResultWithFinish(RESULT_CODE_REFRESH)
    }

    private fun setUpEditButton() {
        val intent = Intent(
            this,
            AddOrUpdateActivity::class.java
        ).apply {
            this.putExtra(AppConstants.KEY_PRODUCT, receivedProduct)
            this.putExtra(AppConstants.KEY_IS_UPDATE, true)
        }
        startAddItemActivity.launch(intent)
    }

    private fun setUpDeleteButton() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Confirmation")
            .setMessage("Do you want to delete this product?")
            .setPositiveButton(
                "Yes"
            ) { dialogInterface, i ->
                deleteProduct()
            }
            .setNegativeButton(
                "No"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun setUpShareButton() {
        if (areSmsPermissionsGranted(this)) {
            showSmsBottomSheetDialog()
        } else {
            requestPermissions(
                smsPermissionsList().toTypedArray(),
                SMS_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun setUpMarkAsPurchasedCheckbox() {
        detailViewBinding.cbPurchased.setOnCheckedChangeListener { _, isChecked ->
            handleCheckChangedForMarkAsPurchased(isChecked)
        }
    }

    private fun setUpAddtoProductCheckbox() {
        detailViewBinding.cbAddtoproduct.setOnCheckedChangeListener { _, isChecked ->
            handleCheckChangedForAddtoProduct(isChecked)
        }
    }

    private fun handleCheckChangedForAddtoProduct(isChecked: Boolean) {
        if (isChecked) {
            updateProductWithAddtoProductTrue()
        } else {
            updateProductWithAddtoProductFalse()
        }
    }

    private fun updateProductWithAddtoProductTrue(){
        receivedProduct?.apply {

            onAddItemClicked(this)
        }
    }

    private fun updateProductWithAddtoProductFalse() {


            MaterialAlertDialogBuilder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Do you want to delete this product?")
                .setPositiveButton(
                    "Yes"
                ) { dialogInterface, i ->
                    deleteProduct()
                }
                .setNegativeButton(
                    "No"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()

    }

    private fun onAddItemClicked(product: Product) {
        val db = MainDatabase.getInstance(this.applicationContext)
        val dao = db.getProductDao()

        // Create product object

        try {
            // Database operations in a background thread
            Thread {

                product.timeStamp =
                    UiUtility.getCurrentTimeStampWithActionSpecified("Created at ")
                dao.insertAProduct(product)

                runOnUiThread {
                    Snackbar.make(
                        detailViewBinding.root,
                        "Product added successfully!",
                        Snackbar.LENGTH_SHORT
                    ).show()


                    setResultWithFinish(RESULT_CODE_COMPLETE)
                }
            }.start()

        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {



                    UiUtility.showToast(
                        this@DetailViewActivity,
                        "Couldn't add product. Try Again."
                    )

            }

        }

    }












    private fun handleCheckChangedForMarkAsPurchased(isChecked: Boolean) {
        if (isChecked) {
            updateProductWithMarkAsPurchasedTrue()
        } else {
            updateProductWithMarkAsPurchasedFalse()
        }
    }














    private fun updateProductWithMarkAsPurchasedTrue() {
        receivedProduct?.markAsPurchased = true
        updateProductDataInDb(receivedProduct)
    }

    private fun updateProductWithMarkAsPurchasedFalse() {
        receivedProduct?.markAsPurchased = false
        updateProductDataInDb(receivedProduct)
    }

    private fun updateProductDataInDb(product: Product?) {
        val testDatabase = MainDatabase.getInstance(this.applicationContext)
        val productDao = testDatabase.getProductDao()

        Thread {
            try {
                product?.apply {
                    productDao.updateProduct(this)
                    runOnUiThread {
                        UiUtility.showToast(
                            this@DetailViewActivity,
                            "Product updated successfully"
                        )
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                runOnUiThread {
                    UiUtility.showToast(
                        this@DetailViewActivity,
                        "Cannot update product."
                    )
                }
            }
        }.start()
    }

    private fun deleteProduct() {
        val testDatabase = MainDatabase.getInstance(this.applicationContext)
        val productDao = testDatabase.getProductDao()

        Thread {
            try {
                receivedProduct?.apply {
                    productDao.deleteProduct(this)
                    runOnUiThread {
                        UiUtility.showToast(
                            this@DetailViewActivity,
                            "Product deleted successfully"
                        )
                        setResultWithFinish(RESULT_CODE_REFRESH)
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                runOnUiThread {
                    UiUtility.showToast(
                        this@DetailViewActivity,
                        "Cannot delete product."
                    )
                }
            }
        }.start()
    }

    private fun setResultWithFinish(resultCode: Int) {
        setResult(resultCode)
        finish()
    }

    private fun showSmsBottomSheetDialog() {
        val smsBottomSheetDialog = BottomSheetDialog(this)
        smsBottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_send_sms)

        val tilContact: TextInputLayout? = smsBottomSheetDialog.findViewById(R.id.til_contact)
        tieContact = smsBottomSheetDialog.findViewById(R.id.tie_contact)
        val sendSmsButton: MaterialButton? = smsBottomSheetDialog.findViewById(R.id.mb_send_sms)

        tilContact?.setEndIconOnClickListener {
            val pickContact = Intent(Intent.ACTION_PICK)
            pickContact.setDataAndType(
                ContactsContract.Contacts.CONTENT_URI,
                ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            )
            startContactActivityForResult.launch(pickContact)
        }

        sendSmsButton?.setOnClickListener {
            val contact = tieContact?.text.toString()
            if (contact.isBlank()) {
                tilContact?.error = "Enter Contact"
            } else {
                sendSms(contact)
                smsBottomSheetDialog.dismiss()
            }
        }
        smsBottomSheetDialog.setCancelable(true)
        smsBottomSheetDialog.show()
    }

    private fun areSmsPermissionsGranted(context: Context): Boolean {
        var areAllPermissionGranted = false
        for (permission in smsPermissionsList()!!) {
            if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                areAllPermissionGranted = true
            } else {
                areAllPermissionGranted = false
                break
            }
        }
        return areAllPermissionGranted
    }

    private fun smsPermissionsList(): List<String> {
        val smsPermissions: MutableList<String> = ArrayList()
        smsPermissions.add(Manifest.permission.READ_SMS)
        smsPermissions.add(Manifest.permission.SEND_SMS)
        smsPermissions.add(Manifest.permission.READ_CONTACTS)
        return smsPermissions
    }

    private fun sendSms(contact: String) {
        Thread {
            try {
                val smsManager: SmsManager = SmsManager.getDefault()
                val message = """
            Item: ${receivedProduct!!.name}
            Price: ${receivedProduct!!.price}
            Description: ${receivedProduct!!.description}
            """.trimIndent()
                smsManager.sendTextMessage(
                    contact,
                    null,
                    message,
                    null,
                    null
                )
                runOnUiThread {
                    UiUtility.showToast(this, "SMS Sent to your Contact.")
                }
            } catch (exception: Exception) {
                runOnUiThread {
                    UiUtility.showToast(this, "Couldn't Send SMS.")
                }
            }
        }.start()
    }

    private fun openSmsAppToSendMessage(contact: String, message: String) {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("smsto:$contact")
        sendIntent.putExtra("sms_body", message)
        startActivity(intent)
    }

    private fun fetchContactNumberFromData(data: Intent) {
        val contactUri = data.data

        // Specify which fields you want
        // your query to return values for
        val queryFields = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        this.contentResolver
            .query(
                contactUri!!,
                null,
                null,
                null,
                null
            )?.use { cursor ->
                // Double-check that you
                // actually got results
                if (cursor.count == 0) return

                // Pull out the first column of
                // the first row of data
                // that is your contact's name
                cursor.moveToFirst()
                val contactNumberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val contactNumber = cursor.getString(contactNumberIndex).apply {
                    //Replacing the brackets and hyphens with empty string as we don't need here
                    this.replace(
                        Regex("[()\\-\\s]+"),
                        ""
                    )
                }
                tieContact?.setText(contactNumber)
            }
    }

    private fun bindAddOrUpdateActivityForResult() {
        startAddItemActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AddOrUpdateActivity.RESULT_CODE_COMPLETE) {
                val product = it.data?.getParcelableExtra<Product>(AppConstants.KEY_PRODUCT)
                populateDataToTheViews(product)
            }
        }
    }

    private fun bindContactPickerActivityForResult() {
        startContactActivityForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it != null) {
                fetchContactNumberFromData(it.data!!)
            }
        }
    }
}

