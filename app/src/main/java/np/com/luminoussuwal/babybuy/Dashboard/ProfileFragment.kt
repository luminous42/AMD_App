package np.com.luminoussuwal.babybuy.Dashboard

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import np.com.luminoussuwal.babybuy.LoginActivity
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profilepic: ImageView
    private lateinit var fab: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        profilepic = binding.ivProfileImage // access ImageView
        fab = binding.fab  // access FloatingActionButton

        fab.setOnClickListener {
            fab.setOnClickListener {
                pickImageLauncher.launch("image/*") // Launch the image picker
            }
        }


        auth = FirebaseAuth.getInstance()

        val ivProfileImage = view.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = binding.tvUserName
        val tvUserEmail = binding.tvUserEmail
        val btnLogout = binding.btnLogout

        // Get user details from Firebase Authentication (or your data source)
        val user = auth.currentUser
        if (user != null) {

            tvUserEmail.text = user.email ?: "User Email"

            // Load user image using Glide (or your preferred image loading library)
//            Glide.with(this)
//                .load(user.photoUrl)
//                .placeholder(R.drawable.ic_default_profile)
//                .into(ivProfileImage)
            val db = Firebase.firestore
            val userRef = db.collection("users").document(user.uid)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        tvUserName.text = document.getString("name") ?: "User Name"
                        binding.tvUserPhone.text = document.getString("phone") ?: "User Phone"
                        binding.tvUserAddress.text = document.getString("address") ?: "User Address"
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting document", e)
                }
        }

        btnLogout.setOnClickListener {

            setUpLogOutButton()


        }


        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(false) // Enable the back button
        }
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Assuming 'data' is the Intent result containing the Uri
        val uri: Uri? = data?.data

        // Ensure the URI is not null before setting it
        uri?.let {
            profilepic.setImageURI(it)
        }
    }


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profilepic.setImageURI(it)
                // Optionally, save the image URI to your database or storage
            }
        }


    private fun setUpLogOutButton() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Do you want to logout?")
            .setPositiveButton(
                "Yes",
                DialogInterface.OnClickListener { dialogInterface,
                                                  i ->
                    logout()
                })
            .setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialogInterface,
                                                  i ->
                    dialogInterface.dismiss()

                })
            .show()

    }

    private fun logout() {
        auth.signOut()
        // Navigate to login activity or handle logout as needed

        //Storing login session in SharedPreferences
        val sharedPreferences = requireActivity()
            .applicationContext.getSharedPreferences(
                "login",
                Context.MODE_PRIVATE
            )

        var sharedPrefEditor = sharedPreferences.edit()
        sharedPrefEditor.putBoolean("isLoggedIn", false)
        sharedPrefEditor.apply()

        view?.let {
            Snackbar.make(
                it, "Log Out Successful!",
                Snackbar.LENGTH_SHORT
            ).show()
        }


        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}