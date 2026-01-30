package com.polarisApp.polarisguard;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_main extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Conecta con activity_main.xml


        Button btnAcceder = findViewById(R.id.btn_bienvenidoAcceder);
        ImageView imgv_carrusel = findViewById(R.id.imgCarrucel);

        //iniciar el carrusel
        AnimationDrawable carrusel = (AnimationDrawable) imgv_carrusel.getDrawable();
        carrusel.start();

        btnAcceder.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), login.class);
            v.getContext().startActivity(intent);
        });
    }
}
