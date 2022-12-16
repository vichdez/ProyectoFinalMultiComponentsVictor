package com.example.proyectofinalmulticomponents

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinalmulticomponents.clases.ComponenteConImagen
import com.example.proyectofinalmulticomponents.databinding.ActivityAdminProductosBinding
import com.example.proyectofinalmulticomponents.login.Login
import com.example.proyectofinalmulticomponents.login.OlvideContrasenia
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdminProductos : AppCompatActivity() {

    private lateinit var mBinding: ActivityAdminProductosBinding
    private lateinit var insertarProducto: Button
    private lateinit var database: DatabaseReference
    private lateinit var imgProd: ImageView
    private lateinit var nombreProd: EditText
    private lateinit var descripProd: EditText
    private lateinit var precioProd: EditText

    //Para insertar Imagen
    private val pickImage = 100
    private val GalleryPick: Int = 1
    private var imageUri: Uri? = null
    private var storageref: StorageReference? = null

    //main
    private lateinit var fstorage: FirebaseStorage
    private lateinit var databasecomponents: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    companion object{
        lateinit var c1: ComponenteConImagen
        lateinit var nombreSring: String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAdminProductosBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        if(supportActionBar !=null)
            this.supportActionBar?.hide();

        //Definiendo las variables
        insertarProducto = findViewById(R.id.btnTerminar)
        imgProd = findViewById(R.id.imgFotoSubir)
        nombreProd = findViewById(R.id.etNombreProducto)
        descripProd = findViewById(R.id.etDescripcionProducto)
        precioProd = findViewById(R.id.etPrecioProducto)

        //Para subir la imagen
        imgProd.setOnClickListener {
            OpenGallery()
        }

        //Definiendo y rellenando los spinners
        val lista = resources.getStringArray(R.array.componentes)
        val componentes =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lista)
        mBinding.spComponentes.adapter = componentes

        val lista2 = resources.getStringArray(R.array.unidades)
        val unidades =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lista2)
        mBinding.spUnidades.adapter = unidades

        //Establecer la imagen en String
        storageref = FirebaseStorage.getInstance().getReference().child("Imagen del producto")
        // var filepath: StorageReference = storageref.child(imageUri.getLastPathSegment())

        var componente = "ejemplo"
        var unidads = 0

        //Seleccionador de spinners del producto
        //Componente
        mBinding.spComponentes.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Toast de elegir un componente del Spinner!
                    Toast.makeText(
                        applicationContext,
                        "Elija un componente del spinner",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    componente = mBinding.spComponentes.selectedItem.toString()

                }

            }
        //Unidades
        mBinding.spUnidades.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Toast de elegir un componente del Spinner!
                    Toast.makeText(
                        applicationContext,
                        "Elija un número de unidades del spinner",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    // unidads = mBinding.spUnidades.selectedItem.toString().toInt()
                    unidads = mBinding.spUnidades.getSelectedItem().toString().toInt()
                }

            }

        //Registrar en la base de datos el producto
        //Función de presionar el botón TERMINAR
        insertarProducto.setOnClickListener {

            val nombre = nombreProd.text.toString()
            val descripcion = descripProd.text.toString()
            val precio = precioProd.text.toString()

            //Revisar si hay imagen, nombre, descrip,...
            if (imageUri == null) {
                Toast.makeText(this, "Suba una imagen del producto", Toast.LENGTH_SHORT).show()

            } else if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(this, "El producto debe tener un nombre", Toast.LENGTH_SHORT).show()

            } else if (TextUtils.isEmpty(descripcion)) {
                Toast.makeText(this, "El producto debe tener una descripción", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(precio)) {
                Toast.makeText(this, "El producto debe tener un precio", Toast.LENGTH_SHORT).show()
            } else if (componente.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "El producto debe tener un tipo de componente",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (unidads.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "El producto debe tener un número de unidades",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //Cuando está todo okay

                //Meterlo en el almacenamiento de storage

                //String de nombre del producto ara actividad storage
                nombreSring=nombre

                //Meter en la base de datos el producto a registrar
                //En teoría el id está ya escrito auto
                //Crear objeto componente
                c1 =
                    ComponenteConImagen(nombre, descripcion, precio.toDouble(), imageUri!!.toString())

                c1.aniadirTag(componente)
                c1.aniadirUnidades(unidads)

                    //Subir imagen al storage
                FirebaseStorageManagement().subirImagen(this, imageUri!!)

                //Meterlo en la bd firebase
                database = FirebaseDatabase.getInstance().getReference("Componentes")

                //Indicar que se ha realizado correctamente
                database.child(nombre).setValue(c1)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Componente " + nombre + " agregado",
                            Toast.LENGTH_SHORT
                        ).show()
                        //Empty edit texts
                        mBinding.spComponentes.setSelection(0);
                        mBinding.spUnidades.setSelection(0);
                        imgProd.setImageResource(0);
                        nombreProd.text.clear()
                        descripProd.text.clear()
                        precioProd.text.clear()

                        //Actualizar la tienda
                        MainActivity.componentesArrayList.clear()
                        componentesBBDD()

                    }.addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            "Ha habido un error en el registro del producto",
                            Toast.LENGTH_SHORT
                        ).show()


                    }
            }
        }


    }

    private fun OpenGallery() {
        //val galleryintent = Intent (Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        //galleryintent.setAction("image/*")
        val galleryintent = Intent(Intent.ACTION_PICK)
        galleryintent.setType("image/*")
        galleryintent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(galleryintent, pickImage)
    }

    //Función de elegir Imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data!!.data
            imgProd.setImageURI(imageUri)
        }
    }

    fun abrirNavigation(view: View){
        var i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
    }




    fun componentesBBDD() {
        //Cosa del main

        fstorage = FirebaseStorage.getInstance()
        storageref = fstorage.reference

        firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference("Usuarios")
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
                                storageref!!.child("Componentes/" + cnombre + ".png").downloadUrl

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
                        this@AdminProductos,
                        "No se pudieron recuperar los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        } else {
        }
    }




}


