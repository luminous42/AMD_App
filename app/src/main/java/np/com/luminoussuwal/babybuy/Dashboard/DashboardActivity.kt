package np.com.luminoussuwal.babybuy.Dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ActivityDashboardBinding

class DashboardActivity :AppCompatActivity(){
    private lateinit var binding: ActivityDashboardBinding

    private val fragmentManager = supportFragmentManager

    private val homeFragment = HomeFragment()
    private val itemFragment = ItemFragment()
    private val offerFragment = OfferFragment()
    private val profileFragment = ProfileFragment()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)



        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "Dashboard"


//        val receivedEmail = intent.getStringExtra(AppConstants.KEY_EMAIL)
//
//        val receivedTestData: TestData?
//        if (android.os.Build.VERSION.SDK_INT >= 33) {
//            receivedTestData = intent.getParcelableExtra(
//                AppConstants.KEY_TEST_DATA,
//                TestData::class.java
//            )
//        } else {
//            receivedTestData = intent
//                .getParcelableExtra<TestData>(AppConstants.KEY_TEST_DATA)
//        }
//        Toast.makeText(
//            this@DashboardActivity,
//            "Received TestData : ".plus(receivedTestData?.variable1),
//            Toast.LENGTH_SHORT
//        ).show()

      //  loadFragment(homeFragment)

//        val navController = findNavController(homeFragment) // Get NavController
//        binding.bottomNavigation.
//        setupWithNavController(navController)

        loadRespectiveFragment(homeFragment)


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

//    private fun loadFragment(fragment: Fragment) {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.home_fragment, fragment)
//            .commit()
//    }

    public fun loadRespectiveFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainerView.id, fragment)
            .addToBackStack(null)
            .commit()
    }

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

