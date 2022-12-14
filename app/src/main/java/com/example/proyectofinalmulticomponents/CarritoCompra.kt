package com.example.proyectofinalmulticomponents

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4
import com.example.proyectofinalmulticomponents.clases.Factura
import com.example.proyectofinalmulticomponents.clases.Componente
import com.example.proyectofinalmulticomponents.customAdapters.ComponentesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class CarritoCompra : AppCompatActivity() {

    private lateinit var lvComponentesCarrito : ListView
    private lateinit var alert : MaterialAlertDialogBuilder
    private lateinit var btnFloat: FloatingActionButton

    companion object{
        var componentesBBDD: ArrayList<Componente> = ArrayList()
        lateinit var adapterCarrito:ComponentesAdapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito_compra)

        lvComponentesCarrito= findViewById(R.id.lyComponentesCarrito)

        btnFloat= findViewById(R.id.fabPagar)

        adapterCarrito=ComponentesAdapter(this, componentesBBDD,true)

        lvComponentesCarrito.adapter= adapterCarrito

        if(componentesBBDD.isEmpty()){
            btnFloat.visibility=INVISIBLE
        }else{
            btnFloat.visibility=VISIBLE
        }
        btnFloat.setOnClickListener {
            alert = MaterialAlertDialogBuilder(this)
            alert.setTitle("Total:")
            alert.setMessage("Total a pagar :  ${suma()}")
            alert.setPositiveButton("Pagar") { _, _ ->

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateString = simpleDateFormat.format(System.currentTimeMillis())

                var fecha:String = String.format("Date: %s", dateString)
                var array:ArrayList<Componente> = ArrayList()
                for (com:Componente in componentesBBDD){
                    array.add(com)
                }
                var fact = Factura(MainActivity.user, fecha,array )

                Facturas.facturaBBDD.add(fact)
                componentesBBDD.clear()
                adapterCarrito.notifyDataSetChanged()

            }
            alert.setNegativeButton("Cancelar"){_,_ ->

            }
            alert.show()

        }

        lvComponentesCarrito.setOnItemClickListener { parent, view, position, id ->
            alert= MaterialAlertDialogBuilder(this)
            alert.setTitle("Sacar del Carrito")
            alert.setMessage("Quiere quitar el articulo ${componentesBBDD[position].nombre} del carrito?")
            alert.setPositiveButton("Quitar") { _, _ ->
                MainActivity.componentesBBDD.add(componentesBBDD[position])
                componentesBBDD.removeAt(position)
                MainActivity.adapterCom.notifyDataSetChanged()
                adapterCarrito.notifyDataSetChanged()
                if(componentesBBDD.isEmpty()){
                    btnFloat.visibility=INVISIBLE
                }else{
                    btnFloat.visibility=VISIBLE
                }
            }
            alert.setNegativeButton("Cancelar"){_,_ ->

            }
            alert.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i: Intent
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

    fun suma():Double{
        var suma =0.0
        for (c:Componente in componentesBBDD){
            suma += c.precio
        }
        return suma
    }
}