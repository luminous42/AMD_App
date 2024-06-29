package np.com.luminoussuwal.babybuy.Dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import np.com.luminoussuwal.babybuy.Dashboard.adapters.OffersHorizontalAdapter
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding.ibItems.setOnClickListener {
            (activity as? DashboardActivity)?.loadRespectiveFragment(ItemFragment())
                }

        binding.ibOffers.setOnClickListener {
            (activity as? DashboardActivity)?.loadRespectiveFragment(OfferFragment())
        }

        setUpOffersRecyclerView()

        setUpFabButton()
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.tvWelcome.text = "Hello, welcome to BabyBuy!"
//
//        val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
//        val adapter = ItemAdapter(items)
//        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.recyclerView.adapter = adapter
//    }

    private fun setUpOffersRecyclerView(){
        val adapter = OffersHorizontalAdapter(generateOffers())
        binding.rvMyOffers.layoutManager = LinearLayoutManager(
            requireActivity(),
            RecyclerView.HORIZONTAL,
            false
        )

        binding.rvMyOffers.adapter = adapter
    }

    private  fun generateOffers():List<Product>{
        val products = mutableListOf<Product>()

        for(i in 1..10){
            val product = Product(
                name = "Baby Bottle".plus(i),
                price = (100*i).toString(),
                description = "This is the best bottle there is out there.".plus(i)
            )
            products.add(product)
        }
        return products
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
}