package com.example.proyectofinalmulticomponents.clases

import com.example.proyectofinalmulticomponents.Facturas
import java.util.Date

class Factura (user:Usuario, fecha:String, lista:ArrayList<ComponenteConImagen>){
    var user:Usuario = user
    var fecha:String = fecha
    var lista:ArrayList<ComponenteConImagen> = lista
    var precio = suma()
    var id = Facturas.idauto

    private fun suma():Double{
        var suma:Double =0.0
        for (comp:ComponenteConImagen in lista) {
            suma += comp.precio * comp.unidadesCliente
        }
        return suma
    }
}