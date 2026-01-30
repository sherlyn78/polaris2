package com.polarisApp.polarisguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class registro_hijo_datos extends AppCompatActivity{

    boolean passwordVisible = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_hijo_datos); // Conecta con registro_hijo_datos.xml

        Button btnregistrarHijo = findViewById(R.id.btn_RegistroUsuarioHijo);
        EditText edtxCorreoHijo = findViewById(R.id.edtx_correo_hijo);
        EditText edtxpasswordHijo = findViewById(R.id.edtx_contrasena_hijo);
        EditText edtxTelefonoHijo = findViewById(R.id.edtx_telefonoHijo);
        ImageView ImgvPasword = findViewById(R.id.ivTogglePasswordH);

        //boton para continuar con el registro
        btnregistrarHijo.setOnClickListener(v -> {

            //guardar los datos en SharedPreferences
            SharedPreferences prefs = getSharedPreferences("datos_usuario_hijo", MODE_PRIVATE);
            prefs.edit().putString("CorreroHijo", edtxCorreoHijo.getText().toString()).apply();
            prefs.edit().putString("passwordHijo", edtxpasswordHijo.getText().toString()).apply();
            prefs.edit().putString("telefonoHijo", edtxTelefonoHijo.getText().toString()).apply();

            Intent intent = new Intent(v.getContext(), registro_hijo_perfil.class);
            v.getContext().startActivity(intent);
        });


        //mostrar oculatar la contraeña
        ImgvPasword.setOnClickListener(v -> {
            if (passwordVisible) {
                // Ocultar contraseña
                edtxpasswordHijo.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ImgvPasword.setImageResource(R.drawable.icon_visibility_off);
                passwordVisible = false;
            } else {
                // Mostrar contraseña
                edtxpasswordHijo.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ImgvPasword.setImageResource(R.drawable.icon_visibility_on);
                passwordVisible = true;
            }
            // Mantener el cursor al final
            edtxpasswordHijo.setSelection(edtxpasswordHijo.getText().length());
        });
    }
}