package com.example.proyectofinalmulticomponents.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinalmulticomponents.Facturas
import com.example.proyectofinalmulticomponents.MainActivity
import com.example.proyectofinalmulticomponents.R
import com.example.proyectofinalmulticomponents.clases.ComponenteConImagen
import com.example.proyectofinalmulticomponents.clases.Factura
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class Login : AppCompatActivity() {


    //main
    private lateinit var fstorage: FirebaseStorage
    private lateinit var storageref: StorageReference
    private lateinit var databasecomponents: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase


    private lateinit var databaseFac: DatabaseReference


    private lateinit var etMail: EditText
    private lateinit var password: EditText
    private lateinit var btnIniciarSesion: Button

    companion object {
        lateinit var user: Usuario
        lateinit var correo : String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(supportActionBar !=null)
            this.supportActionBar?.hide();

        //DEFINICIÓN DE ELEMENTOS
        btnIniciarSesion = findViewById(R.id.btnLogin)
        etMail = findViewById(R.id.etEmailLogin)
        password = findViewById(R.id.etPasswordLogin)

        //Usuario de prueba
        user = Usuario(
            "demo",
            "demo",
            "demo@gmail.com",
            "password",
            671351162,
            "Madrid",
            "Daganzo",
            "C/ Carmen Amaya n22"
        )
        user.setPassword("demodemo")

        MainActivity.componentesArrayList.clear()
        componentesBBDD()
    }

    fun iniciarSesión(view: View) {
        if (etMail.text.isNotEmpty() && password.text.isNotEmpty()) {
            //Se verifica con lo rellenado si el correo y contraseña del usuario
            // está en la bd autenticacion
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                etMail.text.toString(), password.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    correo = etMail.text.toString()
                    //Abrir el navigation drawer
                    leerFactura()
                    var i: Intent = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    Toast.makeText(this, "Bienvenido a MultiComponents", Toast.LENGTH_SHORT).show()
                } else {
                    showAlert()
                }

            }
        }
    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El nombre o la contraseña no son correctos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun registrarse(view: View) {
        var i: Intent = Intent(this, Register::class.java)
        startActivity(i)
    }


    fun olvideContraseña(view: View) {
        var i: Intent = Intent(this, OlvideContrasenia::class.java)
        OlvideContrasenia.userC = user
        startActivity(i)

    }


    fun componentesBBDD() {
        //Cosa del main

        fstorage = FirebaseStorage.getInstance()
        storageref = fstorage.reference

        firebaseDatabase = FirebaseDatabase.getInstance();
        databasecomponents = firebaseDatabase.getReference("Componentes")


        if (MainActivity.componentesArrayList.isEmpty()) {


            lateinit var c: ComponenteConImagen
            //Agarrar la base de datos de componentes
            databasecomponents.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
//FETCH ALL POSSIBLE OBJECTS COMPONENTS FROM FIREBASE
                        for (singleSnapshot in dataSnapshot.children) {
                            //Por cada branch, un componente
                            val cnombre = singleSnapshot.child("nombre").getValue().toString()
                            val cdescripcion =
                                singleSnapshot.child("descripcion").getValue().toString()
                            val cprecio = singleSnapshot.child("precio").getValue().toString()
                            val ctype = singleSnapshot.child("type").getValue().toString()
                            val cunidades =
                                singleSnapshot.child("unidades").getValue().toString()

                            //La imagen se busca en el Storage, sabiendo ya el nombre lo tenemos identificado
                            val cimagen =
                                storageref.child("Componentes/" + cnombre + ".png").downloadUrl
                            if ((cunidades.toInt()-1) > 0) {
                                //lateinit var uri: String
                                cimagen.addOnSuccessListener { uri ->


                                    //2.
                                    c = ComponenteConImagen(
                                        cnombre,
                                        cdescripcion,
                                        cprecio.toDouble(),
                                        uri.toString()
                                    )

                                    c.type = ctype
                                    c.unidades = cunidades.toInt()

                                    //3. add
                                    //Para el arrayList
                                    MainActivity.componentesArrayList.add(c)
                                    // Log.e(TAG, c.nombre + "!" + c.precio + "!" + c.descripcion + "!" + c.imagen)
                                    //Log.e(ContentValues.TAG, MainActivity.componentesArrayList[pos].toString())
                                }
                            } else {
                                //No se crea objeto
                            }

                        }


                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Hubo un error en el listado de productos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@Login,
                        "No se pudieron recuperar los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        } else {
        }

    }//termina la fun


    //Me va a pasar todas las facturas
    fun leerFactura() {
        Facturas.facturaBBDD.clear()
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseFac = firebaseDatabase.getReference("Facturas")


        if (Facturas.facturaBBDD.isEmpty()) {
            lateinit var fac: Factura

            databaseFac.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {


                        for (singleSnapshot in dataSnapshot.children) {
                            val nombreFac = singleSnapshot.getValue().toString()
                            val fechaFac = singleSnapshot.child("fecha").getValue().toString()
                            val idFac = singleSnapshot.child("id").getValue().toString().toInt()
                            val arrayListCompo: ArrayList<ComponenteConImagen> = ArrayList()
                            //Objeto componente
                            for (singleSnapshot in singleSnapshot.child("lista").children) {
                                val comp: ComponenteConImagen
                                val nombrecomp =
                                    singleSnapshot.child("nombre").getValue().toString()
                                val descrcomp =
                                    singleSnapshot.child("descripcion").getValue().toString()
                                val preciocomp =
                                    singleSnapshot.child("precio").getValue().toString()
                                val imgcomp = singleSnapshot.child("imagen").getValue().toString()

                                val unidadescompradas = singleSnapshot.child("unidadesCliente").getValue().toString().toInt()
                                comp = ComponenteConImagen(
                                    nombrecomp,
                                    descrcomp,
                                    preciocomp.toDouble(),
                                    imgcomp
                                )
                                comp.unidadesCliente = unidadescompradas.toInt()
                                comp.actualizar(unidadescompradas)
                                arrayListCompo.add(comp)
                            }
                            //--------
                            val precioFac =
                                singleSnapshot.child("precio").getValue().toString().toDouble()

                            //Objeto usuario
                            val user: Usuario
                            val nomuser =
                                singleSnapshot.child("user").child("nombre").getValue().toString()
                            val apeuser = singleSnapshot.child("user").child("apellidos").getValue()
                                .toString()
                            val emailuser =
                                singleSnapshot.child("user").child("email").getValue().toString()
                            val passuser =
                                singleSnapshot.child("user").child("password").getValue().toString()
                            val tlfuser =
                                singleSnapshot.child("user").child("telefono").getValue().toString()
                            val ciuser =
                                singleSnapshot.child("user").child("ciudad").getValue().toString()
                            val proviuser =
                                singleSnapshot.child("user").child("provincia").getValue()
                                    .toString()
                            val diruser = singleSnapshot.child("user").child("direccion").getValue()
                                .toString()

                            user = Usuario(
                                nomuser,
                                apeuser,
                                emailuser,
                                passuser,
                                tlfuser.toInt(),
                                proviuser,
                                ciuser,
                                diruser
                            )
                            //Se crea el objeto fac
                            fac = Factura(user, fechaFac, arrayListCompo)
                            fac.id = idFac
                            fac.precio = precioFac

                            //Se ve el ultimo id para actualizar el autonumerado
                            Facturas.idauto = idFac + 1
                            //Se mete en el array list
                            if (emailuser.equals(etMail.text.toString())) {
                                Facturas.facturaBBDD.add(fac)
                            } else {
                            }
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Hubo un error en el listado de productos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@Login,
                        "No se pudieron recuperar los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        } else {
        }
    }//Original

    fun numeroEUR(num: Double): String {
        val nf = NumberFormat.getInstance(Locale.GERMAN)
        var string = nf.format(num)
        return string
    }

}
