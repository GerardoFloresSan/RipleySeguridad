package com.example.myfirstapp.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.utils.enableError
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout

abstract class BaseActivity : AppCompatActivity() {
    protected var toolbar: Toolbar? = null
    protected var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    protected var navigationView: NavigationView? = null
    protected var bottomNavigationView: BottomNavigationView? = null
    protected val locationTrackingService by lazy {
        RipleyApplication.locationTrackingService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getView())
        onCreate()
        RipleyApplication.addActivity(this)
        Log.d("onCreate", "____________________________________");
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy", "____________________________________");
        RipleyApplication.removeActivity(this)
    }

    @SuppressLint("PrivateResource")
    protected fun setSupportActionBar(title: String, colorS: Int): ActionBar {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val color = ContextCompat.getColor(this, colorS)

        toolbar?.setTitleTextColor(color)

        val drawable = ContextCompat.getDrawable(this, R.drawable.arrow_left_white_24dp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(drawable)
        supportActionBar!!.title = title

        return supportActionBar!!
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun setupDrawerContentBottom(listener: SimpleNavigationItemSelectedListener) {
        bottomNavigationView = findViewById(R.id.nav_view)

        bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->
            listener.onNavigationItemSelected(item)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            if (drawerToggle != null) {
                return drawerToggle!!.onOptionsItemSelected(item)
            } else onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (drawerToggle != null)
            drawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (drawerToggle != null)
            drawerToggle!!.onConfigurationChanged(newConfig)
    }

    @SuppressLint("RtlHardcoded")
    override fun onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout!!.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout!!.closeDrawers()
                return
            }
        }
        super.onBackPressed()
    }

    abstract fun getView(): Int

    open fun onCreate() {}

    interface SimpleNavigationItemSelectedListener {
        fun onNavigationItemSelected(item: MenuItem)
    }

    fun activeAllWrappers() {
        activeAllWrappers(findViewById(android.R.id.content))
    }

    private fun activeAllWrappers(parent: ViewGroup) {
        for (i in 0..parent.childCount) {

            val view = parent.getChildAt(i)

            if (view is TextInputLayout) {
                view.enableError()
            } else if (view is ViewGroup) {
                activeAllWrappers(parent.getChildAt(i) as ViewGroup)
            }
        }
    }

    fun hideAllWrappers() {
        activeAllWrappers(findViewById(android.R.id.content))
    }

}