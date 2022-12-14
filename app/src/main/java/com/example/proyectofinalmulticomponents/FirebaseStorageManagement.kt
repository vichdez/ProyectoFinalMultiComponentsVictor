package com.example.proyectofinalmulticomponents

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.Uri
import android.content.Context
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageManagement {
    private val TAG = "FirebaseStorageManagement"
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog

    companion object{
        public var Storageurl: String=""
    }


    @SuppressLint("LongLogTag", "SuspiciousIndentation")
    fun subirImagen(mContext: Context, imageURI: Uri){
        mProgressDialog = ProgressDialog(mContext)
        //tarda
        mProgressDialog.setMessage("Por favor, espera, producto subiéndose...")
        mProgressDialog.show()
        val uploadTask = mStorageRef.child("Componentes/"+ AdminProductos.nombreSring +".png").putFile(imageURI)
        uploadTask.addOnSuccessListener {
            //Conseguido
            Log.e(TAG, "Imagen subida")
            //Descargar y convertirla a URL
            val downloadurltask = mStorageRef.child("Componentes/"+ AdminProductos.nombreSring +".png").downloadUrl;

            Storageurl = downloadurltask.toString()
                downloadurltask.addOnSuccessListener {
                    Log.e(TAG, "Imagen path: $it")
                    mProgressDialog.dismiss()
                }.addOnFailureListener {
                    mProgressDialog.dismiss()
                }
        }.addOnFailureListener{
            Log.e(TAG, "Imagen no subida ${it.printStackTrace()}")
            mProgressDialog.dismiss()
        }
    }
}