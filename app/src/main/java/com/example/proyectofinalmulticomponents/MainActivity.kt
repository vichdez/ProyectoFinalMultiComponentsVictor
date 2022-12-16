package com.example.proyectofinalmulticomponents


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.proyectofinalmulticomponents.clases.Componente
import com.example.proyectofinalmulticomponents.clases.ComponenteConImagen
import com.example.proyectofinalmulticomponents.clases.Factura
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.example.proyectofinalmulticomponents.customAdapters.ComponentesconImagenAdapter
import com.example.proyectofinalmulticomponents.databinding.ActivityMainBinding
import com.example.proyectofinalmulticomponents.databinding.NavHeaderNavigationDrawerBinding
import com.example.proyectofinalmulticomponents.login.Login
import com.example.proyectofinalmulticomponents.login.Register
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.text.*
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding2: NavHeaderNavigationDrawerBinding
    private lateinit var userNav: Usuario
    private lateinit var navView: NavigationView
    internal lateinit var nombreNav: TextView
    internal lateinit var mailNav: TextView
    lateinit var nomNav: String
    lateinit var correoNav: String


    private lateinit var etBuscador: EditText
    private lateinit var lvComponentesConImagen: ListView
    private lateinit var gvComponentesConImagen: GridView
    private lateinit var cbFavoritos: CheckBox
    private lateinit var sFiltros: Switch
    private lateinit var llFiltros: LinearLayout
    private lateinit var cbRatones: CheckBox
    private lateinit var cbTeclados: CheckBox
    private lateinit var cbMonitores: CheckBox
    private lateinit var cbProcesadores: CheckBox
    private lateinit var cbPlacasBase: CheckBox
    private lateinit var cbRam: CheckBox
    private lateinit var cbPortatiles: CheckBox
    private lateinit var cbTorres: CheckBox
    private lateinit var cbFuentesAlim: CheckBox

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fstorage: FirebaseStorage
    private lateinit var storageref: StorageReference
    private lateinit var database: DatabaseReference
    private lateinit var databasecomponents: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var currentuserUID: String

    private val mStorageRef = FirebaseStorage.getInstance().reference

    private lateinit var nomBarra: TextView
    private lateinit var mailBarra: TextView
    private lateinit var civ: CircleImageView

    //Para insertar Imagen
    private val pickImage = 100
    private val GalleryPick: Int = 1
    private var imageUri: Uri? = null


    private lateinit var databaseFac: DatabaseReference

    companion object {
        var filtroArrayList: ArrayList<ComponenteConImagen> = ArrayList()
        var arrayFavoritos: ArrayList<ComponenteConImagen> = ArrayList()
        var componentesArrayList: ArrayList<ComponenteConImagen> = ArrayList()
        lateinit var adapterComponentes: ComponentesconImagenAdapter
        lateinit var adapterComponentesGridView: ComponentesconImagenAdapter
        lateinit var user: Usuario
    }

    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //LAYOUT
        val dl: DrawerLayout = findViewById(R.id.drawerlayout)
        val navview: NavigationView = findViewById(R.id.navview)


        toggle = ActionBarDrawerToggle(this, dl, R.string.open, R.string.close)

        dl.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("")

        navview.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_main -> {
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_carritoCompra -> {
                    var intent = Intent(this, CarritoCompra::class.java)
                    startActivity(intent)
                }
                R.id.nav_facturas -> {
                    var intent = Intent(this, Facturas::class.java)
                    startActivity(intent)
                }
                R.id.nav_userConfig -> {
                    var intent = Intent(this, UserConfig::class.java)
                    startActivity(intent)
                }
                R.id.nav_acercaDe -> {
                    var intent = Intent(this, AcercaDe::class.java)
                    startActivity(intent)
                }
                R.id.nav_politicaDePrivacidad -> {
                    var intent = Intent(this, PoliticaDePrivacidad::class.java)
                    startActivity(intent)
                }
                R.id.nav_login -> {
                    var intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }
                else -> {}
            }
            true
        }




        auth = FirebaseAuth.getInstance()
        fstorage = FirebaseStorage.getInstance()
        storageref = fstorage.reference

        //FirebaseDatabase.getInstance() es lo mismo
        firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference("Usuarios")
        databasecomponents = firebaseDatabase.getReference("Componentes")

        //Definimos el uid del usuario actual
        currentuserUID = FirebaseAuth.getInstance().currentUser!!.uid
        // Toast.makeText(this, currentuserUID.toString(), Toast.LENGTH_SHORT).show()

        //Recuperar de la bd Usuarios sus datos
        getdata();

        //leerFactura()

        user = Login.user


        //auxBtn = findViewById(R.id.auxButton)
        // imgBtnMenu = findViewById(R.id.imgBtnMenu) //Abrir menu
        //imgBtnNavigator = findViewById(R.id.imgBtnNavigator) //Abrir Navigator

        lvComponentesConImagen = findViewById(R.id.lvComponentesConImagenHome)
        gvComponentesConImagen = findViewById(R.id.gvComponentesConImagenHome)
        cbFavoritos = findViewById(R.id.cbFavoritos)
        sFiltros = findViewById(R.id.sFiltros)
        llFiltros = findViewById(R.id.llFiltros)
        cbRatones = findViewById(R.id.chbRatones)
        cbTeclados = findViewById(R.id.chbTeclados)
        cbMonitores = findViewById(R.id.chbMonitores)
        cbProcesadores = findViewById(R.id.chbProcesadores)
        cbPlacasBase = findViewById(R.id.chbPlacasBase)
        cbRam = findViewById(R.id.chbRam)
        cbPortatiles = findViewById(R.id.chbPortatiles)
        cbTorres = findViewById(R.id.chbTorres)
        cbFuentesAlim = findViewById(R.id.chbFAliment)

        //Barra lateral
        binding2 = NavHeaderNavigationDrawerBinding.inflate(layoutInflater)
        nomBarra = binding2.username
        mailBarra = binding2.mailuser
        civ = binding2.circleimageview

        nomBarra.visibility = VISIBLE
        mailBarra.visibility = VISIBLE

        /*
        nomBarra.text = user.nombre.toString()
        mailBarra.text = user.email.toString()

         */

        nomBarra.text = "sssss"
        mailBarra.text = " fafaf s"

        //Para subir la imagen
        civ.setOnClickListener {
            OpenGallery()
        }

        etBuscador = findViewById(R.id.buscador)

        llFiltros.visibility = GONE
        etBuscador.visibility = GONE
        sFiltros.isChecked = false


        //Rellenar el main con los Productos de nuestra bbdd (tienda)

/*
        //Comprobar el array componentesArrayList
        if(componentesArrayList.isEmpty()){
            Toast.makeText(
                applicationContext,
                "ESTA VACIO WTF",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            Toast.makeText(
                applicationContext,
                "ESTA lleno",
                Toast.LENGTH_SHORT
            ).show()
        }
*/

        //El relleno de componentes a la bbdd esta en el login

        //componentesBBDD()


        adapterComponentes = ComponentesconImagenAdapter(this, componentesArrayList, false)
        adapterComponentesGridView = ComponentesconImagenAdapter(this, componentesArrayList, false)
        lvComponentesConImagen.adapter = adapterComponentes
        adapterComponentes.notifyDataSetChanged()


        //Meterlos en el adaptador

/*
        if (componentesBBDD.isEmpty()) {
            var c: Componente = Componente(
                "Ratón de Hibertronics",
                "Raton con luces de colorines y cable USB de 2 m !!",
                25.99,
                R.drawable.corazon_relleno
            )
            var c2: Componente = Componente(
                "Pantalla de Hibertronics",
                "Pantalla con luces de colorines de 17 pulgadas ",
                17.01,
                R.drawable.corazon_relleno
            )
            var c3: Componente = Componente(
                "Torre de Hibertronics",
                "Hardware con luces de colorines y 480 GB de memoria !!",
                60.00,
                R.drawable.corazon_relleno
            )
            var c4: Componente = Componente(
                "Teclado de Hibertronics",
                "Mesita con luces de colorines y hueco para tus refrescos y juegos favoritos!!",
                37.45,
                R.drawable.corazon_relleno
            )

            c.aniadirTag("Raton")
            c2.aniadirTag("Monitor")
            c3.aniadirTag("Torre")
            c4.aniadirTag("Teclado")

            componentesBBDD.add(c)
            componentesBBDD.add(c2)
            componentesBBDD.add(c3)
            componentesBBDD.add(c4)
        }
        adapterCom = ComponentesAdapter(this, componentesBBDD, false)
        lvComponentes.adapter = adapterCom

*/

        //Meter un producto en el carrito
        lvComponentesConImagen.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->

                var unidadesdispo: String

                //Unidadesdisponibles
                //if de los arraylist segun el contexto 1)medir cuantas unidades tienen
                if (cbFavoritos.isChecked) {

                    unidadesdispo = arrayFavoritos[position].unidades.toString()
                } else if (cbRatones.isChecked || cbTeclados.isChecked || cbMonitores.isChecked
                    || cbRam.isChecked || cbProcesadores.isChecked || cbTorres.isChecked
                    || cbPlacasBase.isChecked || cbPortatiles.isChecked || cbFuentesAlim.isChecked
                    || !etBuscador.text.isEmpty()
                    && !cbFavoritos.isChecked
                ) {
                    unidadesdispo = filtroArrayList[position].unidades.toString()

                } else {
                    unidadesdispo = componentesArrayList[position].unidades.toString()
                }


                //Te dice las unidades que quedan disponibles
                //Verificar la compra
                val builder = AlertDialog.Builder(this);
                builder.setTitle("Añadir producto")

                builder.setMessage(
                    "¿Desea añadir este producto a la cesta? \n\n\nUnidades disponibles: " + unidadesdispo + ""
                ).setCancelable(false)
                    .setPositiveButton("Si") { dialog, id ->

                        //Identificar el componente por arrayList y llevarselo al carrito
                        //if de los arraylist segun el contexto
                        if (cbFavoritos.isChecked) {
                            //Equipo favoritos
                            if (CarritoCompra.componentesBBDD.contains(arrayFavoritos[position])) {

                                CarritoCompra.componentesBBDD.elementAt(
                                    CarritoCompra.componentesBBDD.indexOf(arrayFavoritos[position])
                                ).unidadesCliente++

                            } else {
                                CarritoCompra.componentesBBDD.add(arrayFavoritos[position])


                                Toast.makeText(
                                    this,
                                    arrayFavoritos[position].nombre.toString() + " añadido en Carrito de la compra",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                        }
                        //Equipo filtros Array List
                        else if (cbRatones.isChecked || cbTeclados.isChecked || cbMonitores.isChecked
                            || cbRam.isChecked || cbProcesadores.isChecked || cbTorres.isChecked
                            || cbPlacasBase.isChecked || cbPortatiles.isChecked || cbFuentesAlim.isChecked
                            || !etBuscador.text.isEmpty() && !cbFavoritos.isChecked
                        ) {
                            if (CarritoCompra.componentesBBDD.contains(filtroArrayList[position])) {

                                CarritoCompra.componentesBBDD.elementAt(
                                    CarritoCompra.componentesBBDD.indexOf(filtroArrayList[position])
                                ).unidadesCliente++

                            } else {
                                CarritoCompra.componentesBBDD.add(filtroArrayList[position])
                            }


                            Toast.makeText(
                                this,
                                filtroArrayList[position].nombre.toString() + " añadido en Carrito de la compra",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            //Equipo componentes Array List
                        } else if (CarritoCompra.componentesBBDD.contains(componentesArrayList[position])) {

                            CarritoCompra.componentesBBDD.elementAt(
                                CarritoCompra.componentesBBDD.indexOf(componentesArrayList[position])
                            ).unidadesCliente++

                        } else {
                            CarritoCompra.componentesBBDD.add(componentesArrayList[position])
                        }

                        Toast.makeText(
                            this,
                            componentesArrayList[position].nombre.toString() + " añadido en Carrito de la compra",
                            Toast.LENGTH_SHORT
                        )
                            .show()


                        //adapterCom.notifyDataSetChanged()
                        adapterComponentes.notifyDataSetChanged()
                    }
                    .setNegativeButton("No")
                    { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }

                val alert = builder.create()
                alert.show()

            }

//El switch de filtros
        sFiltros.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    llFiltros.visibility = VISIBLE
                    etBuscador.visibility = VISIBLE
                }

                false -> {
                    llFiltros.visibility = GONE
                    etBuscador.visibility = GONE

                }
            }
        }

//Checkbox favorito
        cbFavoritos.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    buttonView.buttonTintList = ColorStateList.valueOf(Color.RED)
                    arrayFavoritos = componentesArrayList.filter { componente ->
                        componente.favorito
                    } as ArrayList<ComponenteConImagen>
                    adapterComponentes =
                        ComponentesconImagenAdapter(
                            this,
                            arrayFavoritos as java.util.ArrayList<ComponenteConImagen>,
                            false
                        )
                    lvComponentesConImagen.adapter = adapterComponentes
                }
                false -> {
                    buttonView.buttonTintList = ColorStateList.valueOf(Color.GRAY)
                    if (!cbRatones.isChecked && !cbTeclados.isChecked && !cbMonitores.isChecked
                        && !cbRam.isChecked && !cbProcesadores.isChecked && !cbTorres.isChecked
                        && !cbPlacasBase.isChecked && !cbPortatiles.isChecked && !cbFuentesAlim.isChecked
                        && !cbFavoritos.isChecked
                    ) {
                        adapterComponentes =
                            ComponentesconImagenAdapter(this, componentesArrayList, false)
                        lvComponentesConImagen.adapter = adapterComponentes
                    } else {
                        adapterComponentes =
                            ComponentesconImagenAdapter(this, filtroArrayList, false)
                        lvComponentesConImagen.adapter = adapterComponentes
                    }
                }
            }
        }

//Cuando no haya usuario
        if (currentuserUID.isNullOrEmpty()) {
            var i: Intent = Intent(this, Login::class.java)
            startActivity(i)
        }


        etBuscador.setOnKeyListener(View.OnKeyListener { v, keycode, event ->
            if (event.getAction() === KeyEvent.ACTION_DOWN &&
                keycode == KeyEvent.KEYCODE_ENTER
            ) {

                if (!etBuscador.text.toString().equals("")) {

                    filtroArrayList.clear()
                    // Perform action on key press
                    for (i in 0 until componentesArrayList.size) {
                        if (componentesArrayList[i].nombre
                                .contains(etBuscador.text.toString(), ignoreCase = true)
                        ) {
                            filtroArrayList.add(componentesArrayList[i])
                        }
                    }

                    Toast.makeText(
                        this@MainActivity,
                        "Buscar: " + etBuscador.text.toString() + " :" + filtroArrayList.size,
                        Toast.LENGTH_SHORT
                    ).show()
                    adapterComponentes =
                        ComponentesconImagenAdapter(this, filtroArrayList, false)
                    lvComponentesConImagen.adapter = adapterComponentes

                    return@OnKeyListener true
                } else {
                    adapterComponentes =
                        ComponentesconImagenAdapter(this, componentesArrayList, false)
                    lvComponentesConImagen.adapter = adapterComponentes

                    filtroArrayList.clear()
                }
            }
            false
        })


        updateNavHeader();
    }
//onCreate

    private fun OpenGallery() {
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
            civ.setImageURI(imageUri)
        }
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
                                storageref.child("Componentes/" + cnombre + ".png").downloadUrl

                            if ((cunidades.toInt() - 1) > 0) {
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
                        this@MainActivity,
                        "No se pudieron recuperar los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        } else {
        }
    }


    fun checked(view: View) {
        var checked = view as CheckBox
        compruebafiltro(cbRatones, "Ratón")
        compruebafiltro(cbTeclados, "Teclado")
        compruebafiltro(cbMonitores, "Monitor")
        compruebafiltro(cbRam, "RAM")
        compruebafiltro(cbProcesadores, "Procesador")
        compruebafiltro(cbTorres, "Torre")
        compruebafiltro(cbPlacasBase, "Placa base")
        compruebafiltro(cbPortatiles, "Portátil")
        compruebafiltro(cbFuentesAlim, "Fuente de alimentación")

        if (!cbRatones.isChecked && !cbTeclados.isChecked && !cbMonitores.isChecked
            && !cbRam.isChecked && !cbProcesadores.isChecked && !cbTorres.isChecked
            && !cbPlacasBase.isChecked && !cbPortatiles.isChecked && !cbFuentesAlim.isChecked
            && !cbFavoritos.isChecked
        ) {
            adapterComponentes =
                ComponentesconImagenAdapter(this, componentesArrayList, false)
            lvComponentesConImagen.adapter = adapterComponentes
        } else if (!cbRatones.isChecked && !cbTeclados.isChecked && !cbMonitores.isChecked
            && !cbRam.isChecked && !cbProcesadores.isChecked && !cbTorres.isChecked
            && !cbPlacasBase.isChecked && !cbPortatiles.isChecked && !cbFuentesAlim.isChecked
            && cbFavoritos.isChecked
        ) {
            adapterComponentes =
                ComponentesconImagenAdapter(
                    this,
                    arrayFavoritos as java.util.ArrayList<ComponenteConImagen>,
                    false
                )
            lvComponentesConImagen.adapter = adapterComponentes
        } else {
            adapterComponentes = ComponentesconImagenAdapter(this, filtroArrayList, false)
            lvComponentesConImagen.adapter = adapterComponentes
        }
    }


    fun compruebafiltro(ch: CheckBox, s: String) {
        if (ch.isChecked) {
            for (com: ComponenteConImagen in componentesArrayList) {
                if (com.type.equals(s)) {
                    if (cbFavoritos.isChecked && com.favorito) {
                        if (filtroArrayList.contains(com)) {

                        } else {
                            filtroArrayList.add(com)
                        }
                    } else if (cbFavoritos.isChecked && !com.favorito) {

                    } else {
                        if (filtroArrayList.contains(com)) {

                        } else {
                            filtroArrayList.add(com)
                        }
                    }
                }
            }
        } else {
            for (com: ComponenteConImagen in componentesArrayList) {
                if (filtroArrayList.contains(com) && com.type.equals(s)) {
                    filtroArrayList.remove(com)
                    adapterComponentes.notifyDataSetChanged()
                }
            }
        }
    }


    private fun getdata1() {
        database.child(currentuserUID) // Create a reference to the child node directly
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This callback will fire even if the node doesn't exist, so now check for existence
                    if (dataSnapshot.exists()) {
                        //println("The node exists.")
                    } else {
                        // println("The node does not exist.")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }


    //Coger los datos del usuario que acaba de entrar registrado previamente de la base de datos
    private fun getdata() {


        database.child(currentuserUID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //----
                //Filtramos en la base de datos de Usuarios (database) en busca de ese UID

                if (dataSnapshot.exists()) {

                    //for (singleSnapshot in dataSnapshot.children) {

                    // Toast.makeText(applicationContext,dataSnapshot.key.toString(), Toast.LENGTH_SHORT).show()


                    // if (singleSnapshot.key.toString().equals(currentuserUID))
                    // {
                    //Cogemos el usuario de raíz/ No hacen falta los atributos (hijos)?
                    // val user: Usuario? = singleSnapshot.getValue(Usuario::class.java)

                    //Cogemos los datos del usuario
                    /* val nombre: String =
                        singleSnapshot.child("nombre").getValue(String::class.java)
                            .toString()
                    */
                    val nombre: String =
                        dataSnapshot.child("nombre").getValue(String::class.java)
                            .toString()
                    val apellidos: String =
                        dataSnapshot.child("apellidos").getValue(String::class.java)
                            .toString()
                    val email: String =
                        dataSnapshot.child("email").getValue(String::class.java)
                            .toString()
                    val password: String =
                        dataSnapshot.child("password").getValue(String::class.java)
                            .toString()
                    val telefono: String =
                        dataSnapshot.child("telefono").getValue(Int::class.java)
                            .toString()
                    val direccion: String =
                        dataSnapshot.child("direccion").getValue(String::class.java)
                            .toString()
                    val provincia: String =
                        dataSnapshot.child("provincia").getValue(String::class.java)
                            .toString()
                    val ciudad: String =
                        dataSnapshot.child("ciudad").getValue(String::class.java)
                            .toString()

                    Log.d(
                        "TAG", currentuserUID + " / " + nombre + " / " + apellidos + " / "
                                + email + " / " + password + " / " + telefono + " / " + direccion + " / "
                                + provincia + " / " + ciudad + " / "
                    )

                    //Completamos al usuario
                    user = Usuario(
                        nombre,
                        apellidos,
                        email,
                        password,
                        telefono.toInt(),
                        provincia,
                        ciudad,
                        direccion
                    )

                } else {
                    //Si no se encuentra, es que no existe y hay que crearlo
                    //El UID del usuario ya está en currentUser por lo que ya se puede añadir a la
                    //base de datos de Usuarios

                    //CUIDADO! Para no repetir proceso cada vez que usuario inicia sesion
                    //hacer un filtrado a la base de datos de Usuario si se repite el UID


                    //Meter en la base de datos el usuario a registrar
                    database = FirebaseDatabase.getInstance().getReference("Usuarios")
                    val usuario = Usuario(
                        Register.nombre2,
                        Register.apellido2,
                        Register.email2,
                        Register.password2,
                        Register.telefono2,
                        Register.provincia2,
                        Register.ciudad2,
                        Register.direccion2
                    )

                    //Indicar que se ha realizado correctamente
                    database.child(currentuserUID.toString()).setValue(usuario)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext, "Bienvenido " + Register.nombre2 + " " +
                                        Register.apellido2 + ".", Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Ha habido un error en el registro",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    //}
                    //  }


                    /*}else{
                    Toast.makeText(applicationContext,"NO SE ENCONTRARON DATOS",Toast.LENGTH_LONG).show();
                }

            }*/
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(
                    this@MainActivity,
                    "No se pudieron recuperar los datos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    fun redondeo(num: Double): Double {
        return (num * 100.00).roundToInt() / 100.00
    }

    fun numeroEUR(num: Double): String {
        val nf = NumberFormat.getInstance(Locale.GERMAN)
        var string = nf.format(num)
        return string
    }

    //Me va a pasar todas las facturas
    fun leerFactura() {


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

                                val unidadescompradas =
                                    singleSnapshot.child("unidadesCliente").getValue().toString()
                                        .toInt()
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
                            if (emailuser.equals(user.email.toString())) {
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
                        this@MainActivity,
                        "No se pudieron recuperar los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        } else {
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu);
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item);

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        when (item.itemId) {
            R.id.gridview_mode -> {
                Toast.makeText(applicationContext, "Vista imágenes", Toast.LENGTH_SHORT).show()
                lvComponentesConImagen.setAdapter(null)
                gvComponentesConImagen.setAdapter(adapterComponentes)
            }
            R.id.listview_mode -> {
                Toast.makeText(applicationContext, "Vista lista de productos", Toast.LENGTH_SHORT)
                    .show()
                lvComponentesConImagen.setAdapter(adapterComponentes)
                gvComponentesConImagen.setAdapter(null)
            }
            R.id.admin -> {
                if (currentuserUID.equals("HMCGaBYTXMdWK8N0JeW5TxD34ng1")) {

                    //abrir el Activity
                    Toast.makeText(this, "Usuario admin: Bienvenido", Toast.LENGTH_SHORT).show()
                    var i: Intent = Intent(this, AdminProductos::class.java)
                    startActivity(i)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage("Usuario no tiene los permisos")
                    builder.setPositiveButton("Aceptar", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }

            R.id.ordenAsc -> {
                if (componentesArrayList.isNotEmpty()) {
                    ordenarMenorPrecio(componentesArrayList)
                    adapterComponentes.notifyDataSetChanged()
                }
                if (filtroArrayList.isNotEmpty()) {
                    ordenarMenorPrecio(filtroArrayList)
                    adapterComponentes.notifyDataSetChanged()
                }
                if (arrayFavoritos.isNotEmpty()) {
                    ordenarMenorPrecio(arrayFavoritos as ArrayList<ComponenteConImagen>)
                    adapterComponentes.notifyDataSetChanged()
                }
            }

            R.id.ordenDesc -> {
                if (componentesArrayList.isNotEmpty()) {
                    ordenarMayorPrecio(componentesArrayList)
                    adapterComponentes.notifyDataSetChanged()
                }
                if (filtroArrayList.isNotEmpty()) {
                    ordenarMayorPrecio(filtroArrayList)
                    adapterComponentes.notifyDataSetChanged()
                }
                if (arrayFavoritos.isNotEmpty()) {
                    ordenarMayorPrecio(arrayFavoritos as ArrayList<ComponenteConImagen>)
                    adapterComponentes.notifyDataSetChanged()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }


//Ordenar de mayor-menor

    fun ordenarMayorPrecio(list: ArrayList<ComponenteConImagen>) {
        lateinit var aux: ComponenteConImagen
        for (x in 0 until list.size) {
            for (y in 0 until list.size) {
                if (list[x].precio > list[y].precio) {
                    aux = list[x]
                    list[x] = list[y]
                    list[y] = aux
                }
            }
        }
    }

    fun ordenarMenorPrecio(list: ArrayList<ComponenteConImagen>) {
        lateinit var aux: ComponenteConImagen
        for (x in 0 until list.size) {
            for (y in 0 until list.size) {
                if (list[x].precio < list[y].precio) {
                    aux = list[x]
                    list[x] = list[y]
                    list[y] = aux
                }
            }
        }
    }

    fun updateNavHeader() {

        //Firebase
        fstorage = FirebaseStorage.getInstance()
        storageref = fstorage.reference

        firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference("Usuarios")

        var navview: NavigationView = findViewById(R.id.navview)
        var headerView: View = navview.getHeaderView(0)
        var username: TextView = headerView.findViewById(R.id.username)
        var usermail: TextView = headerView.findViewById(R.id.mailuser)




        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (singleSnapshot in dataSnapshot.children) {

                        if(singleSnapshot.child("email").getValue().toString().equals(Login.correo)) {
                            username.text =
                                singleSnapshot.child("nombre").getValue()
                                    .toString() + " " + singleSnapshot.child("apellidos").getValue()
                                    .toString()

                            usermail.text = singleSnapshot.child("email").getValue().toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })







    }

}

