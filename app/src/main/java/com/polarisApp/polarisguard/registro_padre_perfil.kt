package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class registro_padre_perfil : AppCompatActivity() {
    private var NombrePadre: String? = null
    private var NombreHijo: String? = null
    private var telefonoPadre: String? = null
    private var correoPadre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_padre_perfil) // Conecta con activity_main.xml

        val btnPerfilContinuar = findViewById<Button>(R.id.btn_perfilContinuarRegistro)
        val userPadre = findViewById<EditText>(R.id.perfil_nombrepadre)
        val hijo = findViewById<EditText>(R.id.nombreHijoP)
        val telefono = findViewById<EditText>(R.id.perfil_telefono)
        val edtxCorreo_Padre = findViewById<TextView>(R.id.correoPadre)

        //recuperando los datos del registro global
        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        NombrePadre = prefs.getString("NombrePadre", "")
        NombreHijo = prefs.getString("NombreHijo_Padre", "")
        telefonoPadre = prefs.getString("TelefonoPadre", "")
        correoPadre = prefs.getString("CorreoPadre", "")


        //cargando los datos en el editext
        userPadre.setText(NombrePadre)
        hijo.setText(NombreHijo)
        telefono.setText(telefonoPadre)
        edtxCorreo_Padre.setText(correoPadre)

        btnPerfilContinuar.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), registropadre_terminos::class.java)
            v.getContext().startActivity(intent)
        })
    }
}
