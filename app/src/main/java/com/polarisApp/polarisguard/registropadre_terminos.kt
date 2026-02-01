package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class registropadre_terminos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registropadre_terminos) // Conecta con activity_main.xml

        val btnAccederSesion = findViewById<Button>(R.id.btnAccederSesion)

        btnAccederSesion.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), registro_padre_exitoso::class.java)
            v.getContext().startActivity(intent)
        })
    }
}
