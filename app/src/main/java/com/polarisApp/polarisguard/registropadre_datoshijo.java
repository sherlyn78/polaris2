package com.polarisApp.polarisguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class registropadre_datoshijo extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registropadre_datoshijo); // Conecta con activity_main.xml


        Button btn_RegistrarDatosHijo = findViewById(R.id.btn_RegistroDatosHijo);
        EditText edtxNombreHijo_padre = findViewById(R.id.edtxNombreHijo_padre);
        EditText edtxApellidoHijo = findViewById(R.id.btn_apellidosHijo_padre);
        EditText edtxEdadHijo_Padre = findViewById(R.id.btn_edadHijo_padre);
        EditText edtxCorreoHijo_Padre = findViewById(R.id.btn_correoHijo_padre);
        EditText edtxTelefonoHijo_Padre = findViewById(R.id.btn_telefonoHijo_padre);

        btn_RegistrarDatosHijo.setOnClickListener(v -> {
            // almacenar en SharedPreferences para su uso global en la app
            SharedPreferences prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE);
            prefs.edit().putString("NombreHijo_Padre", edtxNombreHijo_padre.getText().toString()).apply();
            prefs.edit().putString("ApellHijo_Padre", edtxApellidoHijo.getText().toString()).apply();
            prefs.edit().putString("EdadHijo_Padre", edtxEdadHijo_Padre.getText().toString()).apply();
            prefs.edit().putString("CorreoHijo_Padre", edtxCorreoHijo_Padre.getText().toString()).apply();
            prefs.edit().putString("TelefonoHijo_Padre", edtxTelefonoHijo_Padre.getText().toString()).apply();


            Intent intent = new Intent(v.getContext(), registro_padre_perfil.class);
            v.getContext().startActivity(intent);
        });

    }
}
