package com.polarisApp.polarisguard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class registro_padre_direccion extends AppCompatActivity{

    public TextView textView;
    private String[] estados;
    private String[] municipios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_padre_direccion); // Conecta con registro_padre_direccion.xml

        textView = findViewById(R.id.txtv_selecEstado);
        textView = findViewById(R.id.txtv_seleMunicipio);
        estados = getResources().getStringArray(R.array.Estados);
        estados = getResources().getStringArray(R.array.Municipios);
        Button DireccionContinuar = findViewById(R.id.btn_ContinuarDireccion);


        DireccionContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), registropadre_datoshijo.class);
            v.getContext().startActivity(intent);
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motrarDialogoEstados();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motrarDialogoMunicipios();
            }
        });
    }


    //mostrar los estados
    private void motrarDialogoEstados(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar estado");
        builder.setItems(estados, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(estados[which]);
            }
        });
        builder.show();
    }

    //mostrar los municipios
    private void motrarDialogoMunicipios(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar municipio");
        builder.setItems(municipios, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(municipios[which]);
            }
        });
        builder.show();
    }
}
