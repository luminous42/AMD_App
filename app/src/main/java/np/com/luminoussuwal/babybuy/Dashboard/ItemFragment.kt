package np.com.luminoussuwal.babybuy.Dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import np.com.luminoussuwal.babybuy.AppConstants
import np.com.luminoussuwal.babybuy.Dashboard.db.MainDatabase
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.ItemAdapter
import np.com.luminoussuwal.babybuy.databinding.FragmentItemBinding
import np.com.luminoussuwal.babybuy.utility.UiUtility




    class ItemFragment : Fragment(), ItemAdapter.ItemAdapterListener {
        private lateinit var binding: FragmentItemBinding
        private lateinit var startAddOrUpdateActivityForResult: ActivityResultLauncher<Intent>
        private lateinit var startDetailViewActivity: ActivityResultLauncher<Intent>
        private lateinit var adapter: ItemAdapter
        private val productList = mutableListOf<Product>()
        companion object {
            const val ADD_UPDATE_REQUEST_CODE = 1001
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            startAddOrUpdateActivityForResult = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == AddOrUpdateActivity.RESULT_CODE_COMPLETE) {
                    setUpRecyclerView()
                }
            }
            startDetailViewActivity = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == DetailViewActivity.RESULT_CODE_REFRESH) {
                    setUpRecyclerView()
                }
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            // Inflate the layout for this fragment
            binding = FragmentItemBinding.inflate(
                layoutInflater,
                container,
                false
            )

            (activity as? AppCompatActivity)?.supportActionBar?.apply {
                title = "My Items"
                setDisplayHomeAsUpEnabled(false) // Enable the back button
            }

            setUpRecyclerView()

            setUpFabButton()



            return binding.root


        }

        private fun setUpFabButton() {
            binding.fabAddItem.setOnClickListener {
                val intent = Intent(
                    requireActivity(),
                    AddOrUpdateActivity::class.java
                )
                startActivityForResult(intent, ADD_UPDATE_REQUEST_CODE)

            }
        }

        private fun setUpRecyclerView() {

            val db = MainDatabase.getInstance(requireContext())
            var productDao = db.getProductDao()

            Thread {
                try {
                    val products = productDao.getAllProducts().map { it }
                    if (products.isEmpty()) {
                        requireActivity().runOnUiThread {
                            UiUtility.showToast(requireActivity(), "No Items Added.")
                            populateRecyclerView(emptyList())
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            populateRecyclerView(products)
                        }
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    requireActivity().runOnUiThread {
                        UiUtility.showToast(requireActivity(), "Couldn't load items.")
                    }
                }
            }.start()

//        binding.rvMyItems.layoutManager = LinearLayoutManager(
//            requireActivity(),
//            RecyclerView.VERTICAL,
//            false
//        )
        }

        private fun populateRecyclerView(products: List<Product>) {
            adapter = ItemAdapter(
                products,
                this,
                requireActivity().applicationContext
            )
            binding.rvMyItems.adapter = adapter
            binding.rvMyItems.layoutManager = LinearLayoutManager(
                requireActivity(), RecyclerView.VERTICAL,
                false
            )
        }

        override fun onItemClicked(product: Product, position: Int) {
            val intent = Intent(requireActivity(), DetailViewActivity::class.java)
            intent.putExtra(AppConstants.KEY_PRODUCT, product)
            startDetailViewActivity.launch(intent)
        }
//    private  fun generateOffers():List<Product>{
//        val products = mutableListOf<Product>()
//
//        for(i in 1..10){
//            val product = Product(
//                name = "Baby Bottle".plus(i),
//                price = (100*i).toString(),
//                description = "This is the best bottle there is out there.".plus(i)
//            )
//            products.add(product)
//        }
//        return products
//    }
private fun refreshData() {
    setUpRecyclerView()//, e.g., re-fetch from the database or API
    // Notify adapter about data changes
}

        override fun onResume() {
            super.onResume()
            refreshData()
        }


    }
