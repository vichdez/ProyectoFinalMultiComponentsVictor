package com.example.proyectofinalmulticomponents.clases

import java.util.Date

class Factura (user:Usuario, fecha:String, lista:ArrayList<Componente>){
    var user:Usuario = user
    var fecha:String = fecha
    var lista:ArrayList<Componente> = lista
    var precio = suma()

    private fun suma():Double{
        var suma:Double =0.0
        for (comp:Componente in lista) suma+=comp.precio
        return suma
    }
}