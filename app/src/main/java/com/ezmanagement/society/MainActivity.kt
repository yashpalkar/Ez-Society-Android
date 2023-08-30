package com.ezmanagement.society

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.ezmanagement.society.databinding.ActivityMainBinding
import com.ezmanagement.society.fragment.HomeFragment
import com.ezmanagement.society.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)

        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()


        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    changeFragment(0)
                    Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    changeFragment(1)
                    Toast.makeText(this, "People", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    changeFragment(2)
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


    fun changeFragment(position: Int) {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = HomeFragment()
            1 -> fragment = ProfileFragment()
            2 -> fragment = HomeFragment()
            else -> {}
        }

        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()
//            mDrawerList.setItemChecked(position, true)
//            mDrawerList.setSelection(position)
//            setTitle(mNavigationDrawerItemTitles.get(position))
            drawerLayout.closeDrawer(navView)
        } else {
            Log.e("MainActivity", "Error in creating fragment")
        }
    }
}