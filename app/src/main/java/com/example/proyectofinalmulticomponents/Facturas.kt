package com.example.proyectofinalmulticomponents

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.proyectofinalmulticomponents.clases.ComponenteConImagen
import com.example.proyectofinalmulticomponents.clases.Factura
import com.example.proyectofinalmulticomponents.clases.Usuario
import com.example.proyectofinalmulticomponents.customAdapters.FacturasAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.FailureResponse
import com.gkemon.XMLtoPDF.model.SuccessResponse
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stripe.android.PaymentConfiguration

class Facturas : AppCompatActivity() {
    private lateinit var lvFacturas: ListView

    companion object {
        var facturaBBDD: ArrayList<Factura> = ArrayList()
        lateinit var adapterFactura: FacturasAdapter
        var idauto: Int = 1
    }

    private lateinit var fstorage: FirebaseStorage
    private lateinit var storageref: StorageReference
    private lateinit var databaseFac: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facturas)

        if(supportActionBar !=null)
            this.supportActionBar?.hide();


        //Firebase
        fstorage = FirebaseStorage.getInstance()
        storageref = fstorage.reference

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseFac = firebaseDatabase.getReference("Facturas")


        //empieza normal
        lvFacturas = findViewById(R.id.lyFacturas)



        //Aplicar el adaptador
        adapterFactura = FacturasAdapter(this, facturaBBDD)
        lvFacturas.adapter = adapterFactura
        adapterFactura.notifyDataSetChanged()

        //Cuando seleccionas una factura del ListView se crea el PDF
        lvFacturas.setOnItemClickListener { parent, view, position, id ->
            //Verificar la creación de PDF
            val builder = AlertDialog.Builder(this);
            builder.setTitle("Crear pdf")

            builder.setMessage("¿Desea tener el pdf de esta factura?").setCancelable(false)
                .setPositiveButton("Si") { dialog, id ->

                    //Generador de PDF
                    PdfGenerator.getBuilder()
                        .setContext(this@Facturas)
                        .fromViewSource()
                        .fromView(view)
                        .setFileName("Factura${facturaBBDD[position].id}")
                        .setFolderNameOrPath("Factura")
                        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                        .build(object : PdfGeneratorListener() {
                            override fun onFailure(failureResponse: FailureResponse) {
                                super.onFailure(failureResponse)
                            }

                            override fun showLog(log: String) {
                                super.showLog(log)
                            }

                            override fun onStartPDFGeneration() {
                                /*When PDF generation begins to start*/
                            }

                            override fun onFinishPDFGeneration() {
                                /*When PDF generation is finished*/
                            }

                            override fun onSuccess(response: SuccessResponse) {
                                super.onSuccess(response)
                            }
                        })
                }

                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()


        }
    }

    fun abrirNavigation(view: View) {
        var i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
    }



}