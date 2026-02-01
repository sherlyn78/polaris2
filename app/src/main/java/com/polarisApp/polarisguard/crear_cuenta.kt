package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class crear_cuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_cuenta) // Conecta con activity_main.xml

        val btnregistrarPadre = findViewById<Button>(R.id.btn_reg_userPadre)
        val btnRegistrarHijo = findViewById<Button>(R.id.btn_reg_userHijo)


        //crear cuenta del padre
        btnregistrarPadre.setOnClickListener(View.OnClickListener { v: View? ->
            val prefs = getSharedPreferences("cuenta_creada", MODE_PRIVATE)
            val cuenta: String = prefs.getString("CuentaCreadaEnDispositivo", "")!!

            //si ya existe una cuenta hijo, no puede crear una cuenta padre
            if (cuenta === "2") {
                Toast.makeText(this, "Este dispositivo tiene una cuenta hijo", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(v!!.getContext(), registro_padre_datos::class.java)
                v.getContext().startActivity(intent)
            }
        })

        //crear cuenta del hijo
        btnRegistrarHijo.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), registro_hijo_datos::class.java)
            v.getContext().startActivity(intent)
        })
    }
}
