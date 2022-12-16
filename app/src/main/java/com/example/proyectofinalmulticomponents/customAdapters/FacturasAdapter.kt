package com.example.proyectofinalmulticomponents.customAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.proyectofinalmulticomponents.clases.Factura
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.Componente
import com.example.proyectofinalmulticomponents.clases.ComponenteConImagen
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class FacturasAdapter(var context: Context, var array: ArrayList<Factura>): BaseAdapter(){
    lateinit var ivLog: ImageView
    lateinit var tvFech: TextView
    lateinit var tvNom: TextView
    lateinit var tvEma: TextView
    lateinit var tvTel:TextView
    lateinit var tvPro:TextView
    lateinit var tvCiu:TextView
    lateinit var tvDir:TextView
    lateinit var tvLista:TextView
    lateinit var tvPrecio:TextView
    lateinit var tvSubTotalPrecio:TextView
    lateinit var tvIvaPrecio:TextView
    lateinit var tvTotalTotal:TextView
    lateinit var tvTotalPrecio:TextView
    lateinit var tvUnidad: TextView

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
        view = LayoutInflater.from(context).inflate(R.layout.list_item_facturas, null)


        tvFech = view.findViewById(R.id.tvFecha)
        tvFech.text = "${array[position].fecha}"

        tvNom = view.findViewById(R.id.tvfNombre)
        tvNom.text = "${array[position].user.nombre} ${array[position].user.apellidos}"

        tvEma = view.findViewById(R.id.tvfEmail)
        tvEma.text = array[position].user.email

        tvTel = view.findViewById(R.id.tvfTelefono)
        tvTel.text = array[position].user.telefono.toString()

        tvPro = view.findViewById(R.id.tvfProvincia)
        tvPro.text = array[position].user.provincia

        tvCiu = view.findViewById(R.id.tvfCiudad)
        tvCiu.text = array[position].user.ciudad

        tvDir = view.findViewById(R.id.tvfDireccion)
        tvDir.text = array[position].user.direccion

        tvLista = view.findViewById(R.id.tvfLista)

        tvPrecio = view.findViewById(R.id.tvPrecio)

        tvUnidad = view.findViewById(R.id.tvUnidad)

        tvSubTotalPrecio = view.findViewById(R.id.tvSubTotalPrecio)

        tvIvaPrecio = view.findViewById(R.id.tvIvaPrecio)

        var list =""
        var price =""
        var uni = ""
        var fac: Factura = array[position]
        list =""
        for (com: ComponenteConImagen in fac.lista) {
            list+="${com.nombre}\n"
            uni+= "${com.unidadesCliente}\n"
            price+="${numeroEUR(com.precio*com.unidadesCliente)}€\n"
        }
        tvLista.text=list
        tvPrecio.text=price
        tvUnidad.text=uni


        var total=redondeo(array[position].precio)
        tvTotalTotal = view.findViewById(R.id.tvTotalTotal)
        tvTotalTotal.text= "${numeroEUR(total)}€"

        tvSubTotalPrecio.text= "${numeroEUR(redondeo(total/1.21))}€"

        var iva=redondeo(total-(total/1.21))
        tvIvaPrecio.text= "${numeroEUR(iva)}€"
        return view
    }

    fun redondeo(num: Double): Double {
        return (num * 100.00).roundToInt() / 100.00
    }

    fun numeroEUR(num: Double): String {
        val nf = NumberFormat.getInstance(Locale.GERMAN)
        var string = nf.format(num)
        return string
    }
}