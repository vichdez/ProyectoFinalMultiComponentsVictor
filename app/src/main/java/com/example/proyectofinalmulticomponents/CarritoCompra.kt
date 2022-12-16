package com.example.proyectofinalmulticomponents

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinalmulticomponents.clases.ComponenteConImagen
import com.example.proyectofinalmulticomponents.clases.Factura
import com.example.proyectofinalmulticomponents.customAdapters.ComponentesconImagenAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CarritoCompra : AppCompatActivity() {

    private lateinit var lvComponentesCarrito: ListView
    private lateinit var alert: MaterialAlertDialogBuilder
    private lateinit var btnFloat: FloatingActionButton
    private lateinit var totalCompra: TextView

    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var payButton: FloatingActionButton

    private lateinit var database: DatabaseReference
    private lateinit var databaseFac: DatabaseReference

    private var storageref: StorageReference? = null

    //main
    private lateinit var fstorage: FirebaseStorage
    private lateinit var databasecomponents: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    companion object {
        var componentesBBDD: ArrayList<ComponenteConImagen> = ArrayList()
        lateinit var adapterCarrito: ComponentesconImagenAdapter
        private const val TAG = "CarritoCompra"
        private const val BACKEND_URL = "https://stripe-multicomponents.herokuapp.com"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito_compra)

        assert(supportActionBar != null)
        actionBar?.setDisplayHomeAsUpEnabled(true);

        supportActionBar?.setTitle("")

        if(supportActionBar !=null)
            this.supportActionBar?.hide();

        lvComponentesCarrito = findViewById(R.id.lyComponentesCarrito)
        totalCompra = findViewById(R.id.tvTotalC)
        payButton = findViewById(R.id.fabPagar)
        payButton.setOnClickListener(::onPayClicked)
        payButton.isEnabled = false
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)



        adapterCarrito = ComponentesconImagenAdapter(this, componentesBBDD, true)
        lvComponentesCarrito.adapter = adapterCarrito


        //Si no hay compra no aparece el boton de pago
        if (componentesBBDD.isEmpty()) {
            payButton.visibility = INVISIBLE
        } else {
            payButton.visibility = VISIBLE
            fetchPaymentIntent()
        }

        //Quitar del carrito
        lvComponentesCarrito.setOnItemClickListener { parent, view, position, id ->


            alert = MaterialAlertDialogBuilder(this)
            alert.setTitle("Sacar del Carrito")
            alert.setMessage("Quiere quitar un articulo ${componentesBBDD[position].nombre} del carrito?")
            alert.setPositiveButton("Quitar") { _, _ ->
                if(componentesBBDD[position].unidadesCliente<=1) {
                    componentesBBDD[position].unidadesCliente=componentesBBDD[position].unidadesCliente-1;
                    adapterCarrito.notifyDataSetChanged()
                    MainActivity.componentesArrayList.add(componentesBBDD[position])
                    componentesBBDD.removeAt(position)
                    MainActivity.adapterComponentes.notifyDataSetChanged()
                }
                else{
                    componentesBBDD[position].unidadesCliente=componentesBBDD[position].unidadesCliente-1;
                    adapterCarrito.notifyDataSetChanged()
                }


                suma()
                fetchPaymentIntent()

                if (componentesBBDD.isEmpty()) {
                    payButton.visibility = INVISIBLE
                } else {
                    payButton.visibility = VISIBLE
                }
            }
            alert.setNegativeButton("Cancelar") { _, _ ->

            }
            alert.show()
        }


/*
        //Pulsar para pagar
        btnFloat.setOnClickListener {
            alert = MaterialAlertDialogBuilder(this)
            alert.setTitle("Total:")
            alert.setMessage("Total a pagar :  ${suma()}")
            alert.setPositiveButton("Pagar") { _, _ ->

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateString = simpleDateFormat.format(System.currentTimeMillis())

                var fecha:String = String.format("Date: %s", dateString)
                var array:ArrayList<ComponenteConImagen> = ArrayList()
                for (com:ComponenteConImagen in componentesBBDD){
                    array.add(com)
                }
                var fact = Factura(MainActivity.user, fecha,array )

                Facturas.facturaBBDD.add(fact)
                componentesBBDD.clear()
                adapterCarrito.notifyDataSetChanged()

            }
            alert.setNegativeButton("Cancelar"){_,_ ->

            }
            alert.show()

        }

 */


    }

    fun suma(): Double {
        var suma = 0.0
        for (c: ComponenteConImagen in componentesBBDD) {
            suma += c.precio * c.unidadesCliente
        }
        totalCompra.text = "${numeroEUR(redondeo(suma))} €"
        return suma
    }


    fun redondeo(num: Double): Double {
        return (num * 100.00).roundToInt() / 100.00
    }


    fun abrirNavigation(view: View) {
        var i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    //El intento de pago
    private fun fetchPaymentIntent() {
        val url = "$BACKEND_URL/create-payment-intent"

        val amount = redondeo(suma()*100)
        val payMap: MutableMap<String, Any> = HashMap()
        val itemMap: MutableMap<String, Any> = HashMap()
        val itemList: MutableList<Map<String, Any>> = ArrayList()

        payMap["currency"] = "eur"
        itemMap["id"] = "photo_suscription"
        itemMap["amount"] = amount
        itemList.add(itemMap)
        payMap["items"] = itemList

        val shoppingCartContent = Gson().toJson(payMap)

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val body = shoppingCartContent.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient()
            .newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    showAlert("Failed to load data", "Error: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        showAlert("Failed to load page", "Error: $response")
                    } else {
                        val responseData = response.body?.string()
                        val responseJson = responseData?.let { JSONObject(it) } ?: JSONObject()
                        paymentIntentClientSecret = responseJson.getString("clientSecret")
                        runOnUiThread { payButton.isEnabled = true }
                        //Log.i(TAG, "Retrieved PaymentIntent")
                    }
                }
            })
    }

    private fun showAlert(title: String, message: String? = null) {
        runOnUiThread {
            if (!isFinishing) {
                val builder = AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                builder.setPositiveButton("Ok", null)
                builder.create().show()
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showSnackBar(message: String) {
        runOnUiThread {
            var mySnackbar = Snackbar.make(
                findViewById(R.id.lyComponentesCarrito),
                message,
                Snackbar.LENGTH_LONG
            )
            mySnackbar.show()
        }
    }

    private fun onPayClicked(view: View) {
        val configuration = PaymentSheet.Configuration("Example, Inc.")

        for (com: ComponenteConImagen in componentesBBDD) {
            if((com.unidades-com.unidadesCliente)<0){
                Toast.makeText(
                    applicationContext,
                    "No quedan suficientes productos en stock. Borra o baja el numero",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }

    //Cuando estas por terminar de comprar
    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            //Si resulta completado el pago
            is PaymentSheetResult.Completed -> {
                showToast("Pago completado")
                //Fecha para la factura
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateString = simpleDateFormat.format(System.currentTimeMillis())
                var fecha: String = String.format("%s", dateString)

                //Creamos array de componentes para la factura
                var array: ArrayList<ComponenteConImagen> = ArrayList()
                //Los componentes del arrayList static los metemos en el nuevo array
                for (com: ComponenteConImagen in componentesBBDD) {
                    array.add(com)
                    var newUnidades : Int = com.unidades - com.unidadesCliente

                  //Después del filtro
                    //Actualizamos las nuevas unidades
                    databasecomponents = FirebaseDatabase.getInstance().getReference("Componentes")
                    databasecomponents.child(com.nombre).child("unidades").setValue(newUnidades);
                }
                        //abrir base de datos y actualizar


                //Creacion de objeto factura
                var fact = Factura(MainActivity.user, fecha, array)
                Facturas.idauto++

                //Se añade al arrayList de facturas
                Facturas.facturaBBDD.add(fact)

                //Creación de base de datos de Facturas
                //Meterlo en la bd firebase
                database = FirebaseDatabase.getInstance().getReference("Facturas")

                //Indicar que se ha añadido correctamente
                database.child("factura" + fact.id.toString()).setValue(fact)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Factura " + "factura" + fact.id + " agregado",
                            Toast.LENGTH_SHORT
                        ).show()
                        //Se despeja el carrito
                        componentesBBDD.clear()
                        adapterCarrito.notifyDataSetChanged()
                        payButton.isEnabled = false

                        //Volver a cargar bbdd Componentes
                        MainActivity.componentesArrayList.clear()
                        componentesBBDD()

                        //Te manda para la actividad Facturas y te muestra la factura creada
                        var i: Intent = Intent(this, Facturas::class.java)
                        startActivity(i)

                    }.addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            "Ha habido un error en el registro del producto",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

            }
            is PaymentSheetResult.Canceled -> {
                //Log.i(TAG, "Payment canceled!")
            }
            is PaymentSheetResult.Failed -> {
                showAlert("Payment failed", paymentResult.error.localizedMessage)
            }
        }
    }

    fun numeroEUR(num: Double): String {
        val nf = NumberFormat.getInstance(Locale.GERMAN)
        var string = nf.format(num)
        return string
    }


/*
    fun quitarUnidades(c : ComponenteConImagen){

        var numProductoRepetido : Int = c.unidadesCliente
        var arrayNombres : ArrayList<String> = ArrayList()

        var nombreCompElegido:String = c.nombre

        arrayNombres.add(nombreCompElegido)


/*
        for (i in 0 until arrayNombres.size){
            if(nombreCompElegido.equals(arrayNombres[i])){
                numProductoRepetido ++
            }
        }
*/

        var unidadesCompElegido : Int = c.unidades
        if((unidadesCompElegido-numProductoRepetido)>0) {
            databaseComp = FirebaseDatabase.getInstance().getReference("Componentes")
            databaseComp.child(nombreCompElegido).child("unidades")
                .setValue(unidadesCompElegido - numProductoRepetido);

            Log.d(
                TAG,c.nombre + "tenía " + c.unidades + " unidades y ahora tiene " + (unidadesCompElegido - numProductoRepetido).toString()
            )
            //Empezar login
        }else{
            //quitarlo del array
            //Y luego no leerlo
            MainActivity.componentesArrayList.remove(c)
        }
    }


 */
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
                        this@CarritoCompra,
                        "No se pudieron recuperar los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        } else {
        }

    }
}