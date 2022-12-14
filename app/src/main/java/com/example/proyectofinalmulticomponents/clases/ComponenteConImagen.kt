package com.example.proyectofinalmulticomponents.clases

import androidx.room.PrimaryKey


class ComponenteConImagen (nombre:String, desc:String, precio:Double,img:String){

    var nombre:String = nombre
    var descripcion:String = desc
    var precio:Double = precio
    var favorito:Boolean = false
    //var tag:String = tag
    var type:String = ""
    var unidades:Int = 0
    var imagen:String = img

    fun aniadirTag(s:String){
        type = s
    }

    fun aniadirUnidades(u:Int){
        unidades = u
    }

    fun cambiarImagen(im:String){
        imagen=im
    }

}