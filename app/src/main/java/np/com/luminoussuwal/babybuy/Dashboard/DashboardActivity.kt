package np.com.luminoussuwal.babybuy.Dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ActivityDashboardBinding

class DashboardActivity :AppCompatActivity(){
    private lateinit var binding: ActivityDashboardBinding

    private val homeFragment = HomeFragment()
    private val itemFragment = ItemFragment()
    private val offerFragment = OfferFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(homeFragment)
                    true
                }
                R.id.items -> {
                    loadFragment(itemFragment)
                    true
                }
                R.id.offers -> {
                    loadFragment(offerFragment)
                    true
                }
                R.id.profile -> {
                    loadFragment(profileFragment)
                    true
                }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.home
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_fragment, fragment)
            .commit()
    }
}

