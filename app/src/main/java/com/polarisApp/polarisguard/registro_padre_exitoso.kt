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

//guardar datos en la en las preferencias
class registro_padre_exitoso : AppCompatActivity() {
    //url de regisro, Api php, registrar en la bd  ----> registro.php
    //String url_registro = "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/registro_padre.php";
    //String url_registro = "https://beige-mule-441461.hostingersite.com/Backendconection_php/registro_padre.php";
    var url_registro: String = "https://uzielcreador.site/Backendconection_php/registro_padre.php"

    //variables para recibir los datos de SharedPreferences
    protected var nombrep: String? = null
    protected var apellidop: String? = null
    protected var correop: String? = null
    protected var passwordp: String? = null
    protected var telefonop: String? = null
    protected var tipouser: String? = null
    protected var NombreHijo: String? = null
    protected var ApellidoHijo: String? = null
    protected var EdadHijo: String? = null
    protected var CorreoHijo: String? = null
    protected var TelefonoHijo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_padre_exitoso) // Conecta con activity_main.xml

        val btnFinalRegistro = findViewById<Button>(R.id.btn_iniciarsesionPadre)

        btnFinalRegistro.setOnClickListener(View.OnClickListener { v: View? ->
            registrarUsuario()
            val prefs = getSharedPreferences("cuenta_creada", MODE_PRIVATE)
            prefs.edit().putString("CuentaCreadaEnDispositivo", "1".toString())
                .apply() //1 - finalizo cuenta padre,  2 - finalizo cuenta hijo
        })
    }

    //metodo para registrarse en la base de datos tablas ----> tutor y usuariow
    private fun registrarUsuario() {
        //recibir los datos guradados de SharedPreferences de las pantallas anteriores
        //recuperando los datos del registro global

        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        nombrep = prefs.getString("NombrePadre", "")
        apellidop = prefs.getString("ApellPadre", "")
        correop = prefs.getString("CorreoPadre", "")
        passwordp = prefs.getString("PaswordPadre", "")
        telefonop = prefs.getString("TelefonoPadre", "")
        tipouser = prefs.getString("tipoUser", "") //tipo de usuario = padre
        //datos del hijo
        NombreHijo = prefs.getString("NombreHijo_Padre", "")
        ApellidoHijo = prefs.getString("ApellHijo_Padre", "")
        EdadHijo = prefs.getString("EdadHijo_Padre", "")
        CorreoHijo = prefs.getString("CorreoHijo_Padre", "")
        TelefonoHijo = prefs.getString("TelefonoHijo_Padre", "")

        //Validar datos completos antes de enviar al servidor
        // Validar si algún campo está vacío
        val camposFaltantes = StringBuilder()

        if (nombrep!!.isEmpty()) camposFaltantes.append("Nombre, ")
        if (apellidop!!.isEmpty()) camposFaltantes.append("Apellido, ")
        if (correop!!.isEmpty()) camposFaltantes.append("Correo, ")
        if (passwordp!!.isEmpty()) camposFaltantes.append("Contraseña, ")
        if (telefonop!!.isEmpty()) camposFaltantes.append("Teléfono, ")
        if (tipouser!!.isEmpty()) camposFaltantes.append("Tipo de usuario, ")
        if (NombreHijo!!.isEmpty()) camposFaltantes.append("Nombre del hijo, ")
        if (ApellidoHijo!!.isEmpty()) camposFaltantes.append("Apellido del hijo, ")
        if (EdadHijo!!.isEmpty()) camposFaltantes.append("Edad del hijo, ")
        if (CorreoHijo!!.isEmpty()) camposFaltantes.append("correo del hijo, ")
        if (TelefonoHijo!!.isEmpty()) camposFaltantes.append("Telefono del hijo, ")

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
            //se procede a enviar los datos porel metodo post a la Api php
            //verificar que en la api estan los mismos parametros
            override fun getParams(): MutableMap<String?, String?> {
                val params: MutableMap<String?, String?> = HashMap<String?, String?>()
                params.put("nombrep", nombrep)
                params.put("apellidop", apellidop)
                params.put("correop", correop)
                params.put("passwordp", passwordp)
                params.put("telefonop", telefonop)
                params.put("tipouser", tipouser)
                params.put("NombreHijo", NombreHijo)
                params.put("ApellidoHijo", ApellidoHijo)
                params.put("EdadHijo", EdadHijo)
                params.put("CorreoHijo", CorreoHijo)
                params.put("TelefonoHijo", TelefonoHijo)
                return params
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add<String?>(stringRequest)
    } //crear metodo que confirme el monitoreo del hijo.
    //es decir el sistema debe saber a que hijo con correo y numero de telefono se relaciona
    //la saber cual es el hijo en monitore, mostrar mensaje de que se esta monitoreando
}
