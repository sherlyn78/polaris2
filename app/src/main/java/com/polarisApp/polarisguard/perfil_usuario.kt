package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class perfil_usuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_usuario) //Conecta con perfil_usuario.xml

        val imgb_volver = findViewById<ImageButton>(R.id.imgbtn_anterior_aperfil)

        val nombre_padre = findViewById<EditText>(R.id.perfil_nombrepadre)
        val nombre_hijo = findViewById<EditText>(R.id.perfil_nombre_hijo)
        val telefono_padre = findViewById<EditText>(R.id.perfil_numero_Padre)
        val telefono_hijo = findViewById<EditText>(R.id.perfil_numero_Hijo)

        val txtv_correo_padre = findViewById<TextView>(R.id.perfil_correo_user)

        imgb_volver.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), pantalla_inicio_mapa::class.java)
            v.getContext().startActivity(intent)
            finish()
        })

        val datos = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        val nombrep: String = datos.getString("NombrePadre", "")!!
        val apellidop: String = datos.getString("ApellPadre", "")!!
        val NombreHijo: String = datos.getString("NombreHijo_Padre", "")!!
        val ApellidoHijo: String = datos.getString("ApellHijo_Padre", "")!!
        val correo_padre: String = datos.getString("CorreoPadre", "")!!
        val Telefono_hijo: String = datos.getString("TelefonoHijo_Padre", "")!!
        val TelefonoPadre: String = datos.getString("TelefonoPadre", "")!!

        nombre_padre.setText(nombrep + " " + apellidop)
        nombre_hijo.setText(NombreHijo + " " + ApellidoHijo)
        txtv_correo_padre.setText(correo_padre)
        telefono_hijo.setText(Telefono_hijo)
        telefono_padre.setText(TelefonoPadre)
    }
}
