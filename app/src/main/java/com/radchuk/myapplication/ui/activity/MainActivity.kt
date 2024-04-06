package com.radchuk.myapplication.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.radchuk.myapplication.R
import com.radchuk.myapplication.databinding.ActivityMainBinding
import com.radchuk.myapplication.ui.fragments.CategotiesCarFragment
import com.radchuk.myapplication.ui.fragments.DriverFragment
import com.radchuk.myapplication.ui.fragments.MainFragment
import com.radchuk.myapplication.ui.fragments.VehicleFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.toolbar.title = "Головна"
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MainFragment())
            .commit()
        /*binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_main -> {
                    binding.toolbar.title = "Головна"
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MainFragment())
                        .commit()
                    return@setNavigationItemSelectedListener true
                }
                R.id.menu_drivers -> {
                    binding.toolbar.title = "Водії"
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, DriverFragment())
                        .commit()
                    return@setNavigationItemSelectedListener true
                }
                R.id.menu_cars -> {
                    binding.toolbar.title = "Транспорт"
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, VehicleFragment())
                        .commit()
                    return@setNavigationItemSelectedListener true
                }
                R.id.menu_categories_cars ->{
                    binding.toolbar.title = "Категорії"
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CategotiesCarFragment())
                        .commit()
                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener false
            }
        }

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}