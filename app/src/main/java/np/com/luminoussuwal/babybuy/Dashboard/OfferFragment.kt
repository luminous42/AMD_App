package np.com.luminoussuwal.babybuy.Dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.FragmentOfferBinding


class OfferFragment : Fragment() {

    private lateinit var binding: FragmentOfferBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOfferBinding.inflate(
            layoutInflater,
            container,
            false
        )

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Offers"
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        return binding.root
    }


}