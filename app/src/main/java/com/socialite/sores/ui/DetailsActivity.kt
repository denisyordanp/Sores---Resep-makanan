package com.socialite.sores.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.socialite.sores.R
import com.socialite.sores.adapters.viewPagers.PagerAdapter
import com.socialite.sores.ui.fragments.ingredients.IngredientsFragment
import com.socialite.sores.ui.fragments.instructions.InstructionsFragment
import com.socialite.sores.ui.fragments.overview.OverviewFragment
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    companion object{
        const val RESULT_BUNDLE_KEY = "resultBundle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setActivityView()
        setUpViewPager()
    }

    private fun setActivityView() {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpViewPager() {
        val myBundle = bundle
        val adapter = PagerAdapter(
            myBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
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
}