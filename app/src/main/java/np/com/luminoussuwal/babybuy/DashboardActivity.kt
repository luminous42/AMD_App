package np.com.luminoussuwal.babybuy

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import np.com.luminoussuwal.babybuy.databinding.ActivityDashboardBinding

class DashboardActivity :AppCompatActivity(){
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedEmail = intent.getStringExtra(AppConstants.KEY_EMAIL)

        val receivedTestData: TestData?
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            receivedTestData = intent.getParcelableExtra(
                AppConstants.KEY_TEST_DATA,
                TestData::class.java
            )
        } else {
            receivedTestData = intent
                .getParcelableExtra<TestData>(AppConstants.KEY_TEST_DATA)
        }
        Toast.makeText(
            this@DashboardActivity,
            "Received TestData : ".plus(receivedTestData?.variable1),
            Toast.LENGTH_SHORT
        ).show()

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.items -> {
                    // Load the ItemsFragment (similar to HomeFragment)
                    true
                }
                R.id.profile -> {
                    // Load the ProfileFragment (similar to HomeFragment)
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

