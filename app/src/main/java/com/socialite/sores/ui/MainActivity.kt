package com.socialite.sores.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.socialite.sores.R
import com.socialite.sores.util.NetworkListener
import com.socialite.sores.viewModels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class  MainActivity : AppCompatActivity() {

    private val recipesViewModel: RecipesViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SoresResepMakanan)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)

        setUpNavigation()
        setNetworkStatusListener()
    }

    private fun setUpNavigation() {
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, getAppBarConfigurations())
    }

    private fun setNetworkStatusListener() {
        recipesViewModel.readIsBackOnline.observe(this) {
            recipesViewModel.isBackOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(this@MainActivity)
                .collect { status ->
                    recipesViewModel.isNetworkAvailable = status
                    recipesViewModel.showNetworkStatus()
                }
        }
    }

    private fun getAppBarConfigurations(): AppBarConfiguration {
        val sets = setOf(R.id.recipesFragment, R.id.favoriteRecipesFragment, R.id.foodJokeFragment)
        return AppBarConfiguration(sets)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}