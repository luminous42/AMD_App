package np.com.luminoussuwal.babybuy.Dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import np.com.luminoussuwal.babybuy.LoginActivity
import np.com.luminoussuwal.babybuy.R





class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        val ivProfileImage = view.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // Get user details from Firebase Authentication (or your data source)
        val user = auth.currentUser
        if (user != null) {
            tvUserName.text = user.displayName ?: "User Name"
            tvUserEmail.text = user.email ?: "User Email"

            // Load user image using Glide (or your preferred image loading library)
//            Glide.with(this)
//                .load(user.photoUrl)
//                .placeholder(R.drawable.ic_default_profile)
//                .into(ivProfileImage)
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            // Navigate to login activity or handle logout as needed
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "My Profile"
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        return view
    }
}