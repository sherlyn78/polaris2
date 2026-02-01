package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class login : AppCompatActivity() {
    var passwordVisible: Boolean = false
    var edtxPasword: EditText? = null
    var edtxUser: EditText? = null

    //String URL_LOGIN = "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/loguin.php";
    var URL_LOGIN: String = "https://uzielcreador.site/Backendconection_php/loguin.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Conecta con activity_main.xml

        val btnIniciarSesion = findViewById<Button>(R.id.btn_IniciarSesion)
        val txtvcrear_cuenta = findViewById<TextView>(R.id.txtv_CrearCuena)
        edtxPasword = findViewById<EditText>(R.id.edtxPassword)
        edtxUser = findViewById<EditText>(R.id.edtxUser)
        val ImgvPasword = findViewById<ImageView>(R.id.ImgvTogglePasswordLogin)

        btnIniciarSesion.setOnClickListener(View.OnClickListener { v: View? ->
            loginUsuario() //llamar a la funcion login
        })

        txtvcrear_cuenta.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), crear_cuenta::class.java)
            v.getContext().startActivity(intent)
        })



        ImgvPasword.setOnClickListener(View.OnClickListener { v: View? ->
            if (passwordVisible) {
                // Ocultar contraseña
                edtxPasword!!.setTransformationMethod(PasswordTransformationMethod.getInstance())
                ImgvPasword.setImageResource(R.drawable.icon_visibility_off)
                passwordVisible = false
            } else {
                // Mostrar contraseña
                edtxPasword!!.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                ImgvPasword.setImageResource(R.drawable.icon_visibility_on)
                passwordVisible = true
            }
            // Mantener el cursor al final
            edtxPasword!!.setSelection(edtxPasword!!.getText().length)
        })
    }


    private fun loginUsuario() {
        val usuario = edtxUser!!.getText().toString()
        val clave = edtxPasword!!.getText().toString()

        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, URL_LOGIN,
            Response.Listener { response: String? ->
                try {
                    val json = JSONObject(response)
                    val status = json.getString("status")
                    val mensaje = json.getString("mensaje")
                    val id_user = json.getString("user_id")
                    val tipoUser = json.getString("tipoUser")

                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

                    if (status == "ok") {
                        //guardar el correro que se ingreso como identificador en la app
                        val prefs = getSharedPreferences("identificador-sesion", MODE_PRIVATE)
                        prefs.edit().putString("idCorreo", usuario.toString()).apply()
                        prefs.edit().putString("idUser", id_user.toString()).apply()
                        prefs.edit().putString("tipoUser", tipoUser.toString()).apply()


                        //si el mensaje se recibe una verificaion correcta enviar a pantalla mapa.
                        val intent = Intent(this, pantalla_inicio_mapa::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        //limpiar los editext para volver a ingresar
                        edtxUser!!.setText("")
                        edtxPasword!!.setText("")
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Error al procesar respuesta", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error: VolleyError? ->
                //Manejo de errores
                if (error is TimeoutError || error is NoConnectionError) {
                    Toast.makeText(
                        this,
                        "No se pudo conectar al servidor. Verifica tu conexión a Internet.",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (error is AuthFailureError) {
                    Toast.makeText(
                        this,
                        "Error de autenticación. Revisa tus credenciales.",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (error is ServerError) {
                    Toast.makeText(
                        this,
                        "El servidor encontró un error. Intenta más tarde.",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (error is NetworkError) {
                    Toast.makeText(
                        this,
                        "Error de red. Verifica tu conexión o VPN.",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (error is ParseError) {
                    Toast.makeText(
                        this,
                        "Error al interpretar la respuesta del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error desconocido. Intenta nuevamente.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        ) {
            override fun getParams(): MutableMap<String?, String?> {
                val params: MutableMap<String?, String?> = HashMap<String?, String?>()
                params.put("email", usuario)
                params.put("password", clave)
                return params
            }
        }


        val queue = Volley.newRequestQueue(this)
        queue.add<String?>(stringRequest)
    }
}
