package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class registro_hijo_perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_hijo_perfil) // Conecta con activity_main.xml


        val btnContinuarRegistroHijo = findViewById<Button>(R.id.btn_continuarRegistro)

        val edxt_usuario = findViewById<EditText?>(R.id.edtx_usuarioHijo)
        val edxt_correoHijo = findViewById<EditText>(R.id.edtx_usuarioHijo)
        val edxt_telefonoHijo = findViewById<EditText?>(R.id.edtx_usuarioHijo)
        val edxt_padreTutor = findViewById<EditText?>(R.id.edtx_usuarioHijo)
        val edxt_telefonoTutor = findViewById<EditText?>(R.id.edtx_usuarioHijo)
        val edxt_correoTutor = findViewById<EditText?>(R.id.edtx_usuarioHijo)

        //recuperando los datos del registro global -- datos_usuario_hijo
        val prefs = getSharedPreferences("datos_usuario_hijo", MODE_PRIVATE)

        //mostramos en el perfil los datos almacenados
        edxt_correoHijo.setText(prefs.getString("CorreroHijo", ""))
        edxt_correoHijo.setText(prefs.getString("telefonoHijo", ""))

        //llamar a la funcion que muestra los demas datos del hijo guardados desde la base de datos
        btnContinuarRegistroHijo.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), registro_hijo_terminos::class.java)
            v.getContext().startActivity(intent)
        })
    }
}
