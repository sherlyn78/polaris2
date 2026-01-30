package com.polarisApp.polarisguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class notificaciones extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificaciones); // Conecta con perfil_usuario.xml



        ImageButton imgb_Inicio = findViewById(R.id.btn_Inicio);
        ImageButton imgb_Mapa = findViewById(R.id.btn_mapa);
        ImageButton imgb_Perfil = findViewById(R.id.btn_perfil);
        ImageButton imbgtn_volver = findViewById(R.id.btn_Notificaciones_anterior);

        imbgtn_volver.setOnClickListener(v -> {
            finish();
        });

        imgb_Inicio.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), pantalla_inicio_mapa.class);
            v.getContext().startActivity(intent);
            finish();
        });

        imgb_Mapa.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), mapa_padre.class);
            v.getContext().startActivity(intent);
            finish();
        });

        imgb_Perfil.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), perfil_usuario.class);
            v.getContext().startActivity(intent);
            finish();
        });

    }
}
