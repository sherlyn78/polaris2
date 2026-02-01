package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class notificaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notificaciones) // Conecta con perfil_usuario.xml


        val imgb_Inicio = findViewById<ImageButton>(R.id.btn_Inicio)
        val imgb_Mapa = findViewById<ImageButton>(R.id.btn_mapa)
        val imgb_Perfil = findViewById<ImageButton>(R.id.btn_perfil)
        val imbgtn_volver = findViewById<ImageButton>(R.id.btn_Notificaciones_anterior)

        imbgtn_volver.setOnClickListener(View.OnClickListener { v: View? ->
            finish()
        })

        imgb_Inicio.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), pantalla_inicio_mapa::class.java)
            v.getContext().startActivity(intent)
            finish()
        })

        imgb_Mapa.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), mapa_padre::class.java)
            v.getContext().startActivity(intent)
            finish()
        })

        imgb_Perfil.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), perfil_usuario::class.java)
            v.getContext().startActivity(intent)
            finish()
        })
    }
}
