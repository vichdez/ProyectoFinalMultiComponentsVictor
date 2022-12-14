package com.example.proyectofinalmulticomponents.clases

import android.widget.EditText

class Usuario (nombre:String , apellidos:String, email:String, password:String, telefono:Int, provincia:String, ciudad:String, direccion:String){
    var nombre:String = nombre
    var apellidos:String = apellidos
    var email:String = email
    var telefono:Int = telefono
    var provincia:String = provincia
    var ciudad:String = ciudad
    var direccion:String = direccion
    private var isAdmin:Boolean = false
    private var password:String = password

    fun setAdmin(admin:Boolean){
        isAdmin = admin
    }

    fun setPassword(pass:String){
        password = pass
    }

    fun getPassword(): String {
        return password
    }

    fun atenticaPassword(et:EditText):Boolean{
        println(et.text)
        println(password)
        return et.text.toString() == password
    }
}