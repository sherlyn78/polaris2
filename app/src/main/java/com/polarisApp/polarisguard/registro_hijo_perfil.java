package com.polarisApp.polarisguard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class registro_hijo_perfil extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_hijo_perfil); // Conecta con activity_main.xml


        Button btnContinuarRegistroHijo = findViewById(R.id.btn_continuarRegistro);

        EditText edxt_usuario = findViewById(R.id.edtx_usuarioHijo);
        EditText edxt_correoHijo = findViewById(R.id.edtx_usuarioHijo);
        EditText edxt_telefonoHijo = findViewById(R.id.edtx_usuarioHijo);
        EditText edxt_padreTutor = findViewById(R.id.edtx_usuarioHijo);
        EditText edxt_telefonoTutor = findViewById(R.id.edtx_usuarioHijo);
        EditText edxt_correoTutor = findViewById(R.id.edtx_usuarioHijo);

        //recuperando los datos del registro global -- datos_usuario_hijo
        SharedPreferences prefs = getSharedPreferences("datos_usuario_hijo", MODE_PRIVATE);

        //mostramos en el perfil los datos almacenados
        edxt_correoHijo.setText(prefs.getString("CorreroHijo", ""));
        edxt_correoHijo.setText(prefs.getString("telefonoHijo",""));

        //llamar a la funcion que muestra los demas datos del hijo guardados desde la base de datos

        btnContinuarRegistroHijo.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), registro_hijo_terminos.class);
            v.getContext().startActivity(intent);
        });

    }
}
