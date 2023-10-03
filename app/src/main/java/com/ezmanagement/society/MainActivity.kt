package com.ezmanagement.society

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.ezmanagement.society.RoundUp.RoundUpActivity
import com.ezmanagement.society.Visitors.AddVisitor
import com.ezmanagement.society.Visitors.CheckedInVisitorList.CheckedInVisitorActivity
import com.ezmanagement.society.common.dialog.DialogListener
import com.ezmanagement.society.common.dialog.DialogUtils
import com.ezmanagement.society.databinding.ActivityMainBinding
import com.ezmanagement.society.fragment.HomeFragment
import com.ezmanagement.society.fragment.ProfileFragment
import com.ezmanagement.society.sharedPreference.SharedPref
import com.ezmanagement.society.utils.RefreshTokenCallBack
import com.ezmanagement.society.utils.RefreshTokenClass
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(), RefreshTokenCallBack, OnClickListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    var sharedPref: SharedPref? = null
    var editor: SharedPreferences.Editor? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this);
        drawerLayout = findViewById(R.id.drawerLayout)
        binding.visitorRelativeLayout.setOnClickListener(this)
        binding.visitorListRelativeLayout.setOnClickListener(this)
        binding.roundUpRelativeLayout.setOnClickListener(this)
        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)


        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()

        actionBarToggle.toolbarNavigationClickListener = OnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(actionBarToggle)
        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    changeFragment(0)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this,ProfileActivity::class.java))

                    true
                }
                R.id.nav_settings -> {
                    changeFragment(2)
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_logout -> {
                    drawerLayout.closeDrawer(navView)
                    DialogUtils.ShowAlert(this,"Alert","Do you want to logout",AppConstants.CONFIRM,AppConstants.CANCEL,object :DialogListener{
                        override fun onPositiveClicked() {
                            sharedPref!!.clearedSharedPref()
                            var intent=Intent(applicationContext,LoginActivity::class.java)
                            gotoActivity(intent,true)
                        }

                        override fun onNegativeClicked() {

                        }

                    })

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

    override fun onResume() {
        super.onResume()

        val login_token = sharedPref!!.getUserData(
            AppConstants.LOGIN_TOKEN,
            String::class.java,
            ""
        )

        var refreshTokenClass: RefreshTokenClass = RefreshTokenClass(this)
        refreshTokenClass.onGetRefreshToken(login_token,this)
    }

    override fun onSucess() {

    }

    override fun onError() {
        Log.d("ononError", "onRefreshTokenExpired")
        var intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onRefreshTokenExpired(errorMessage: String) {
//        startActivity(Intent(this,LoginActivity::class.java))

        Log.d("onRefreshTokenExpired", errorMessage)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.visitorRelativeLayout -> {
                startActivity(Intent(this, AddVisitor::class.java))
            }
            R.id.visitorListRelativeLayout ->{
                startActivity(Intent(this,CheckedInVisitorActivity::class.java))
            }
            R.id.roundUpRelativeLayout->{
                startActivity(Intent(this,RoundUpActivity::class.java))
            }
        }
    }
}