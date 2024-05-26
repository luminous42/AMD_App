package np.com.luminoussuwal.babybuy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import np.com.luminoussuwal.babybuy.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding to Splash Activity XML File
        binding =ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Navigating to Login Activity
        Handler().postDelayed({

        //Check LoggedIn State in SharedPreferences
        val sharedPreferences =this@SplashActivity.
        application.getSharedPreferences(
            "login",
            Context.MODE_PRIVATE
            )

        val isLoggedIn = sharedPreferences.getBoolean(
            "isLoggedIn",
            false
        )

        if(isLoggedIn){
            val intent = Intent(
                this@SplashActivity,
                DashboardActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(
                this@SplashActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        },
            3000

        )

    }
}