package com.polarisApp.polarisguard;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
public class registro_hijo_terminos extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_hijo_terminos); // Conecta con registro_hijo_terminos.xml


        Button btnIniciarAccesoCuenta = findViewById(R.id.btn_Terminos);
        ImageButton imgbtn_Volver = findViewById(R.id.imgbtn_anterior); //al presionar volver cierra la actividad actual, abre pantalla anterior

        btnIniciarAccesoCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), registro_hijo_exitoso.class);
            v.getContext().startActivity(intent);
        });

    }
}
