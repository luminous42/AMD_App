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
import np.com.luminoussuwal.babybuy.Dashboard.adapters.ItemAdapter
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.databinding.FragmentItemBinding
import np.com.luminoussuwal.babybuy.utility.UiUtility


class OfferFragment : Fragment(), ItemAdapter.ItemAdapterListener {

    private lateinit var binding: FragmentItemBinding
    private lateinit var startDetailViewActivity: ActivityResultLauncher<Intent>
    private lateinit var adapter: ItemAdapter

    private val productList = listOf(
        Product(
            1,
            "Gerber Organic Baby Food",
            "1200",
            "Gerber Organic 2nd Foods Baby Food, Banana, Apple, Blueberry, 4 oz, 2 Pack",
            "https://i5.walmartimages.ca/images/Large/183/947/6000197183947.jpg",
            "Food"
        ),
        Product(
            2,
            "Fisher-Price Rock-a-Stack",
            "2500",
            "Fisher-Price Rock-a-Stack Baby Toy with 5 Colorful Rings for Infants Ages 6 Months and Older",
            "https://toybook.com/wp-content/uploads/sites/4/2023/12/FisherPrice_Stack-jpg.webp",
            "Toys"
        ),
        Product(
            3,
            "Johnson's First Touch",
            "4197",
            "Johnson's First Touch Gift Set, Baby Bath & Skin Products, 5 items",
            "https://images-cdn.ubuy.co.in/656cc0512d450018b36c6707-johnson-s-first-touch-gift-set-baby.jpg",
            "Skin"
        ),
        Product(
            4,
            "Pampers Diapers",
            "1500",
            "Pampers Newborn Diapers, 76 count",
            "https://nepaleasyshopping.com/images/product/1609832051_Pampers%20New%20Diapers%20Pants.jpg",
            "Diapers"
        ),
        Product(
            5,
            "Huggies Wipes",
            "500",
            "Huggies Natural Care Sensitive Baby Wipes, Unscented, 3 Pack, 168 Total Wipes",
            "https://pics.walgreens.com/prodimg/395177/900.jpg",
            "Wipes"
        ),
        Product(
            6,
            "Chicco Next2Me Crib",
            "12000",
            "Chicco Next2Me Side-Sleeping Crib, Dove Grey",
            "https://www.nepal.ubuy.com/productimg/?image=aHR0cHM6Ly9pbWFnZXMtbmEuc3NsLWltYWdlcy1hbWF6b24uY29tL2ltYWdlcy9JLzQxQ2l6akktelpMLl9TUzQwMF8uanBn.jpg",
            "Furniture"
        ),
        Product(
            7,
            "Avent Baby Bottle",
            "1800",
            "Philips Avent Natural Baby Bottle, Clear, 9oz, 3 Pack",
            "https://static.beautytocare.com/cdn-cgi/image/width=1600,height=1600,f=auto/media/catalog/product//p/h/philips-avent-natural-response-airfree-vent-baby-bottle-1m-bear-260ml.png",
            "Feeding"
        ),
        Product(
            8,
            "Baby Einstein Musical Toy",
            "3500",
            "Baby Einstein Take Along Tunes Musical Toy, Ages 3 months+",
            "https://www.kids2.com/cdn/shop/files/dc7bfwhmrphtzc2v9li7.jpg?v=1701888031",
            "Toys"
        ),
        Product(
            9,
            "Baby Monitor",
            "8000",
            "Infant Optics DXR-8 Video Baby Monitor with Interchangeable Optical Lens",
            "https://m.media-amazon.com/images/I/61QEntSevyL.jpg",
            "Electronics"
        ),
        Product(
            10,
            "Baby Carrier",
            "6000",
            "Ergobaby Omni 360 All-Position Baby Carrier for Newborn to Toddler, Pearl Grey",
            "https://i5.walmartimages.com/seo/Baby-Carrier-Convertible-Ergonomic-Kangaroo-Bag-Breathable-Front-Facing-Infant-Backpack-Pouch-Wrap-Sling-Newborns-Green_2c150b6b-48da-4f2d-97f2-c0e78f955391_1.70f2fdba5e6f2defd1e765c467da4084.jpeg",
            "Accessories"
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            title = "Offers"
            setDisplayHomeAsUpEnabled(false) // Enable the back button
        }

        setUpRecyclerView()

        binding.addBtn.visibility = View.GONE
        binding.fabAddItem.visibility = View.GONE
        binding.tvLblMyItems.text = "Most Wished For Products"
        return binding.root


    }


    private fun setUpRecyclerView() {


        try {
            populateRecyclerView(productList)

        } catch (exception: Exception) {
            exception.printStackTrace()
            requireActivity().runOnUiThread {
                UiUtility.showToast(requireActivity(), "Couldn't load items.")
            }
        }


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
        intent.putExtra(AppConstants.KEY_IS_UPDATE, true)
        intent.putExtra(AppConstants.KEY_IS_SUGGESTION, true)
        startDetailViewActivity.launch(intent)

    }

    override fun onOfferClicked(product: Product, position: Int) {
        val intent = Intent(requireActivity(), DetailViewActivity::class.java)
        intent.putExtra(AppConstants.KEY_PRODUCT, product)
        intent.putExtra(AppConstants.KEY_IS_UPDATE, true)
        intent.putExtra(AppConstants.KEY_IS_SUGGESTION, true)
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

}
