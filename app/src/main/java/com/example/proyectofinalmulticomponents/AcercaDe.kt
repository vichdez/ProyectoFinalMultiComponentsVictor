package com.example.proyectofinalmulticomponents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AcercaDe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca_de)

        if(supportActionBar !=null)
            this.supportActionBar?.hide();
    }

    fun abrirNavigation(view: View){
        var i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}