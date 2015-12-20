package com.example.maxi.swenapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxi.swenapp.R;


public class DialogMedalla extends DialogFragment{

    private Context context;
    private String mensaje;
    private String medalla;
    private int image;

    ImageView imageView;
    TextView textView;

    public DialogMedalla(Context context, String mensaje, String medalla, int image) {
        this.context = context;
        this.mensaje = mensaje;
        this.medalla = medalla;
        this.image = image;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_medalla, null);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        textView = (TextView) view.findViewById(R.id.textView);

        textView.setText(mensaje);
        imageView.setBackgroundResource(image);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);
        builder.setTitle("¡Felicidades, obtuviste una Medalla! " + medalla);
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }


}
