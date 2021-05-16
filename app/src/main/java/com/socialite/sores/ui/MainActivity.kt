package com.socialite.sores.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.socialite.sores.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class  MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SoresResepMakanan)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)

        setUpNavigation()
    }

    private fun setUpNavigation(){
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, getAppBarConfigurations())
    }

    private fun getAppBarConfigurations(): AppBarConfiguration {
        val sets = setOf(R.id.recipesFragment, R.id.favoriteRecipesFragment, R.id.foodJokeFragment)
        return AppBarConfiguration(sets)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}