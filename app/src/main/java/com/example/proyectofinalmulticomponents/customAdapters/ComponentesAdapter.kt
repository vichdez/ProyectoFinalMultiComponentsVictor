package com.example.proyectofinalmulticomponents.customAdapters

import android.view.View
import java.util.*
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.Componente

class ComponentesAdapter(var context:Context, var array:ArrayList<Componente>, var car:Boolean): BaseAdapter() {
    lateinit var ivComp: ImageView
    lateinit var tvNom: TextView
    lateinit var tvDesc: TextView
    lateinit var tvPrecio: TextView
    lateinit var chbFav:CheckBox

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
        view = LayoutInflater.from(context).inflate(R.layout.list_item_componentes, null)

        ivComp = view.findViewById(R.id.ivComponente)
        ivComp.setImageResource(array[position].imagen)

        tvNom = view.findViewById(R.id.tvNomComponente)
        tvNom.text = array[position].nombre

        tvDesc = view.findViewById(R.id.tvDescComponente)
        tvDesc.text = array[position].descripcion

        tvPrecio = view.findViewById(R.id.tvPreComponente)
        tvPrecio.text = array[position].precio.toString()

        chbFav = view.findViewById(R.id.cbItemFavoritos)
        if(!car){
            chbFav.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    true-> {
                        buttonView.buttonTintList = ColorStateList.valueOf(Color.RED)
                        array[position].favorito=true
                    }
                    false->{buttonView.buttonTintList= ColorStateList.valueOf(Color.GRAY)
                        array[position].favorito=false
                    }
                }
            }
        }else{
            chbFav.visibility=INVISIBLE
        }
        when(array[position].favorito){
            true-> chbFav.isChecked=true
            false-> chbFav.isChecked=false
        }
        return view
    }
}