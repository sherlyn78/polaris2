package com.polarisApp.polarisguard;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



//para acceder a la hora del sipositivo
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.os.Handler;
import android.os.Looper;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.appcompat.app.AppCompatActivity;

public class pantalla_inicio_mapa extends AppCompatActivity {

    protected  String tipouser;

    private Handler handler = new Handler();
    private Runnable runnable;

    private TextView txtv_Hora, txtv_fecha;

    //llamar a la clase de geocodificacion
    private GeocodingInverso geocodingHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio_mapa); // Conecta con pantalla_inicio_mapa.xml

        SharedPreferences datos = getSharedPreferences("datos_usuario", MODE_PRIVATE);
        String nombrep = datos.getString("NombrePadre", "");
        String apellidop = datos.getString("ApellPadre", "");
        String NombreHijo = datos.getString("NombreHijo_Padre", "");
        String ApellidoHijo = datos.getString("ApellHijo_Padre", "");

        ImageView imgv_carrusel = findViewById(R.id.imgCarrucel_pricipal);

        //iniciar el carrusel7
        AnimationDrawable carrusel = (AnimationDrawable) imgv_carrusel.getDrawable();
        carrusel.start();

        //para obetner formato fecha-hora
        Calendar calendar =  Calendar.getInstance();

        ImageButton Imgb_Mapa = findViewById(R.id.btn_mapaP1);
        ImageButton Imgb_Notificaciones = findViewById(R.id.btn_notificacionesP);
        ImageButton Imgb_Perfil = findViewById(R.id.btn_perfilP);
        txtv_fecha = findViewById(R.id.txtv_fecha);
        txtv_Hora = findViewById(R.id.txtv_Hora);
        TextView txtv_NombreTutor = findViewById(R.id.txtv_Nombre_Padre);
        TextView txtv_NombreHijo = findViewById(R.id.txtv_Nombre_Hijo);

        txtv_NombreTutor.setText(nombrep + " " + apellidop);
        txtv_NombreHijo.setText(NombreHijo + " " + ApellidoHijo);

        actualizarReloj();

        Imgb_Mapa.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("identificador-sesion", MODE_PRIVATE);
            tipouser = prefs.getString("tipoUser", "");
            //si tipouser == 1 enviar a mapa_padre
            //si tipouser == 2 enviar a mapa_hijo
            if(Integer.parseInt(tipouser) == 1){
                Intent intent = new Intent(v.getContext(), mapa_padre.class);
                v.getContext().startActivity(intent);
                finish();
            }else if (Integer.parseInt(tipouser) == 2){
                Intent intent = new Intent(v.getContext(), mapa_hijo.class);
                v.getContext().startActivity(intent);
                finish();
            }
        });

        Imgb_Notificaciones.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), notificaciones.class);
            v.getContext().startActivity(intent);
            finish();
        });

        Imgb_Perfil.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), perfil_usuario.class);
            v.getContext().startActivity(intent);
            finish();
        });

    }

    private void actualizarReloj() {
        runnable = new Runnable() {
            @Override
            public void run() {
                // Obtener fecha y hora actual
                Calendar calendario = Calendar.getInstance();

                // Formatear la fecha
                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String fecha = formatoFecha.format(calendario.getTime());

                // Formatear la hora
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String hora = formatoHora.format(calendario.getTime());

                // Mostrar en los TextViews
                txtv_fecha.setText(fecha);
                txtv_Hora.setText(hora);

                // Llamar de nuevo después de 1 segundo
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable); // inicia el Runnable
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el Runnable cuando la Activity se destruya
        handler.removeCallbacks(runnable);
    }
}
