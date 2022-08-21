package ru.netology.nerecipe.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.nerecipe.FeedFragment
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.AppActivityBinding
import ru.netology.nerecipe.databinding.FragmentFeedBinding

class AppActivity : AppCompatActivity(R.layout.app_activity){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // val binding = AppActivityBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        val  bnvMain = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bnvMain.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
                "fragment_edit_recipe" -> bnvMain.visibility = View.GONE
                "SingleRecipe" -> bnvMain.visibility = View.GONE
                "EditStage" -> bnvMain.visibility = View.GONE
                else -> bnvMain.visibility = View.VISIBLE
            }
        }

    }
}