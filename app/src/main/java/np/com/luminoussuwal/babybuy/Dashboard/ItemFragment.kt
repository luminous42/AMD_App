package np.com.luminoussuwal.babybuy.Dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import np.com.luminoussuwal.babybuy.Dashboard.adapters.OffersHorizontalAdapter
import np.com.luminoussuwal.babybuy.Dashboard.db.MainDatabase
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.Dashboard.db.ProductDAO
import np.com.luminoussuwal.babybuy.ItemAdapter
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.FragmentItemBinding


class ItemFragment : Fragment() {

    private lateinit var binding: FragmentItemBinding
    private lateinit var productDao: ProductDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        setUpOffersRecyclerView()

        setUpFabButton()



        lifecycleScope.launch(Dispatchers.IO) {
            val db = MainDatabase.getInstance(requireContext())
            productDao = db.getProductDao()
            val products = productDao.getAllProducts() // Fetch all products
            withContext(Dispatchers.Main) {
                val adapter = ItemAdapter(products)


                binding.rvMyItems.adapter = adapter
            }

        }

        return binding.root


    }

    private fun setUpFabButton(){
        binding.fabAddItem.setOnClickListener{
            val intent = Intent(
                requireActivity(),
                AddOrUpdateActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun setUpOffersRecyclerView(){

        binding.rvMyItems.layoutManager = LinearLayoutManager(
            requireActivity(),
            RecyclerView.VERTICAL,
            false
        )
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

}