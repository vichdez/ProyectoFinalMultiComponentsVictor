package com.example.proyectofinalmulticomponents.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class OlvideContrasenia : AppCompatActivity() {
    private lateinit var etCorreo: EditText
    private lateinit var etForgotPassw: EditText
    private lateinit var etForgotPassw2: EditText
    private lateinit var btnGuardar: Button

    companion object {
        lateinit var userC: Usuario
    }

    private lateinit var database: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var currentuserUID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvide_contrasenia)

        etCorreo = findViewById(R.id.etCorreoFP)
        etForgotPassw = findViewById(R.id.etFP)
        etForgotPassw2 = findViewById(R.id.etFP2)
        btnGuardar = findViewById(R.id.btnCambiar)


        if(supportActionBar !=null)
            this.supportActionBar?.hide();
    }

    //Accion del boton Aceptar
    fun comprobarNuevaContrasenia(view: View) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        database = firebaseDatabase.getReference("Usuarios")

        //Que nada este vacio
        if (etCorreo.text.isNotEmpty() && etForgotPassw.text.isNotEmpty() && etForgotPassw2.text.isNotEmpty()) {
            if (etForgotPassw.text.toString() == etForgotPassw2.text.toString()) {

                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (singleSnapshot in dataSnapshot.children) {
                                val uemail: String =
                                    singleSnapshot.child("email").getValue(String::class.java)
                                        .toString()

                                val upassword: String =
                                    singleSnapshot.child("password").getValue(String::class.java)
                                        .toString()

                                Log.e(TAG, uemail + "!" + upassword)

                                if (etCorreo.text.toString() == uemail) {

                                    //Auth: Sign In With this User
                                    auth = FirebaseAuth.getInstance()
                                    auth.signInWithEmailAndPassword(uemail, upassword)
                                    var user = auth.currentUser
                                    if (user != null) {
                                    Log.e(TAG, user.toString())
                                    currentuserUID = user!!.uid

                                    //Ese es el usuario que nos interesa
                                    //Le cambiamos la contraseña a la nueva
                                    if (etForgotPassw.text.toString().length < 6 || etForgotPassw2.text.toString().length < 6) {
                                        etForgotPassw.setError("La contraseña debe tener al menos 6 caracteres");
                                    } else {
                                        etForgotPassw.setError(null)
                                    }


                                    if (etForgotPassw.text.toString().length >= 6 && etForgotPassw2.text.toString().length >= 6 &&
                                        etForgotPassw.text.toString() == etForgotPassw2.text.toString()
                                    ) {

                                        var correoUsuario = etCorreo.text.toString()
                                        var passwordUsuario = etForgotPassw.text.toString()

                                        database.child(currentuserUID).child("password")
                                            .setValue(passwordUsuario);


                                            user.updatePassword(passwordUsuario)
                                                .addOnCompleteListener { task2 ->
                                                    if (task2.isSuccessful) {
                                                        Toast.makeText(
                                                            this@OlvideContrasenia,
                                                            "Contraseña reseteada correctamente",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        Log.d(TAG, "User password updated.")
                                                    } else {

                                                        Toast.makeText(
                                                            this@OlvideContrasenia,
                                                            "Contraseña no se ha podido cambiar correctamente",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        Log.d(TAG, "User password problem.")
                                                    }
                                                }

                                        } else {
                                            Toast.makeText(
                                                this@OlvideContrasenia,
                                                "No se ha podido comprobar la autenticidad del usuario",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }


                                    }else{
                                        Toast.makeText(
                                            this@OlvideContrasenia,
                                            "La contraseña no se tratado correctamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }


                                }
                            }//for
                        }else{
                            Toast.makeText(
                                this@OlvideContrasenia,
                                "El correo no es un usuario registrado",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


            } else {
                Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Rellene los campos vacíos", Toast.LENGTH_SHORT).show()
        }
    }


}

