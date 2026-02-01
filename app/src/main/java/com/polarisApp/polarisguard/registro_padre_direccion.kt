package com.polarisApp.polarisguard

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class registro_padre_direccion : AppCompatActivity() {
    var textView: TextView? = null
    private var estados: Array<String?>
    private val municipios: Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_padre_direccion) // Conecta con registro_padre_direccion.xml

        textView = findViewById<TextView>(R.id.txtv_selecEstado)
        textView = findViewById<TextView>(R.id.txtv_seleMunicipio)
        estados = getResources().getStringArray(R.array.Estados)
        estados = getResources().getStringArray(R.array.Municipios)
        val DireccionContinuar = findViewById<Button>(R.id.btn_ContinuarDireccion)


        DireccionContinuar.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), registropadre_datoshijo::class.java)
            v.getContext().startActivity(intent)
        })

        textView!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                motrarDialogoEstados()
            }
        })

        textView!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                motrarDialogoMunicipios()
            }
        })
    }


    //mostrar los estados
    private fun motrarDialogoEstados() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar estado")
        builder.setItems(estados, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                textView!!.setText(estados[which])
            }
        })
        builder.show()
    }

    //mostrar los municipios
    private fun motrarDialogoMunicipios() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar municipio")
        builder.setItems(municipios, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                textView!!.setText(municipios[which])
            }
        })
        builder.show()
    }
}
