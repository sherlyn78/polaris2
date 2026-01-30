package com.polarisApp.polarisguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class crear_cuenta extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta); // Conecta con activity_main.xml

        Button btnregistrarPadre = findViewById(R.id.btn_reg_userPadre);
        Button btnRegistrarHijo = findViewById(R.id.btn_reg_userHijo);
        

        //crear cuenta del padre
        btnregistrarPadre.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("cuenta_creada", MODE_PRIVATE);
            String cuenta = prefs.getString("CuentaCreadaEnDispositivo", "");

            //si ya existe una cuenta hijo, no puede crear una cuenta padre
            if(cuenta == "2"){
                Toast.makeText(this, "Este dispositivo tiene una cuenta hijo", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(v.getContext(), registro_padre_datos.class);
                v.getContext().startActivity(intent);
            }
        });

        //crear cuenta del hijo
        btnRegistrarHijo.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), registro_hijo_datos.class);
            v.getContext().startActivity(intent);
        });
    }
}
