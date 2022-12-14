package com.example.proyectofinalmulticomponents

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.*
import androidx.core.view.get
import com.example.proyectofinalmulticomponents.clases.Componente
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.example.proyectofinalmulticomponents.customAdapters.ComponentesAdapter
import com.example.proyectofinalmulticomponents.login.Login

class MainActivity : AppCompatActivity() {

    private lateinit var lvComponentes: ListView
    private lateinit var cbFavoritos: CheckBox
    private lateinit var sFiltros: Switch
    private lateinit var llFiltros: LinearLayout
    private lateinit var cbRatones: CheckBox
    private lateinit var cbTeclados: CheckBox
    private lateinit var cbMonitores: CheckBox
    private lateinit var cbProcesadores: CheckBox
    private lateinit var cbPlacasBase: CheckBox
    private lateinit var cbRam: CheckBox
    private lateinit var cbPortatiles: CheckBox
    private lateinit var cbTorres: CheckBox
    private lateinit var cbFuentesAlim: CheckBox
    var  filtroarray :ArrayList<Componente> = ArrayList()
    private var arrayFav:List<Componente> = listOf()

    companion object{
        var componentesBBDD: ArrayList<Componente> = ArrayList()
        lateinit var adapterCom: ComponentesAdapter
        lateinit var user: Usuario
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        Login.user = Usuario("demo","demo","demo@gmail.com","password",671351162,"Madrid","Daganzo","C/ Carmen Amaya n22")
        Login.user.setPassword("demodemo")

        user = Login.user

        lvComponentes = findViewById(R.id.lyComponentesHome)
        cbFavoritos = findViewById(R.id.cbFavoritos)
        sFiltros = findViewById(R.id.sFiltros)
        llFiltros = findViewById(R.id.llFiltros)
        cbRatones = findViewById(R.id.chbRatones)
        cbTeclados = findViewById(R.id.chbTeclados)
        cbMonitores = findViewById(R.id.chbMonitores)
        cbProcesadores= findViewById(R.id.chbProcesadores)
        cbPlacasBase= findViewById(R.id.chbPlacasBase)
        cbRam= findViewById(R.id.chbRam)
        cbPortatiles= findViewById(R.id.chbPortatiles)
        cbTorres= findViewById(R.id.chbTorres)
        cbFuentesAlim= findViewById(R.id.chbFAliment)

        llFiltros.visibility= GONE
        if(componentesBBDD.isEmpty()){
            var c:Componente = Componente("Ratón de Hibertronics","Raton con luces de colorines y cable USB de 2 m !!",25.99,R.drawable.corazon_relleno)
            var c2:Componente = Componente("Pantalla de Hibertronics","Pantalla con luces de colorines de 17 pulgadas ",17.01,R.drawable.corazon_relleno)
            var c3:Componente = Componente("Torre de Hibertronics","Hardware con luces de colorines y 480 GB de memoria !!",60.00,R.drawable.corazon_relleno)
            var c4:Componente = Componente("Teclado de Hibertronics","Mesita con luces de colorines y hueco para tus refrescos y juegos favoritos!!",37.45,R.drawable.corazon_relleno)

            c.aniadirTag("Raton")
            c2.aniadirTag("Monitor")
            c3.aniadirTag("Torre")
            c4.aniadirTag("Teclado")

            componentesBBDD.add(c)
            componentesBBDD.add(c2)
            componentesBBDD.add(c3)
            componentesBBDD.add(c4)
        }
        adapterCom = ComponentesAdapter(this,componentesBBDD,false)
        lvComponentes.adapter = adapterCom


        lvComponentes.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _->

            CarritoCompra.componentesBBDD.add(componentesBBDD[position])
            componentesBBDD.remove(componentesBBDD[position])
            adapterCom.notifyDataSetChanged()
        }

        sFiltros.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> llFiltros.visibility= VISIBLE

                false ->llFiltros.visibility= GONE
            }
        }

        cbFavoritos.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true->{buttonView.buttonTintList= ColorStateList.valueOf(Color.RED)
                    arrayFav = componentesBBDD.filter { componente ->
                        componente.favorito
                    }
                    adapterCom= ComponentesAdapter(this,arrayFav as java.util.ArrayList<Componente>,false)
                    lvComponentes.adapter= adapterCom
                }
                false->{buttonView.buttonTintList= ColorStateList.valueOf(Color.GRAY)
                    if(!cbRatones.isChecked && !cbTeclados.isChecked && !cbMonitores.isChecked
                        && !cbRam.isChecked && !cbProcesadores.isChecked && !cbTorres.isChecked
                        && !cbPlacasBase.isChecked && !cbPortatiles.isChecked && !cbFuentesAlim.isChecked
                        && !cbFavoritos.isChecked){
                        adapterCom= ComponentesAdapter(this, componentesBBDD,false)
                        lvComponentes.adapter = adapterCom
                    }else{
                        adapterCom= ComponentesAdapter(this,filtroarray,false)
                        lvComponentes.adapter= adapterCom
                    }
                }
            }
        }


    }

    fun checked(view: View){
        var checked = view as CheckBox
        compruebafiltro(cbRatones,"Raton")
        compruebafiltro(cbTeclados,"Teclado")
        compruebafiltro(cbMonitores,"Monitor")
        compruebafiltro(cbRam,"Ram")
        compruebafiltro(cbProcesadores,"Procesador")
        compruebafiltro(cbTorres,"Torre")
        compruebafiltro(cbPlacasBase,"PlacaBase")
        compruebafiltro(cbPortatiles,"Portatil")
        compruebafiltro(cbFuentesAlim,"FuenteAlimentacion")

        if(!cbRatones.isChecked && !cbTeclados.isChecked && !cbMonitores.isChecked
            && !cbRam.isChecked && !cbProcesadores.isChecked && !cbTorres.isChecked
            && !cbPlacasBase.isChecked && !cbPortatiles.isChecked && !cbFuentesAlim.isChecked
            && !cbFavoritos.isChecked){
            adapterCom= ComponentesAdapter(this, componentesBBDD,false)
            lvComponentes.adapter = adapterCom
        }else if (!cbRatones.isChecked && !cbTeclados.isChecked && !cbMonitores.isChecked
            && !cbRam.isChecked && !cbProcesadores.isChecked && !cbTorres.isChecked
            && !cbPlacasBase.isChecked && !cbPortatiles.isChecked && !cbFuentesAlim.isChecked
            && cbFavoritos.isChecked){adapterCom= ComponentesAdapter(this,arrayFav as java.util.ArrayList<Componente>,false)
            lvComponentes.adapter= adapterCom
        }else{
            adapterCom= ComponentesAdapter(this,filtroarray,false)
            lvComponentes.adapter= adapterCom
        }
    }

    fun compruebafiltro(ch:CheckBox,s:String){
        if (ch.isChecked) {
            for(com:Componente in componentesBBDD){
                if(com.tags.contains(s)){
                    if(cbFavoritos.isChecked && com.favorito) {
                        if (filtroarray.contains(com)) {

                        } else {
                            filtroarray.add(com)
                        }
                    }else if(cbFavoritos.isChecked && !com.favorito){

                    }else{
                        if (filtroarray.contains(com)) {

                        } else {
                            filtroarray.add(com)
                        }
                    }
                }
            }
        }else{
            for(com:Componente in componentesBBDD) {
                if (filtroarray.contains(com) && com.tags.contains(s)) {
                    filtroarray.remove(com)
                    adapterCom.notifyDataSetChanged()
                }
            }
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

            }
            R.id.CarritoItem ->{
                i = Intent(this,CarritoCompra::class.java)
                startActivity(i)

            }
            R.id.FacturasItem ->{
                i = Intent(this,Facturas::class.java)
                startActivity(i)

            }
            R.id.UsuarioItem ->{
                i = Intent(this,UserConfig::class.java)
                startActivity(i)

            }

        }
        return super.onOptionsItemSelected(item)
    }
}