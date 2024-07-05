package np.com.luminoussuwal.babybuy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import np.com.luminoussuwal.babybuy.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val TAG = "SignUpActivity"
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        auth = FirebaseAuth.getInstance()

        Log.i(TAG, "onCreate: ")

        binding.btnSignUp.setOnClickListener {
            val email = binding.tieEmail.text.toString().trim()
            val password = binding.tiePassword.text.toString().trim()
            val confirmPassword = binding.tieConfirmPassword.text.toString().trim()
            val name = binding.tieUsername.text.toString().trim()
            val phone = binding.tiePhone.text.toString().trim()
            val address = binding.tieAddress.text.toString().trim()

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

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            saveAdditionalUserDetails(user?.uid, name, phone, address)

                            val rootView =
                                findViewById<View>(android.R.id.content) // Get the root view of your layout
                            Snackbar.make(rootView, "Sign Up Successful!", Snackbar.LENGTH_SHORT)
                                .show()

                            //Navigating to Login Activity
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 1000)
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Email already registered.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

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


            }

        }
        binding.tvLogin.setOnClickListener {
            binding.tvLogin.isEnabled = false
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveAdditionalUserDetails(
        userId: String?,
        name: String,
        phone: String,
        address: String
    ) {
        if (userId != null) {
            val db = Firebase.firestore
            val userRef = db.collection("users").document(userId)
            val userData = hashMapOf(
                "name" to name,
                "phone" to phone,
                "address" to address
            )
            userRef.set(userData)
                .addOnSuccessListener {
                    Log.d(TAG, "User details saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error saving user details", e)
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