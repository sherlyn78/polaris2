package com.polarisApp.polarisguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity{
    boolean passwordVisible = false;
    EditText  edtxPasword, edtxUser;

    //String URL_LOGIN = "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/loguin.php";

    String URL_LOGIN = "https://uzielcreador.site/Backendconection_php/loguin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // Conecta con activity_main.xml

        Button btnIniciarSesion = findViewById(R.id.btn_IniciarSesion);
        TextView txtvcrear_cuenta = findViewById(R.id.txtv_CrearCuena);
        edtxPasword =  findViewById(R.id.edtxPassword);
        edtxUser =  findViewById(R.id.edtxUser);
        ImageView ImgvPasword = findViewById(R.id.ImgvTogglePasswordLogin);

        btnIniciarSesion.setOnClickListener(v -> {
            loginUsuario();  //llamar a la funcion login
        });

        txtvcrear_cuenta.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), crear_cuenta .class);
            v.getContext().startActivity(intent);
        });



        ImgvPasword.setOnClickListener(v -> {
            if (passwordVisible) {
                // Ocultar contraseña
                edtxPasword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ImgvPasword.setImageResource(R.drawable.icon_visibility_off);
                passwordVisible = false;
            } else {
                // Mostrar contraseña
                edtxPasword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ImgvPasword.setImageResource(R.drawable.icon_visibility_on);
                passwordVisible = true;
            }

            // Mantener el cursor al final
            edtxPasword.setSelection(edtxPasword.getText().length());
        });
    }




    private void loginUsuario() {
        String usuario = edtxUser.getText().toString();
        String clave = edtxPasword.getText().toString();

        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.getString("status");
                        String mensaje = json.getString("mensaje");
                        String id_user = json.getString("user_id");
                        String tipoUser = json.getString("tipoUser");

                        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

                        if (status.equals("ok")) {
                            //guardar el correro que se ingreso como identificador en la app
                            SharedPreferences prefs = getSharedPreferences("identificador-sesion", MODE_PRIVATE);
                            prefs.edit().putString("idCorreo", usuario.toString()).apply();
                            prefs.edit().putString("idUser", id_user.toString()).apply();
                            prefs.edit().putString("tipoUser", tipoUser.toString()).apply();


                            //si el mensaje se recibe una verificaion correcta enviar a pantalla mapa.
                            Intent intent = new Intent(this, pantalla_inicio_mapa.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //limpiar los editext para volver a ingresar
                            edtxUser.setText("");
                            edtxPasword.setText("");
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error al procesar respuesta", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    //Manejo de errores
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(this, "No se pudo conectar al servidor. Verifica tu conexión a Internet.", Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(this, "Error de autenticación. Revisa tus credenciales.", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(this, "El servidor encontró un error. Intenta más tarde.", Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(this, "Error de red. Verifica tu conexión o VPN.", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(this, "Error al interpretar la respuesta del servidor.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Error desconocido. Intenta nuevamente.", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", usuario);
                params.put("password", clave);
                return params;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

}
