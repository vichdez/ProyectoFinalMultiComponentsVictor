package com.example.proyectofinalmulticomponents

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.proyectofinalmulticomponents.clases.Componente
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.example.proyectofinalmulticomponents.customAdapters.ComponentesAdapter
import com.example.proyectofinalmulticomponents.databinding.ActivityNavigationDrawerBinding
import com.example.proyectofinalmulticomponents.databinding.NavHeaderNavigationDrawerBinding
import com.example.proyectofinalmulticomponents.login.Login
import com.google.firebase.auth.FirebaseAuth

class NavigationDrawerActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var menu: Menu

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding2: NavHeaderNavigationDrawerBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var userNav: Usuario
    private lateinit var navView: NavigationView

    private lateinit var dl: DrawerLayout

    internal lateinit var nombreNav: TextView
    internal lateinit var mailNav: TextView

    companion object {
        lateinit var user: Usuario
        lateinit var nomNav: String
        lateinit var correoNav: String
        lateinit var binding: ActivityNavigationDrawerBinding
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        binding2 = NavHeaderNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dl = binding.drawerLayout
        navView = binding.navView

        nomNav = binding2.navHeadernombre.text.toString()
        correoNav = binding2.navHeadermail.text.toString()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainActivity, R.id.favoritos, R.id.carritoCompra, R.id.facturas,
                R.id.userConfig, R.id.acercaDe, R.id.politicaDePrivacidad, R.id.login
            ), dl
        )
//TOOLBAR
        toggle = ActionBarDrawerToggle(
            this,
            dl,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)
        supportActionBar!!.title = ""
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        dl.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener(this)

  /*
val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

   */

//MENÚ




    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //  return super.onContextItemSelected(item)
        var id: Int = item.itemId
        if (id == R.id.nav_main) {
            var i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else if (id == R.id.nav_favoritos) {
            var i: Intent = Intent(this, Favoritos::class.java)
            startActivity(i)
        } else if (id == R.id.nav_carritoCompra) {
            var i: Intent = Intent(this, CarritoCompra::class.java)
            startActivity(i)
        } else if (id == R.id.nav_facturas) {
            var i: Intent = Intent(this, Facturas::class.java)
            startActivity(i)
        } else if (id == R.id.nav_userConfig) {
            var i: Intent = Intent(this, UserConfig::class.java)
            startActivity(i)
        } else if (id == R.id.nav_acercaDe) {
            var i: Intent = Intent(this, AcercaDe::class.java)
            startActivity(i)
        } else if (id == R.id.nav_politicaDePrivacidad) {
            var i: Intent = Intent(this, PoliticaDePrivacidad::class.java)
            startActivity(i)
        } else if (id == R.id.nav_login) {
            var i: Intent = Intent(this, Login::class.java)
            startActivity(i)
            //LOGOUT
            FirebaseAuth.getInstance().signOut()
        }

        dl.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}