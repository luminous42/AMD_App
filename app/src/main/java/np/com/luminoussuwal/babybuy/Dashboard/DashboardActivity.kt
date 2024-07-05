package np.com.luminoussuwal.babybuy.Dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private val homeFragment = HomeFragment()
    private val itemFragment = ItemFragment()
    private val offerFragment = OfferFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set action bar title
        supportActionBar?.title = "Dashboard"

        // Load home fragment by default
        loadRespectiveFragment(homeFragment)

        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadRespectiveFragment(homeFragment)
                    true
                }
                R.id.items -> {
                    loadRespectiveFragment(itemFragment)
                    true
                }
                R.id.offers -> {
                    loadRespectiveFragment(offerFragment)
                    true
                }
                R.id.profile -> {
                    loadRespectiveFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    // Function to load respective fragment
     fun loadRespectiveFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Handle back button press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
