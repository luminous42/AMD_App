package np.com.luminoussuwal.babybuy

import CloudDrawable
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import np.com.luminoussuwal.babybuy.Dashboard.DashboardActivity
import np.com.luminoussuwal.babybuy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        view.background = CloudDrawable()

        FirebaseApp.initializeApp(this.applicationContext)
        val auth = Firebase.auth

        Log.i(TAG, "onCreate: ")

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isEnabled = false
            binding.btnLogin.text = ""
            binding.progressBar.visibility = View.VISIBLE

            val email = binding.tieEmail.text.toString().trim()
            val password = binding.tiePassword.text.toString().trim()
            if (email.isNullOrEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please input an Email",
                    Toast.LENGTH_SHORT
                )
                    .show()

                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Log In"
                return@setOnClickListener

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter a valid Email Address",
                    Toast.LENGTH_SHORT
                )
                    .show()

                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Log In"
                return@setOnClickListener

            } else if (password.isNullOrEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please input a password",
                    Toast.LENGTH_SHORT
                )
                    .show()

                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Log In"
                return@setOnClickListener

            } else if (password.matches(Regex("^[0-9]{6}$"))) {
                Toast.makeText(
                    this@LoginActivity,
                    "Password should be atleast be 6 digits",
                    Toast.LENGTH_SHORT
                )
                    .show()

                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Log In"
                return@setOnClickListener

            } else {




                //local field validation success
                //TODO remote server or local db authentication
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser



                            //Storing login session in SharedPreferences
                            val sharedPreferences = this@LoginActivity
                                .applicationContext.getSharedPreferences(
                                    "login",
                                    Context.MODE_PRIVATE
                                )

                            var sharedPrefEditor = sharedPreferences.edit()
                            sharedPrefEditor.putBoolean("isLoggedIn", true)
                            sharedPrefEditor.apply()

                            binding.progressBar.visibility = View.GONE
                            binding.btnLogin.isEnabled = true

                            Snackbar.make(
                                view, "Login Successful!",
                                Snackbar.LENGTH_SHORT
                            ).show()

//                            Handler(Looper.getMainLooper()).postDelayed({
//                            //Navigating to Dashboard Activity

                            val intent = Intent(
                                this@LoginActivity,
                                DashboardActivity::class.java
                            )
                            intent.putExtra(AppConstants.KEY_EMAIL, email)

                            startActivity(intent)
                            finish()

//                            }, 600)

                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.btnLogin.isEnabled = true
                            binding.btnLogin.text = "Log In"

                            Snackbar.make(
                                view,
                                "Invalid Email or Password",
                                Snackbar.LENGTH_SHORT
                            ).show()

                        }
                    }
            }
        }
        binding.tvSignup.setOnClickListener {
            binding.tvSignup.isEnabled = false
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            //intent.putExtra(AppConstants.KEY_EMAIL, email)
            //intent.putExtra(AppConstants.KEY_TEST_DATA, testData)
            startActivity(intent)
            finish()
        }

//        binding.ivShowHidePassword.setOnClickListener {
//            isPasswordVisible = !isPasswordVisible
//            togglePasswordVisibility(
//                binding.tiePassword,
//                binding.ivShowHidePassword,
//                isPasswordVisible
//            )
//        }
    }
//    private fun togglePasswordVisibility(editText: TextInputEditText, imageView: ImageView, isVisible: Boolean) {
//        if (isVisible) {
//            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//            imageView.setImageResource(R.drawable.ic_hide_password) // Change to hide password icon
//        } else {
//            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//            imageView.setImageResource(R.drawable.ic_show_password) // Change to show password icon
//        }
//        // Move the cursor to the end
//        editText.setSelection(editText.text?.length ?: 0)
//    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
        binding.tvSignup.isEnabled = true
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