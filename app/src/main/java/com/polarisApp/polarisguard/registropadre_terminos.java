package com.polarisApp.polarisguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class registropadre_terminos extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registropadre_terminos); // Conecta con activity_main.xml

        Button btnAccederSesion = findViewById(R.id.btnAccederSesion);

        btnAccederSesion.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), registro_padre_exitoso.class);
            v.getContext().startActivity(intent);
        });


    }
}
