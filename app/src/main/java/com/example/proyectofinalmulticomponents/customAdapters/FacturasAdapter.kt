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
import kotlin.math.roundToInt

class FacturasAdapter(var context: Context, var array: ArrayList<Factura>): BaseAdapter(){
    lateinit var ivLog: ImageView
    lateinit var tvNom: TextView
    lateinit var tvEma: TextView
    lateinit var tvTel:TextView
    lateinit var tvPro:TextView
    lateinit var tvCiu:TextView
    lateinit var tvDir:TextView
    lateinit var tvLista:TextView
    lateinit var tvPrecio: TextView
    lateinit var tvIva:TextView
    lateinit var tvTotalIva:TextView
    lateinit var tvPrecioCom:TextView
    lateinit var tvTotal:TextView

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

        ivLog = view.findViewById(R.id.ivFacturas)
        ivLog.setImageResource(R.drawable.tickets)

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

        tvPrecioCom = view.findViewById(R.id.tvprecioCom)

        tvIva = view.findViewById(R.id.tvIvaCom)

        var iIva=0.0
        var s =""
        var sinIva=""
        var sinIvaT=0.0
        var soloIva=""
        var fac: Factura = array[position]

            iIva=0.0
            s =""
            sinIva=""
            soloIva=""
            for (com: Componente in fac.lista) {
                var iva = com.precio*21/100

                s+="${com.nombre}\n"
                iIva+= iva
                sinIva +="${redondeo(com.precio-iva)}€\n"
                soloIva += "${redondeo(iva)}€\n"
                sinIvaT +=redondeo(com.precio-iva)
            }
            tvLista.text=s
            tvPrecioCom.text = sinIva
            tvIva.text = soloIva

            tvPrecio = view.findViewById(R.id.tvfTotal)
            tvPrecio.text = "$sinIvaT€"

            tvTotalIva = view.findViewById(R.id.tvTotalIva)
            tvTotalIva.text = "${redondeo(iIva)}€"

            tvTotal = view.findViewById(R.id.tvTotalPrecio)
            tvTotal.text="Total precio final: ${array[position].precio}€"
        return view
    }

    fun redondeo(num: Double): Double {
        return (num * 100.0).roundToInt() / 100.0
    }
}