package com.example.maxi.swenapp.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.dialogs.DialogMedalla;

public class Desafios {

    SharedPreferences preferences;
    Context context;
    FragmentManager fragmentManager;

    public Desafios(Context context, SharedPreferences preferences, FragmentManager fragmentManager) {
        this.context = context;
        this.preferences = preferences;
        this.fragmentManager = fragmentManager;
    }

    public void getMedallaPorAcciones() {
        int shares = preferences.getInt("share", 0);
        int comments = preferences.getInt("comment", 0);
        int likes = preferences.getInt("like", 0);

        int interacciones = shares + comments + likes;

        if((interacciones > 0) && preferences.getBoolean("Newbie", true)){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Newbie", false);
            editor.commit();

            int newbie = R.drawable.newbie;
            String mensaje = "Desbloqueaste esta insignia al realizar tu primera interacciòn con una publicacion";

            DialogMedalla dialogo = new DialogMedalla(context, mensaje, "Newbie", newbie);
            dialogo.show(fragmentManager, "tagAlerta");
        }

        if((interacciones > 19) && preferences.getBoolean("Explorer", true)){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Explorer", false);
            editor.commit();

            int explorer = R.drawable.explorer;
            String mensaje = "Desbloqueaste esta insignia al realizar 20 interacciones con la aplicacion";

            DialogMedalla dialogo = new DialogMedalla(context, mensaje,"Explorer", explorer);
            dialogo.show(fragmentManager, "tagAlerta");
        }

        if((interacciones > 49) && preferences.getBoolean("Influencer", true)){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Influencer", false);
            editor.commit();

            int influencer = R.drawable.influencer;
            String mensaje = "Desbloqueaste esta insignia al realizar 50 interacciones con la aplicacion";

            DialogMedalla dialogo = new DialogMedalla(context, mensaje, "Influencer", influencer);
            dialogo.show(fragmentManager, "tagAlerta");
        }
    }
}
