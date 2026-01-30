package com.polarisApp.polarisguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import  androidx.appcompat.app.AppCompatActivity;

public class registro_padre_perfil extends AppCompatActivity  {

    private String NombrePadre, NombreHijo, telefonoPadre, correoPadre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_padre_perfil); // Conecta con activity_main.xml

        Button btnPerfilContinuar = findViewById(R.id.btn_perfilContinuarRegistro);
        EditText userPadre = findViewById(R.id.perfil_nombrepadre);
        EditText hijo = findViewById(R.id.nombreHijoP);
        EditText telefono = findViewById(R.id.perfil_telefono);
        TextView edtxCorreo_Padre = findViewById(R.id.correoPadre);

        //recuperando los datos del registro global
        SharedPreferences prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE);
        NombrePadre = prefs.getString("NombrePadre", "");
        NombreHijo = prefs.getString("NombreHijo_Padre", "");
        telefonoPadre = prefs.getString("TelefonoPadre", "");
        correoPadre = prefs.getString("CorreoPadre", "");


        //cargando los datos en el editext
        userPadre.setText(NombrePadre);
        hijo.setText(NombreHijo);
        telefono.setText(telefonoPadre);
        edtxCorreo_Padre.setText(correoPadre);

        btnPerfilContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), registropadre_terminos.class);
            v.getContext().startActivity(intent);
        });
    }
}
