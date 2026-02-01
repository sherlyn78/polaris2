package com.polarisApp.polarisguard

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class activity_main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Conecta con activity_main.xml


        val btnAcceder = findViewById<Button>(R.id.btn_bienvenidoAcceder)
        val imgv_carrusel = findViewById<ImageView>(R.id.imgCarrucel)

        //iniciar el carrusel
        val carrusel = imgv_carrusel.getDrawable() as AnimationDrawable
        carrusel.start()

        btnAcceder.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), login::class.java)
            v.getContext().startActivity(intent)
        })
    }
}
