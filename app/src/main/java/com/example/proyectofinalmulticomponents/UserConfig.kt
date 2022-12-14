package com.example.proyectofinalmulticomponents

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.proyectofinalmulticomponents.MainActivity.Companion.user
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.example.proyectofinalmulticomponents.login.Login
import com.example.proyectofinalmulticomponents.login.Register
import com.google.firebase.auth.FirebaseAuth

class UserConfig : AppCompatActivity() {
    private lateinit var guardar: Button
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var email: EditText
    private lateinit var telefono: EditText
    private lateinit var provincia: EditText
    private lateinit var ciudad: EditText
    private lateinit var direccion: EditText

    private lateinit var cerrarsesion: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_config)


        cerrarsesion = findViewById(R.id.btnCerrar)
        guardar= findViewById(R.id.btnGuardar)
        nombre= findViewById(R.id.etNombre)
        apellido= findViewById(R.id.etApellido)
        email= findViewById(R.id.etEmail)
        telefono = findViewById(R.id.etPhone)
        provincia = findViewById(R.id.etProvincia)
        ciudad = findViewById(R.id.etCiudad)
        direccion = findViewById(R.id.etDireccion)

        nombre.setText(user!!.nombre ,TextView.BufferType.EDITABLE)
        apellido.setText(user!!.apellidos ,TextView.BufferType.EDITABLE)
        email.setText(user!!.email,TextView.BufferType.EDITABLE)
        telefono.setText(user!!.telefono.toString(),TextView.BufferType.EDITABLE)
        provincia.setText(user!!.provincia,TextView.BufferType.EDITABLE)
        ciudad.setText(user!!.ciudad,TextView.BufferType.EDITABLE)
        direccion.setText(user!!.direccion,TextView.BufferType.EDITABLE)

        guardar.setOnClickListener {

            var nombremod:String =nombre.text.toString()
            var apellidomod:String =apellido.text.toString()
            var emailmod = email.text.toString()
            var passwordmod = user.getPassword()
            var telefonomod = telefono.text.toString().toInt()
            var provinciamod= provincia.text.toString()
            var ciudadmod = ciudad.text.toString()
            var direccionmod = direccion.text.toString()

            user= Usuario(nombremod,apellidomod,emailmod,passwordmod,telefonomod,provinciamod,ciudadmod,direccionmod)
        }

        cerrarsesion.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            var i:Intent = Intent(this, Login::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i: Intent
        when(item.itemId){
            R.id.HomeItem ->{
                finish()
            }
            R.id.CarritoItem ->{
                i = Intent(this,CarritoCompra::class.java)
                startActivity(i)
                finish()
            }
            R.id.FacturasItem ->{
                i = Intent(this,Facturas::class.java)
                startActivity(i)
                finish()
            }
            R.id.UsuarioItem ->{
                i = Intent(this,UserConfig::class.java)
                startActivity(i)
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}