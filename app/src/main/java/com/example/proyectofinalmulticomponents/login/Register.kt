package com.example.proyectofinalmulticomponents.login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
    private lateinit var telefono: EditText
    private lateinit var provincia: EditText
    private lateinit var ciudad: EditText
    private lateinit var direccion: EditText

   // private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    private lateinit var user:Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        registrarse= findViewById(R.id.btnRRegister)
        nombre = findViewById(R.id.etRNombre)
        apellido = findViewById(R.id.etRApellido)
        email = findViewById(R.id.etREmail)
        password = findViewById(R.id.etRPassw)
        telefono = findViewById(R.id.etRTel)
        provincia = findViewById(R.id.etRProvin)
        ciudad = findViewById(R.id.etRCiu)
        direccion = findViewById(R.id.etRDir)

/*
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
*/
        registrarse.setOnClickListener{


            val nombre2 = nombre.text.toString()
            val apellido2 = apellido.text.toString()
            val email2 = email.text.toString()
            val password2 = password.text.toString()
            val telefono2 = Integer.parseInt(telefono.text.toString())
            val provincia2 = provincia.text.toString()
            val ciudad2 = ciudad.text.toString()
            val direccion2 = direccion.text.toString()

            //Victor - añadir firebase para meter en el record este usuario con su correo y contraseña para q le
            //iniciar sesion
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email2,
                password2
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                } else {

                    Log.d(TAG, email2 + "!" +password2)
                    Toast.makeText(this, "Ha habido un error en el registro", Toast.LENGTH_SHORT).show()

                }
            }

            database = FirebaseDatabase.getInstance().getReference("Usuarios")
            val Usuario = Usuario(nombre2,apellido2,email2,password2,telefono2,provincia2,ciudad2,direccion2)
            database.child(telefono.text.toString()).setValue(Usuario).addOnSuccessListener {

                //----------------


                nombre.text.clear()
                apellido.text.clear()
                email.text.clear()
                password.text.clear()
                telefono.text.clear()
                provincia.text.clear()
                ciudad.text.clear()
                direccion.text.clear()

            }.addOnFailureListener{
                Toast.makeText(this, "Ha habido un error en el registro", Toast.LENGTH_SHORT).show()
            }



        }
    }

    fun registrarUsuario(view: View){
        if(!nombre.text.toString().isEmpty() && !apellido.text.toString().isEmpty() &&
            !email.text.toString().isEmpty() &&!password.text.toString().isEmpty() &&
            !telefono.text.toString().isEmpty() &&!provincia.text.toString().isEmpty() &&
            !ciudad.text.toString().isEmpty() &&!direccion.text.toString().isEmpty()){


            user.setPassword((password.text.toString()))

            user= Usuario(nombre.text.toString(),apellido.text.toString(),email.text.toString(), user.getPassword(),
                telefono.text.toString().toInt(),provincia.text.toString(),
                ciudad.text.toString(),direccion.text.toString())
            Login.user = user


            finish()
        }
    }
}