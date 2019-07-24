package com.education.dynamic

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.education.dynamic.fragments.ImageFragment
import com.education.dynamic.fragments.MessageFragment
import com.education.dynamic.fragments.WebFragment
import com.education.dynamic.interfaces.OnFail
import com.education.dynamic.interfaces.OnLoad
import com.education.dynamic.model.DynamicItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), OnFail, OnLoad,
    MessageFragment.OnMessageFragmentListener, ImageFragment.OnImageFragmentListener,
    WebFragment.OnWebFragmentListener {

    private lateinit var viewModel: ItemViewModel
    private lateinit var agent: Agent
    private lateinit var navView: NavigationView
    private var listener : NavigationView.OnNavigationItemSelectedListener? = null
    private var isLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        lifecycle.addObserver(DataLoader)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        agent = Agent(this)
        viewModel.items.observe(this, Observer {
            val mapMenu = HashMap<MenuItem, DynamicItem>()
            navView.menu.clear()
            for (item in it) {
                val icon = when (item.function) {
                    "text" -> {R.drawable.ic_message}
                    "image" -> {R.drawable.ic_image}
                    "url" -> {R.drawable.ic_url}
                    else -> {R.drawable.ic_error}
                }
                val i = navView.menu.add(item.name)
                mapMenu[i] = item
                i.setIcon(icon)
            }
            listener = object : NavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    for(i in 0..navView.menu.size()-1) navView.menu.getItem(i).isChecked = false
                    item.isChecked = true
                    val dyn = mapMenu[item]
                    toolbar.title = dyn!!.name
                    agent.showContent(dyn)
                    return true
                }
            }
            navView.setNavigationItemSelectedListener(listener)
            val start = if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 0 else navView.menu.size()-1
            navView.menu.getItem(start).isChecked = true
            listener?.onNavigationItemSelected(navView.menu.getItem(start))
        })
        agent.setOnErrorListener(this)
        DataLoader.setOnErrorListener(this)
        DataLoader.setOnLoadListener(this)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            listener?.onNavigationItemSelected(navView.menu.getItem(0))
        }else{
            listener?.onNavigationItemSelected(navView.menu.getItem(navView.menu.size()-1))
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onLoad() {
        pgLoader.show()
    }

    override fun onLoaded() {
        pgLoader.hide()
    }

    override fun onFail(ex: Exception) {
        pgLoader.hide()
        val snackbar = Snackbar.make(findViewById(R.id.llContainer), ex.message.toString(), Snackbar.LENGTH_SHORT)
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        snackbar.show()
    }

}
