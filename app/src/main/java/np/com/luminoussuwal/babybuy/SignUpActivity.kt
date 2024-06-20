package np.com.luminoussuwal.babybuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import np.com.luminoussuwal.babybuy.databinding.ActivityLoginBinding
import np.com.luminoussuwal.babybuy.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val TAG = "SignUpActivity"
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.i(TAG, "onCreate: ")

        binding.btnSignUp.setOnClickListener{
            val email = binding.tieEmail.text.toString().trim()
            val password = binding.tiePassword.text.toString().trim()
            val confirmPassword = binding.tieConfirmPassword.text.toString().trim()

            if (email.isNullOrEmpty()) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please input an Email",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please enter a valid Email Address",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password.isNullOrEmpty()) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please input a password",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password.length < 6) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Password should be at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password != confirmPassword) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //local field validation success
                //TODO remote server or local db authentication

                //Storing signup session in SharedPreferences
//                val sharedPreferences = this@SignUpActivity
//                    .applicationContext.getSharedPreferences(
//                        "signup",
//                        Context.MODE_PRIVATE
//                    )
//
//                val sharedPrefEditor = sharedPreferences.edit()
//                sharedPrefEditor.putBoolean("isSignedUp", true)
//                sharedPrefEditor.apply()

                //Navigating to Login Activity
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        binding.tvLogin.setOnClickListener {
            binding.tvLogin.isEnabled = false
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
        binding.tvLogin.isEnabled = true // Re-enable the button in case user returns
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