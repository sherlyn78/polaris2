package com.polarisApp.polarisguard;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class registro_padre_datos extends AppCompatActivity {

    boolean passwordVisible = false;
    private String  NombrePadre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_padre_datos); // Conecta con activity_main.xml

        Button btnRegistrarUsuario = findViewById(R.id.btn_RegistroUsuarioPadre);
        EditText edtxNombrePadre = findViewById(R.id.edtx_nombre_padre);
        EditText edtxApellPadre = findViewById(R.id.btn_apellidos_padre);
        EditText edtxCorreoPadre = findViewById(R.id.btn_correo_padre);
        EditText edtxpasswordPadre = findViewById(R.id.btn_contrasena_padre);
        EditText edtxTelefonoPadre = findViewById(R.id.btn_numerotelefono_padre);
        ImageView ImgvPasword = findViewById(R.id.ivTogglePassword);


        //sin en dado caso no se acompleta el registro al llegar a la pantalla registro exitoso./
        //verificar el dilema de los datos que quedan almacenados en SharedPreferences hasta el momento,
        //pero que no seenvian al servidor a la base de datos.
        //es decir aqui ya se almaceno en SharedPreferences, pero no se ha mandado a la bd
        //si falla el envio, o conexxion, y redirije nuevamente aa crear cuenta.
        //y el usuaario escribe otra ves sus datos, estos valores almacenado se sobre escriben o quer pedo.
        btnRegistrarUsuario.setOnClickListener(v -> {
            // almacenar en SharedPreferences para su uso global en la app
            SharedPreferences prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE);
            prefs.edit().putString("NombrePadre", edtxNombrePadre.getText().toString()).apply();
            prefs.edit().putString("ApellPadre", edtxApellPadre.getText().toString()).apply();
            prefs.edit().putString("CorreoPadre", edtxCorreoPadre.getText().toString()).apply();
            prefs.edit().putString("PaswordPadre", edtxpasswordPadre.getText().toString()).apply();
            prefs.edit().putString("TelefonoPadre", edtxTelefonoPadre.getText().toString()).apply();
            prefs.edit().putString("tipoUser", "1".toString()).apply();    //tipo de usuario :  1 = padre, 2 = hijo.

            Intent intent = new Intent(v.getContext(), registro_padre_direccion.class);
            v.getContext().startActivity(intent);
        });


        ImgvPasword.setOnClickListener(v -> {
            if (passwordVisible) {
                // Ocultar contraseña
                edtxpasswordPadre.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ImgvPasword.setImageResource(R.drawable.icon_visibility_off);
                passwordVisible = false;
            } else {
                // Mostrar contraseña
                edtxpasswordPadre.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ImgvPasword.setImageResource(R.drawable.icon_visibility_on);
                passwordVisible = true;
            }
            // Mantener el cursor al final
            edtxpasswordPadre.setSelection(edtxpasswordPadre.getText().length());
        });

    }
}
