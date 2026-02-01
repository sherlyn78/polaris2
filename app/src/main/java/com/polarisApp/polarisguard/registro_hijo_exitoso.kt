package com.polarisApp.polarisguard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NoConnectionError
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class registro_hijo_exitoso : AppCompatActivity() {
    //url de regisro, Api php, registrar en la bd  ----> registro.php
    var url_registro: String =
        "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/registro_hijo.php"

    //variables para recibir los datos de SharedPreferences
    protected var correo: String? = null
    protected var telefono: String? = null
    protected var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_hijo_exitoso) // Conecta con registro_hijo_exitoso.xml

        val FinalizarRegistro = findViewById<Button>(R.id.btn_FinalizaRegistro)

        FinalizarRegistro.setOnClickListener(View.OnClickListener { v: View? ->
            registrarUsuarioHijo()
        })
    }


    //metodo para enviar registros a la base de datos.
    private fun registrarUsuarioHijo() {
        //recibir los datos guradados de SharedPreferences de las pantallas anteriores
        //recuperando los datos del registro global
        val prefs = getSharedPreferences("datos_usuario_hijo", MODE_PRIVATE)
        correo = prefs.getString("CorreroHijo", "")
        telefono = prefs.getString("telefonoHijo", "")
        password = prefs.getString("passwordHijo", "")

        //Validar datos completos antes de enviar al servidor
        // Validar si algún campo está vacío
        val camposFaltantes = StringBuilder()

        if (correo!!.isEmpty()) camposFaltantes.append("Nombre, ")
        if (telefono!!.isEmpty()) camposFaltantes.append("Apellido, ")
        if (password!!.isEmpty()) camposFaltantes.append("Apellido, ")

        if (camposFaltantes.length > 0) {
            // Eliminar la última coma y espacio
            val mensaje = "Faltan los siguientes campos: " +
                    camposFaltantes.substring(0, camposFaltantes.length - 2)
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            return
        }


        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url_registro,
            Response.Listener { response: String? ->
                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("status")

                    if (status == "ok") {
                        val mensaje = jsonObject.getString("mensaje")

                        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

                        //si la conexion a la api es correctamente, enviar a la pantalla loguin
                        val intent = Intent(this, login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMsg = jsonObject.getString("mensaje")
                        Toast.makeText(this, "Error: " + errorMsg, Toast.LENGTH_LONG).show()

                        val intent = Intent(this, crear_cuenta::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al procesar respuesta JSON", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error: VolleyError? ->
                //Toast.makeText(this, "Error de red: " + error.toString(), Toast.LENGTH_LONG).show();
                val mensajeError: String?
                if (error is TimeoutError) {
                    mensajeError = "Tiempo de espera agotado. Verifica tu conexión."
                } else if (error is NoConnectionError) {
                    mensajeError = "Sin conexión a Internet. Revisa tu red."
                } else {
                    mensajeError = "Error de red inesperado: " + error.toString()
                }
                Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show()

                // Mostrar detalles en Logcat
                error!!.printStackTrace() // muestra la traza completa

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    val body = String(error.networkResponse.data)
                    Log.e("VolleyErrorBody", body) // muestra el cuerpo de la respuesta del servidor
                } else {
                    Log.e("VolleyError", "Sin respuesta del servidor o sin datos")
                }

                Log.e("VolleyError", "Tipo: " + error.javaClass.getSimpleName())
                Log.e("VolleyError", "Mensaje: " + error.message)
            }) {
            //se procede a enviar los datos por el metodo post a la Api php
            //verificar que en la api estan los mismos parametros
            override fun getParams(): MutableMap<String?, String?> {
                val params: MutableMap<String?, String?> = HashMap<String?, String?>()
                params.put("correo", correo)
                params.put("password", password)
                params.put("telefono", telefono)
                return params
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add<String?>(stringRequest)
    }
}
