package com.polarisApp.polarisguard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class registro_hijo_exitoso extends AppCompatActivity{


    //url de regisro, Api php, registrar en la bd  ----> registro.php
    String url_registro = "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/registro_hijo.php";

    //variables para recibir los datos de SharedPreferences
    protected String correo, telefono, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_hijo_exitoso); // Conecta con registro_hijo_exitoso.xml

        Button FinalizarRegistro = findViewById(R.id.btn_FinalizaRegistro);

        FinalizarRegistro.setOnClickListener(v -> {
            registrarUsuarioHijo();
        });
    }


    //metodo para enviar registros a la base de datos.
    private void registrarUsuarioHijo() {
        //recibir los datos guradados de SharedPreferences de las pantallas anteriores
        //recuperando los datos del registro global
        SharedPreferences prefs = getSharedPreferences("datos_usuario_hijo", MODE_PRIVATE);
        correo = prefs.getString("CorreroHijo", "");
        telefono = prefs.getString("telefonoHijo", "");
        password = prefs.getString("passwordHijo", "");

        //Validar datos completos antes de enviar al servidor
        // Validar si algún campo está vacío
        StringBuilder camposFaltantes = new StringBuilder();

        if (correo.isEmpty()) camposFaltantes.append("Nombre, ");
        if (telefono.isEmpty()) camposFaltantes.append("Apellido, ");
        if (password.isEmpty()) camposFaltantes.append("Apellido, ");

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
            //se procede a enviar los datos por el metodo post a la Api php
            @Override
            //verificar que en la api estan los mismos parametros
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                params.put("password", password);
                params.put("telefono", telefono);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
