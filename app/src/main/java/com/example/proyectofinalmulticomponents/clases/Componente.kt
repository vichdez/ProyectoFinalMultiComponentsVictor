package com.example.proyectofinalmulticomponents.clases

class Componente (nombre:String, desc:String, precio:Double, img:Int){
    var nombre:String = nombre
    var descripcion:String = desc
    var precio:Double = precio
    var favorito:Boolean = false
    var tags:ArrayList<String> = ArrayList()
    var imagen:Int = img

    fun aniadirTag(s:String){
        tags.add(s)
    }
}
