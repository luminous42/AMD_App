package np.com.luminoussuwal.babybuy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import np.com.luminoussuwal.babybuy.Dashboard.DashboardActivity
import np.com.luminoussuwal.babybuy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.i(TAG, "onCreate: ")

        binding.btnLogin.setOnClickListener() {
            val email = binding.tieEmail.text.toString().trim()
            val password = binding.tiePassword.text.toString().trim()
            if (email.isNullOrEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please input an Email",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter a valid Email Address",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (password.isNullOrEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please input a password",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (password.matches(Regex("^[0-9]{6}$"))) {
                Toast.makeText(
                    this@LoginActivity,
                    "Password should be atleast be 6 digits",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                //local field validation success
                //TODO remote server or local db authentication


                //Storing login session in SharedPreferences
                val sharedPreferences = this@LoginActivity
                    .applicationContext.getSharedPreferences(
                        "login",
                        Context.MODE_PRIVATE
                    )

                var sharedPrefEditor = sharedPreferences.edit()
                sharedPrefEditor.putBoolean("isLoggedIn", true)
                sharedPrefEditor.apply()

                val testData = TestData(
                    variable1 = "Some Test Data",
                    variable2 = 1
                )
                //Navigating to Dashboard Activity

                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                intent.putExtra(AppConstants.KEY_EMAIL, email)
                intent.putExtra(AppConstants.KEY_TEST_DATA, testData)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }


}