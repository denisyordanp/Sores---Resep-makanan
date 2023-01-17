package com.socialite.sores_food.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.sores_food.R
import com.socialite.sores_food.adapters.viewPagers.PagerAdapter
import com.socialite.sores_food.data.database.entities.FavoritesEntity
import com.socialite.sores_food.databinding.ActivityDetailsBinding
import com.socialite.sores_food.ui.fragments.ingredients.IngredientsFragment
import com.socialite.sores_food.ui.fragments.instructions.InstructionsFragment
import com.socialite.sores_food.ui.fragments.overview.OverviewFragment
import com.socialite.sores_food.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    private lateinit var menuItem: MenuItem

    companion object {
        const val RESULT_BUNDLE_KEY = "resultBundle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setActivityView()
        setUpViewPager()
    }

    private fun setActivityView() {
        setSupportActionBar(_binding.toolbar)
        _binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorite_menu)
        checkFavoritesRecipe(menuItem)
        return true
    }

    private fun checkFavoritesRecipe(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoritesEntities ->
            try {
                for (favorite in favoritesEntities) {
                    if (favorite.result.id == args.result.id) {
                        changeFavoriteIcon(menuItem, R.color.yellow)
                        recipeSaved = true
                        savedRecipeId = favorite.id
                        return@observe
                    }
                }
            } catch (e: Exception) {
                Log.e("DetailsActivity", e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.save_to_favorite_menu -> {
                if (recipeSaved) {
                    removeResult(item)
                } else {
                    saveResult(item)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpViewPager() {
        val myBundle = bundle
        val pagerAdapter = PagerAdapter(
            myBundle,
            fragments,
            this
        )

        _binding.detailsViewPager2.apply {
            adapter = pagerAdapter
        }
        TabLayoutMediator(_binding.tabLayout, _binding.detailsViewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private val fragments: ArrayList<Fragment>
        get() {
            val fragments = ArrayList<Fragment>()
            fragments.add(OverviewFragment())
            fragments.add(IngredientsFragment())
            fragments.add(InstructionsFragment())
            return fragments
        }

    private val titles: ArrayList<String>
        get() {
            val titles = ArrayList<String>()
            titles.add("Overview")
            titles.add("Ingredients")
            titles.add("Instructions")
            return titles
        }

    private val bundle: Bundle
        get() {
            val resultBundle = Bundle()
            resultBundle.putParcelable(RESULT_BUNDLE_KEY, args.result)
            return resultBundle
        }

    private fun removeResult(item: MenuItem) {
        removeFromDatabase()
        changeFavoriteIcon(item, R.color.white)
        showSnackBar("Remove saved recipe.")
    }

    private fun saveResult(item: MenuItem) {
        saveToDatabase()
        changeFavoriteIcon(item, R.color.yellow)
        showSnackBar("Recipe saved.")
    }

    private fun saveToDatabase() {
        val favoritesEntity =
            FavoritesEntity(args.result)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        recipeSaved = true
    }

    private fun removeFromDatabase() {
        val favoritesEntity =
            FavoritesEntity(
                savedRecipeId,
                args.result
            )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        recipeSaved = false
    }

    private fun changeFavoriteIcon(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            _binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        changeFavoriteIcon(menuItem, R.color.white)
    }
}
