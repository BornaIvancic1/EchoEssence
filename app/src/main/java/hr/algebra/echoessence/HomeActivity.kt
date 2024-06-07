package hr.algebra.echoessence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.echoessence.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")

        val bundle = Bundle()
        bundle.putString("email", email)
        bundle.putString("name", displayName)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.setGraph(R.navigation.mobile_navigation, bundle)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_me
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        findViewById<Button>(R.id.signOutBtn).setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
