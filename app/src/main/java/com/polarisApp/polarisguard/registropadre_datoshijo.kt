package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class registropadre_datoshijo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registropadre_datoshijo) // Conecta con activity_main.xml


        val btn_RegistrarDatosHijo = findViewById<Button>(R.id.btn_RegistroDatosHijo)
        val edtxNombreHijo_padre = findViewById<EditText>(R.id.edtxNombreHijo_padre)
        val edtxApellidoHijo = findViewById<EditText>(R.id.btn_apellidosHijo_padre)
        val edtxEdadHijo_Padre = findViewById<EditText>(R.id.btn_edadHijo_padre)
        val edtxCorreoHijo_Padre = findViewById<EditText>(R.id.btn_correoHijo_padre)
        val edtxTelefonoHijo_Padre = findViewById<EditText>(R.id.btn_telefonoHijo_padre)

        btn_RegistrarDatosHijo.setOnClickListener(View.OnClickListener { v: View? ->
            // almacenar en SharedPreferences para su uso global en la app
            val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)
            prefs.edit().putString("NombreHijo_Padre", edtxNombreHijo_padre.getText().toString())
                .apply()
            prefs.edit().putString("ApellHijo_Padre", edtxApellidoHijo.getText().toString()).apply()
            prefs.edit().putString("EdadHijo_Padre", edtxEdadHijo_Padre.getText().toString())
                .apply()
            prefs.edit().putString("CorreoHijo_Padre", edtxCorreoHijo_Padre.getText().toString())
                .apply()
            prefs.edit()
                .putString("TelefonoHijo_Padre", edtxTelefonoHijo_Padre.getText().toString())
                .apply()


            val intent = Intent(v!!.getContext(), registro_padre_perfil::class.java)
            v.getContext().startActivity(intent)
        })
    }
}
