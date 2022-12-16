package com.example.proyectofinalmulticomponents.customAdapters

import android.view.View
import java.util.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.proyectofinalmulticomponents.MainActivity
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.ComponenteConImagen
import android.content.Context
import com.example.proyectofinalmulticomponents.CarritoCompra
import com.example.proyectofinalmulticomponents.clases.Componente
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import kotlin.collections.ArrayList

class ComponentesconImagenAdapter(
    var context: Context,
    var array: ArrayList<ComponenteConImagen>, var car: Boolean
) : BaseAdapter() {
    lateinit var ivComp: ImageView
    lateinit var tvNom: TextView
    lateinit var tvDesc: TextView
    lateinit var tvPrecio: TextView
    lateinit var chbFav: CheckBox
    lateinit var tvUnitClients : TextView
    var repes : ArrayList<ComponenteConImagen> = ArrayList()

    override fun getCount(): Int {
        return array.count()
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        view = LayoutInflater.from(context).inflate(R.layout.list_item_componentes_con_imagen, null)

        ivComp = view.findViewById(R.id.ivComponente)
        //String imagen
        val imageRef = array[position].imagen.toString()
        Picasso.with(context).load(imageRef).into(ivComp)

        chbFav = view.findViewById(R.id.cbItemFavoritos)
        if(!car){
            ivComp = view.findViewById(R.id.ivComponente)
            val imageRef = array[position].imagen.toString()
            Picasso.with(context).load(imageRef).into(ivComp)

            tvNom = view.findViewById(R.id.tvNomComponente)
            tvNom.text = array[position].nombre

            tvDesc = view.findViewById(R.id.tvDescComponente)
            tvDesc.text = array[position].descripcion

            tvUnitClients = view.findViewById(R.id.tvUnitClients)
            tvUnitClients.text="" //TODO pon el stock

            tvPrecio = view.findViewById(R.id.tvPreComponente)
            tvPrecio.text = numeroEUR(array[position].precio)+"€"
            chbFav.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    true-> {
                        buttonView.buttonTintList = ColorStateList.valueOf(Color.RED)
                        array[position].favorito=true
                    }
                    false->{buttonView.buttonTintList= ColorStateList.valueOf(Color.GRAY)
                        array[position].favorito=false
                        MainActivity.arrayFavoritos.remove(array[position])
                        MainActivity.adapterComponentes.notifyDataSetChanged()
                    }
                }
            }
        }else{
            chbFav.visibility=INVISIBLE

            ivComp = view.findViewById(R.id.ivComponente)
            val imageRef = array[position].imagen.toString()
            Picasso.with(context).load(imageRef).into(ivComp)

            tvNom = view.findViewById(R.id.tvNomComponente)
            tvNom.text = array[position].nombre

            tvDesc = view.findViewById(R.id.tvDescComponente)
            tvDesc.text = array[position].descripcion

            tvUnitClients = view.findViewById(R.id.tvUnitClients)
            tvUnitClients.text="x${array[position].unidadesCliente}"

            tvPrecio = view.findViewById(R.id.tvPreComponente)
            tvPrecio.text = numeroEUR(array[position].precio)+"€"
            CarritoCompra.adapterCarrito.notifyDataSetChanged()
        }
        when (array[position].favorito) {
            true -> chbFav.isChecked = true
            false -> chbFav.isChecked = false
        }
        return view
    }

    fun numeroEUR(num: Double): String {
        val nf = NumberFormat.getInstance(Locale.GERMAN)
        var string = nf.format(num)
        return string
    }

    fun compare(c1: ComponenteConImagen, c2: ComponenteConImagen): Int {
        var resul = c1.nombre.compareTo(c2.nombre)
        return resul
    }
}