package com.polarisApp.polarisguard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//para acceder a la hora del sipositivo
class pantalla_inicio_mapa : AppCompatActivity() {
    protected var tipouser: String? = null

    private val handler = Handler()
    private var runnable: Runnable? = null

    private var txtv_Hora: TextView? = null
    private var txtv_fecha: TextView? = null

    //llamar a la clase de geocodificacion
    private val geocodingHelper: GeocodingInverso? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_inicio_mapa) // Conecta con pantalla_inicio_mapa.xml

        val datos = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        val nombrep: String = datos.getString("NombrePadre", "")!!
        val apellidop: String = datos.getString("ApellPadre", "")!!
        val NombreHijo: String = datos.getString("NombreHijo_Padre", "")!!
        val ApellidoHijo: String = datos.getString("ApellHijo_Padre", "")!!

        val imgv_carrusel = findViewById<ImageView>(R.id.imgCarrucel_pricipal)

        //iniciar el carrusel7
        val carrusel = imgv_carrusel.getDrawable() as AnimationDrawable
        carrusel.start()

        //para obetner formato fecha-hora
        val calendar = Calendar.getInstance()

        val Imgb_Mapa = findViewById<ImageButton>(R.id.btn_mapaP1)
        val Imgb_Notificaciones = findViewById<ImageButton>(R.id.btn_notificacionesP)
        val Imgb_Perfil = findViewById<ImageButton>(R.id.btn_perfilP)
        txtv_fecha = findViewById<TextView>(R.id.txtv_fecha)
        txtv_Hora = findViewById<TextView>(R.id.txtv_Hora)
        val txtv_NombreTutor = findViewById<TextView>(R.id.txtv_Nombre_Padre)
        val txtv_NombreHijo = findViewById<TextView>(R.id.txtv_Nombre_Hijo)

        txtv_NombreTutor.setText(nombrep + " " + apellidop)
        txtv_NombreHijo.setText(NombreHijo + " " + ApellidoHijo)

        actualizarReloj()

        Imgb_Mapa.setOnClickListener(View.OnClickListener { v: View? ->
            val prefs = getSharedPreferences("identificador-sesion", MODE_PRIVATE)
            tipouser = prefs.getString("tipoUser", "")
            //si tipouser == 1 enviar a mapa_padre
            //si tipouser == 2 enviar a mapa_hijo
            if (tipouser!!.toInt() == 1) {
                val intent = Intent(v!!.getContext(), mapa_padre::class.java)
                v.getContext().startActivity(intent)
                finish()
            } else if (tipouser!!.toInt() == 2) {
                val intent = Intent(v!!.getContext(), mapa_hijo::class.java)
                v.getContext().startActivity(intent)
                finish()
            }
        })

        Imgb_Notificaciones.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), notificaciones::class.java)
            v.getContext().startActivity(intent)
            finish()
        })

        Imgb_Perfil.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), perfil_usuario::class.java)
            v.getContext().startActivity(intent)
            finish()
        })
    }

    private fun actualizarReloj() {
        runnable = object : Runnable {
            override fun run() {
                // Obtener fecha y hora actual
                val calendario = Calendar.getInstance()

                // Formatear la fecha
                val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fecha = formatoFecha.format(calendario.getTime())

                // Formatear la hora
                val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val hora = formatoHora.format(calendario.getTime())

                // Mostrar en los TextViews
                txtv_fecha!!.setText(fecha)
                txtv_Hora!!.setText(hora)

                // Llamar de nuevo después de 1 segundo
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable!!) // inicia el Runnable
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener el Runnable cuando la Activity se destruya
        handler.removeCallbacks(runnable!!)
    }
}
