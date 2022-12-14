package com.example.proyectofinalmulticomponents.login

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.proyectofinalmulticomponents.MainActivity
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {


    private lateinit var etMail:EditText
    private lateinit var password:EditText
    private lateinit var btnIniciarSesion:Button

    companion object{
        lateinit var user:Usuario
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnIniciarSesion = findViewById(R.id.btnLogin);

        user = Usuario("demo","demo","demo@gmail.com","password",671351162,"Madrid","Daganzo","C/ Carmen Amaya n22")
        etMail=findViewById(R.id.etEmailLogin)
        user.setPassword("demodemo")
        password=findViewById(R.id.etPasswordLogin)
    }

    fun iniciarSesión(view: View) {
        btnIniciarSesion.setOnClickListener {
            if (etMail.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    etMail.text.toString(),
                    password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        var i:Intent = Intent(this,MainActivity::class.java)
                        startActivity(i)
                    } else {
                      showAlert()
                    }
                }
            }
        }
    }



    private fun showAlert(){
        val builder = AlertDialog.Builder( this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun registrarse(view: View){
        var i:Intent = Intent(this,Register::class.java)
        startActivity(i)
    }


    fun olvideContraseña(view:View){
        var i:Intent = Intent(this,OlvideContrasenia::class.java)
        OlvideContrasenia.userC= user
        startActivity(i)

    }
}
