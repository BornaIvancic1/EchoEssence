package hr.algebra.echoessence

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.echoessence.dao.LibraryRepository
import hr.algebra.echoessence.databinding.ActivityHomeBinding
import hr.algebra.echoessence.model.Library
import hr.algebra.echoessence.ui.library.LibraryFragment
import hr.algebra.echoessence.ui.me.MeViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    private lateinit var libraryRepository: LibraryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        libraryRepository = LibraryRepository(this)

        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")
        val photoUrl = auth.currentUser?.photoUrl.toString()

        val meViewModel = ViewModelProvider(this)[MeViewModel::class.java]
        meViewModel.email.value = email
        meViewModel.name.value = displayName
        meViewModel.photoUrl.value = photoUrl

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_me
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val libraryFragment = navHostFragment?.childFragmentManager?.fragments?.find { it is LibraryFragment } as? LibraryFragment
        libraryFragment?.onUserLoggedIn()
    }

    override fun onResume() {
        super.onResume()
        loadLibraryData()
    }

    private fun loadLibraryData() {
        val userId=getCurrentUserId()
        val libraryEntries = userId?.let { libraryRepository.getLibraryEntriesByUserId(it) }
        if (libraryEntries != null) {
            updateLibraryUI(libraryEntries)
        }
    }

    private fun updateLibraryUI(libraryEntries: List<Library>) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val libraryFragment = navHostFragment?.childFragmentManager?.fragments?.find { it is LibraryFragment } as? LibraryFragment

        libraryFragment?.updateLibraryData(libraryEntries)
    }

    private fun getCurrentUserId(): Int? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) // Use 'this' or 'applicationContext'
        return sharedPreferences.getInt("userId", -1).takeIf { it != -1 }
    }
}