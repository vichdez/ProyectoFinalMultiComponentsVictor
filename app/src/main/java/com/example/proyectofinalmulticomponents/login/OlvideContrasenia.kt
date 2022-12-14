package com.example.proyectofinalmulticomponents.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.Usuario

class OlvideContrasenia : AppCompatActivity() {
    private lateinit var etForgotPassw:EditText
    private lateinit var etForgotPassw2:EditText
    private lateinit var btnGuardar:Button
    companion object{
        lateinit var userC:Usuario
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvide_contrasenia)

        etForgotPassw= findViewById(R.id.etFP)
        etForgotPassw2= findViewById(R.id.etFP2)
        btnGuardar= findViewById(R.id.btnCambiar)

    }

    fun comprobarNuevaContrasenia(view: View){
        if(etForgotPassw.text.isNotEmpty() && etForgotPassw2.text.isNotEmpty()){
            if(etForgotPassw.text.toString() == etForgotPassw2.text.toString()){
                userC.setPassword(etForgotPassw.text.toString())
                Login.user = userC
                Toast.makeText(this,"La contraseña se cambió con éxito",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"La contraseña no coincide",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Rellene los campos vacíos",Toast.LENGTH_SHORT).show()
        }
    }
}