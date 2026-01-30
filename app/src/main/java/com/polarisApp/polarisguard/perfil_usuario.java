package com.polarisApp.polarisguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class perfil_usuario extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario); //Conecta con perfil_usuario.xml

        ImageButton imgb_volver = findViewById(R.id.imgbtn_anterior_aperfil);

        EditText nombre_padre = findViewById(R.id.perfil_nombrepadre);
        EditText nombre_hijo = findViewById(R.id.perfil_nombre_hijo);
        EditText telefono_padre = findViewById(R.id.perfil_numero_Padre);
        EditText telefono_hijo = findViewById(R.id.perfil_numero_Hijo);

        TextView txtv_correo_padre = findViewById(R.id.perfil_correo_user);

        imgb_volver.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), pantalla_inicio_mapa.class);
            v.getContext().startActivity(intent);
            finish();
        });

        SharedPreferences datos = getSharedPreferences("datos_usuario", MODE_PRIVATE);
        String nombrep = datos.getString("NombrePadre", "");
        String apellidop = datos.getString("ApellPadre", "");
        String NombreHijo = datos.getString("NombreHijo_Padre", "");
        String ApellidoHijo = datos.getString("ApellHijo_Padre", "");
        String correo_padre = datos.getString("CorreoPadre", "");
        String Telefono_hijo = datos.getString("TelefonoHijo_Padre", "");
        String TelefonoPadre = datos.getString("TelefonoPadre", "");

        nombre_padre.setText(nombrep + " " + apellidop);
        nombre_hijo.setText(NombreHijo + " " + ApellidoHijo);
        txtv_correo_padre.setText(correo_padre);
        telefono_hijo.setText(Telefono_hijo);
        telefono_padre.setText(TelefonoPadre);


    }
}
