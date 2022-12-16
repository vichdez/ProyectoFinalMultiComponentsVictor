package com.example.proyectofinalmulticomponents

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.proyectofinalmulticomponents.MainActivity.Companion.user
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.example.proyectofinalmulticomponents.login.Login
import com.example.proyectofinalmulticomponents.login.Register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

    private lateinit var database: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var currentuserUID: String

    private lateinit var user: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_config)

        if(supportActionBar !=null)
            this.supportActionBar?.hide();

        firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference("Usuarios")

       // cerrarsesion = findViewById(R.id.btnCerrar)
        guardar= findViewById(R.id.btnGuardar)
        nombre= findViewById(R.id.etNombre)
        apellido= findViewById(R.id.etApellido)
        email= findViewById(R.id.etEmail)
        email.setEnabled(false);
        telefono = findViewById(R.id.etPhone)
        provincia = findViewById(R.id.etProvincia)
        ciudad = findViewById(R.id.etCiudad)
        direccion = findViewById(R.id.etDireccion)

        user = MainActivity.user

        nombre.setText(user!!.nombre ,TextView.BufferType.EDITABLE)
        apellido.setText(user!!.apellidos ,TextView.BufferType.EDITABLE)
        email.setText(user!!.email,TextView.BufferType.NORMAL)
        email.setEnabled(false);
        telefono.setText(user!!.telefono.toString(),TextView.BufferType.EDITABLE)
        provincia.setText(user!!.provincia,TextView.BufferType.EDITABLE)
        ciudad.setText(user!!.ciudad,TextView.BufferType.EDITABLE)
        direccion.setText(user!!.direccion,TextView.BufferType.EDITABLE)

        guardar.setOnClickListener {
            condicionantes()

        }


    }


    fun condicionantes() {

        var nombremod:String =nombre.text.toString()
        var apellidomod:String =apellido.text.toString()
        var emailmod = email.text.toString()
        var telefonomod = telefono.text.toString().toInt()
        var provinciamod= provincia.text.toString()
        var ciudadmod = ciudad.text.toString()
        var direccionmod = direccion.text.toString()

        //Telefono

       if (telefonomod.toString().length != 9) {
            telefono.setError("El número de telefono en España debe tener 9 dígitos")

        } else if ((telefono.text.toString().length == 9)) {
           telefono.setError(null)

       }

        //Si está relleno todo
        if (!nombremod.toString().isEmpty() && !apellidomod.toString().isEmpty() &&
            !telefonomod.toString().isEmpty() && !provinciamod.toString().isEmpty() &&
            !ciudadmod.toString().isEmpty() && !direccionmod.toString().isEmpty() &&
            telefonomod.toString().length == 9
        ) {

            //Modificamos objeto Usuario con dichos datos
            user= Usuario(nombremod,apellidomod,emailmod,user.getPassword(),telefonomod,provinciamod,ciudadmod,direccionmod)

            //Guardar cambios en la base de datos

            //Guardar cambios en la base de datos

            currentuserUID = FirebaseAuth.getInstance().currentUser!!.uid
            database.child(currentuserUID).child("nombre").setValue(nombremod);
            database.child(currentuserUID).child("apellidos").setValue(apellidomod);
            database.child(currentuserUID).child("email").setValue(emailmod);
            database.child(currentuserUID).child("telefono").setValue(telefonomod);
            database.child(currentuserUID).child("provincia").setValue(provinciamod);
            database.child(currentuserUID).child("ciudad").setValue(ciudadmod);
            database.child(currentuserUID).child("direccion").setValue(direccionmod);


                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    //Empezar login
                    Toast.makeText(this, "Usuario modificado correctamente", Toast.LENGTH_SHORT).show()
                    var i: Intent = Intent(this, MainActivity::class.java)
                    startActivity(i)

            finish()
        } else {
            Toast.makeText(this, "Existen parámetros no cumplimentados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Cambie los parámetros señalados en rojo")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun abrirNavigation(view: View){
        var i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

}