package com.example.proyectofinalmulticomponents

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.example.proyectofinalmulticomponents.clases.Factura
import com.example.proyectofinalmulticomponents.customAdapters.FacturasAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class Facturas : AppCompatActivity() {
    private lateinit var lvFacturas : ListView

    companion object{
        var facturaBBDD: ArrayList<Factura> = ArrayList()
        lateinit var adapterFactura:  FacturasAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facturas)

        lvFacturas = findViewById(R.id.lyFacturas)

        adapterFactura= FacturasAdapter(this, facturaBBDD)

        lvFacturas.adapter = adapterFactura

        adapterFactura.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i: Intent
        when(item.itemId){
            R.id.HomeItem ->{
                finish()
            }
            R.id.CarritoItem ->{
                i = Intent(this,CarritoCompra::class.java)
                startActivity(i)
                finish()
            }
            R.id.FacturasItem ->{
                i = Intent(this,Facturas::class.java)
                startActivity(i)
                finish()
            }
            R.id.UsuarioItem ->{
                i = Intent(this,UserConfig::class.java)
                startActivity(i)
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}