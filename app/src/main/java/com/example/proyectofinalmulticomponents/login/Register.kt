package com.example.proyectofinalmulticomponents.login

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinalmulticomponents.MainActivity
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.example.proyectofinalmulticomponents.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var registrarse: Button
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordRep: EditText
    private lateinit var telefono: EditText
    private lateinit var provincia: EditText
    private lateinit var ciudad: EditText
    private lateinit var direccion: EditText

    private lateinit var user: Usuario

    companion object {
        lateinit var currentuserUID: String
        lateinit var nombre2: String
        lateinit var apellido2: String
        lateinit var email2: String
        lateinit var password2: String
        var telefono2: Int = 0
        lateinit var provincia2: String
        lateinit var ciudad2: String
        lateinit var direccion2: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        if(supportActionBar !=null)
            this.supportActionBar?.hide();


        //Definiendo las variables
        registrarse = findViewById(R.id.btnRRegister)
        nombre = findViewById(R.id.etRNombre)
        apellido = findViewById(R.id.etRApellido)
        email = findViewById(R.id.etREmail)
        password = findViewById(R.id.etRPassw)
        passwordRep = findViewById(R.id.etRPassw2)
        telefono = findViewById(R.id.etRTel)
        provincia = findViewById(R.id.etRProvin)
        ciudad = findViewById(R.id.etRCiu)
        direccion = findViewById(R.id.etRDir)


    }

    //Si está cada param completado, se crea al usuario en objeto con lo rellenado
    fun registrarUsuario(view: View) {


        //Contraseña y telefono
        if (password.text.toString().length < 6) {
            password.setError("La contraseña debe tener al menos 6 caracteres");

        } else if (telefono.text.toString().length != 9) {
            telefono.setError("El número de telefono en España debe tener 9 dígitos");

        } else if ((telefono.text.toString().length == 9)) {
            telefono.setError(null);
        } else if ((password.text.toString().length >= 6)) {
            password.setError(null);
        }

        if (password.text.toString() != passwordRep.text.toString()) {
            passwordRep.setError("Las contraseñas no coinciden");
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }
        else{
            passwordRep.setError(null);
        }


        //Si está relleno todo
        if (!nombre.text.toString().isEmpty() && !apellido.text.toString().isEmpty() &&
            !email.text.toString().isEmpty() && !password.text.toString().isEmpty() && !passwordRep.text.toString().isEmpty() &&
            !telefono.text.toString().isEmpty() && !provincia.text.toString().isEmpty() &&
            !ciudad.text.toString().isEmpty() && !direccion.text.toString().isEmpty() &&
            password.text.toString().length >= 6 && telefono.text.toString().length == 9 &&
            password.text.toString() == passwordRep.text.toString()
        ) {
            //Variables de edit text getText
            nombre2 = nombre.text.toString()
            apellido2 = apellido.text.toString()
            email2 = email.text.toString()
            password2 = password.text.toString()
            telefono2 = Integer.parseInt(telefono.text.toString())
            provincia2 = provincia.text.toString()
            ciudad2 = ciudad.text.toString()
            direccion2 = direccion.text.toString()

            //Creamos objeto Usuario con dichos datos
            user = Usuario(
                nombre2,
                apellido2,
                email2,
                password2,
                telefono2.toInt(),
                provincia2,
                ciudad2,
                direccion2
            )
            //Convertimos a la demo de Login al nuevo registrado
            Login.user = user


            //Victor - autenticación para meter en el record este usuario con su correo y contraseña para q le deje
            //iniciar sesion
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email2,
                password2
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    //Empezar login
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT)
                        .show()
                    var i: Intent = Intent(this, Login::class.java)
                    startActivity(i)

                } else {
                    Log.d(TAG, email2 + "!" + password2)
                    Toast.makeText(
                        this,
                        "Ha habido un error durante el proceso de registro",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
}