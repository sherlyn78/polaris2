package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class registro_hijo_terminos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_hijo_terminos) // Conecta con registro_hijo_terminos.xml


        val btnIniciarAccesoCuenta = findViewById<Button>(R.id.btn_Terminos)
        val imgbtn_Volver =
            findViewById<ImageButton?>(R.id.imgbtn_anterior) //al presionar volver cierra la actividad actual, abre pantalla anterior

        btnIniciarAccesoCuenta.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), registro_hijo_exitoso::class.java)
            v.getContext().startActivity(intent)
        })
    }
}
