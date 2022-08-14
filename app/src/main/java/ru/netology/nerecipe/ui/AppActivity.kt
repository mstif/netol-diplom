package ru.netology.nerecipe.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

    }
}