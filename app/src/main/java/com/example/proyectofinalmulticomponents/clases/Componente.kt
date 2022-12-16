package com.example.proyectofinalmulticomponents.clases

import androidx.room.PrimaryKey


class Componente (nombre:String, desc:String, precio:Double,img:Int){


    var nombre:String = nombre
    var descripcion:String = desc
    var precio:Double = precio
    var favorito:Boolean = false
    //var tag:String = tag
    var tags:ArrayList<String> = ArrayList()
    var unidades:Int = 0
    var imagen:Int = img

    fun aniadirTag(s:String){
        tags.add(s)
    }

    fun aniadirUnidades(u:Int){
        unidades = u
    }

}
