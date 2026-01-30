package com.polarisApp.polarisguard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;

import android.content.SharedPreferences;  //guardar datos en la en las preferencias
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import java.util.HashMap;
import java.util.Map;

public class registro_padre_exitoso extends AppCompatActivity {

    //url de regisro, Api php, registrar en la bd  ----> registro.php
    //String url_registro = "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/registro_padre.php";

    //String url_registro = "https://beige-mule-441461.hostingersite.com/Backendconection_php/registro_padre.php";

    String url_registro = "https://uzielcreador.site/Backendconection_php/registro_padre.php";

    //variables para recibir los datos de SharedPreferences
    protected String nombrep, apellidop, correop, passwordp, telefonop, tipouser, NombreHijo, ApellidoHijo, EdadHijo, CorreoHijo, TelefonoHijo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_padre_exitoso); // Conecta con activity_main.xml

        Button btnFinalRegistro = findViewById(R.id.btn_iniciarsesionPadre);

        btnFinalRegistro.setOnClickListener(v -> {
            registrarUsuario();
            SharedPreferences prefs = getSharedPreferences("cuenta_creada", MODE_PRIVATE);
            prefs.edit().putString("CuentaCreadaEnDispositivo", "1".toString()).apply(); //1 - finalizo cuenta padre,  2 - finalizo cuenta hijo
        });
    }

    //metodo para registrarse en la base de datos tablas ----> tutor y usuariow
    private void registrarUsuario() {

        //recibir los datos guradados de SharedPreferences de las pantallas anteriores
        //recuperando los datos del registro global
        SharedPreferences prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE);
        nombrep = prefs.getString("NombrePadre", "");
        apellidop = prefs.getString("ApellPadre", "");
        correop = prefs.getString("CorreoPadre", "");
        passwordp = prefs.getString("PaswordPadre", "");
        telefonop = prefs.getString("TelefonoPadre", "");
        tipouser = prefs.getString("tipoUser", "");//tipo de usuario = padre
        //datos del hijo
        NombreHijo = prefs.getString("NombreHijo_Padre", "");
        ApellidoHijo = prefs.getString("ApellHijo_Padre", "");
        EdadHijo = prefs.getString("EdadHijo_Padre", "");
        CorreoHijo = prefs.getString("CorreoHijo_Padre", "");
        TelefonoHijo = prefs.getString("TelefonoHijo_Padre", "");

        //Validar datos completos antes de enviar al servidor
        // Validar si algún campo está vacío
        StringBuilder camposFaltantes = new StringBuilder();

        if (nombrep.isEmpty()) camposFaltantes.append("Nombre, ");
        if (apellidop.isEmpty()) camposFaltantes.append("Apellido, ");
        if (correop.isEmpty()) camposFaltantes.append("Correo, ");
        if (passwordp.isEmpty()) camposFaltantes.append("Contraseña, ");
        if (telefonop.isEmpty()) camposFaltantes.append("Teléfono, ");
        if (tipouser.isEmpty()) camposFaltantes.append("Tipo de usuario, ");
        if (NombreHijo.isEmpty()) camposFaltantes.append("Nombre del hijo, ");
        if (ApellidoHijo.isEmpty()) camposFaltantes.append("Apellido del hijo, ");
        if (EdadHijo.isEmpty()) camposFaltantes.append("Edad del hijo, ");
        if (CorreoHijo.isEmpty()) camposFaltantes.append("correo del hijo, ");
        if (TelefonoHijo.isEmpty()) camposFaltantes.append("Telefono del hijo, ");

        if (camposFaltantes.length() > 0) {
            // Eliminar la última coma y espacio
            String mensaje = "Faltan los siguientes campos: " +
                    camposFaltantes.substring(0, camposFaltantes.length() - 2);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            return;
        }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_registro,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("ok")) {
                            String mensaje = jsonObject.getString("mensaje");

                            Toast.makeText(this, mensaje , Toast.LENGTH_LONG).show();

                            //si la conexion a la api es correctamente, enviar a la pantalla loguin
                            Intent intent = new Intent(this, login.class);
                            startActivity(intent);
                            finish();


                        } else {
                            String errorMsg = jsonObject.getString("mensaje");
                            Toast.makeText(this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(this, crear_cuenta.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar respuesta JSON", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    //Toast.makeText(this, "Error de red: " + error.toString(), Toast.LENGTH_LONG).show();
                    String mensajeError;
                    if (error instanceof com.android.volley.TimeoutError) {
                        mensajeError = "Tiempo de espera agotado. Verifica tu conexión.";
                    } else if (error instanceof com.android.volley.NoConnectionError) {
                        mensajeError = "Sin conexión a Internet. Revisa tu red.";
                    } else {
                        mensajeError = "Error de red inesperado: " + error.toString();
                    }
                    Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show();

                    // Mostrar detalles en Logcat
                    error.printStackTrace(); // muestra la traza completa

                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String body = new String(error.networkResponse.data);
                        Log.e("VolleyErrorBody", body); // muestra el cuerpo de la respuesta del servidor
                    } else {
                        Log.e("VolleyError", "Sin respuesta del servidor o sin datos");
                    }

                    Log.e("VolleyError", "Tipo: " + error.getClass().getSimpleName());
                    Log.e("VolleyError", "Mensaje: " + error.getMessage());


                }) {
            //se procede a enviar los datos porel metodo post a la Api php
            @Override
            //verificar que en la api estan los mismos parametros
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombrep", nombrep);
                params.put("apellidop", apellidop);
                params.put("correop", correop);
                params.put("passwordp", passwordp);
                params.put("telefonop", telefonop);
                params.put("tipouser", tipouser);
                params.put("NombreHijo", NombreHijo);
                params.put("ApellidoHijo", ApellidoHijo);
                params.put("EdadHijo", EdadHijo);
                params.put("CorreoHijo", CorreoHijo);
                params.put("TelefonoHijo", TelefonoHijo);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }



    //crear metodo que confirme el monitoreo del hijo.
    //es decir el sistema debe saber a que hijo con correo y numero de telefono se relaciona
    //la saber cual es el hijo en monitore, mostrar mensaje de que se esta monitoreando

}
