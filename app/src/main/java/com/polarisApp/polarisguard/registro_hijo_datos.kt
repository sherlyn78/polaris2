package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class registro_hijo_datos : AppCompatActivity() {
    var passwordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_hijo_datos) // Conecta con registro_hijo_datos.xml

        val btnregistrarHijo = findViewById<Button>(R.id.btn_RegistroUsuarioHijo)
        val edtxCorreoHijo = findViewById<EditText>(R.id.edtx_correo_hijo)
        val edtxpasswordHijo = findViewById<EditText>(R.id.edtx_contrasena_hijo)
        val edtxTelefonoHijo = findViewById<EditText>(R.id.edtx_telefonoHijo)
        val ImgvPasword = findViewById<ImageView>(R.id.ivTogglePasswordH)

        //boton para continuar con el registro
        btnregistrarHijo.setOnClickListener(View.OnClickListener { v: View? ->

            //guardar los datos en SharedPreferences
            val prefs = getSharedPreferences("datos_usuario_hijo", MODE_PRIVATE)
            prefs.edit().putString("CorreroHijo", edtxCorreoHijo.getText().toString()).apply()
            prefs.edit().putString("passwordHijo", edtxpasswordHijo.getText().toString()).apply()
            prefs.edit().putString("telefonoHijo", edtxTelefonoHijo.getText().toString()).apply()

            val intent = Intent(v!!.getContext(), registro_hijo_perfil::class.java)
            v.getContext().startActivity(intent)
        })


        //mostrar oculatar la contraeña
        ImgvPasword.setOnClickListener(View.OnClickListener { v: View? ->
            if (passwordVisible) {
                // Ocultar contraseña
                edtxpasswordHijo.setTransformationMethod(PasswordTransformationMethod.getInstance())
                ImgvPasword.setImageResource(R.drawable.icon_visibility_off)
                passwordVisible = false
            } else {
                // Mostrar contraseña
                edtxpasswordHijo.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                ImgvPasword.setImageResource(R.drawable.icon_visibility_on)
                passwordVisible = true
            }
            // Mantener el cursor al final
            edtxpasswordHijo.setSelection(edtxpasswordHijo.getText().length)
        })
    }
}