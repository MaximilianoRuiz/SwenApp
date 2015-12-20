package com.example.maxi.swenapp.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.dialogs.DialogMedalla;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Desafios {

    SharedPreferences preferences;
    Context context;
    FragmentManager fragmentManager;
    Firebase myFirebaseRef;

    Long hombres, mujeres, todos;

    public Desafios(Context context, SharedPreferences preferences, FragmentManager fragmentManager) {
        this.context = context;
        this.preferences = preferences;
        this.fragmentManager = fragmentManager;
    }

    public void getMedallaPorAcciones() {
        int shares = preferences.getInt("share", 0);
        int comments = preferences.getInt("comment", 0);
        int likes = preferences.getInt("like", 0);

        Firebase.setAndroidContext(context);

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

        myFirebaseRef = new Firebase("https://crackling-fire-955.firebaseio.com/");

        myFirebaseRef.child("hombres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                hombres = (Long) snapshot.getValue();
                getMedallaDaily();
            }
            @Override public void onCancelled(FirebaseError error) { }
        });
//
        myFirebaseRef.child("mujeres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mujeres = (Long) snapshot.getValue();
                getMedallaWamu();
            }
            @Override public void onCancelled(FirebaseError error) { }
        });

        myFirebaseRef.child("todos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                todos = (Long) snapshot.getValue();
                getMedallaVaqueria();
            }
            @Override public void onCancelled(FirebaseError error) { }
        });

    }

    public void getMedallaVaqueria(){
        if(preferences.getInt("shareSentiteComodo", 0) > 4){
            if(preferences.getInt("shareGifCard", 0) > 0){
                if(todos < 8){
                    Long value = todos + 1;
                    myFirebaseRef.child("hombres").setValue(value);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("Vaqueria", false);
                    editor.commit();

                    int influencer = R.drawable.vaqueria;
                    String mensaje = "Desbloqueaste esta insignia. Con ella tienes %20 OFF en Vaqueria. ¡Que lo disfrutes!";

                    DialogMedalla dialogo = new DialogMedalla(context, mensaje, "Vaqueria", influencer);
                    dialogo.show(fragmentManager, "tagAlerta");
                }
            }
        }
    }

    public void getMedallaWamu(){
        if("famele".equals(preferences.getString("gender", "male"))){
            if(preferences.getInt("shareSentiteComodo", 0) > 4){
                if(preferences.getInt("shareGifCard", 0) > 0){
                    if(mujeres < 1){
                        Long value = mujeres + 1;
                        myFirebaseRef.child("hombres").setValue(value);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("Wamu", false);
                        editor.commit();

                        int influencer = R.drawable.wamu;
                        String mensaje = "Desbloqueaste esta insignia. Con ella tienes una remera gratis para mujer de la marca Wamu en cualquier sucursar de Vaqueria. ¡Que la disfrutes!";

                        DialogMedalla dialogo = new DialogMedalla(context, mensaje, "Wamu", influencer);
                        dialogo.show(fragmentManager, "tagAlerta");
                    }
                }
            }
        }
    }

    public void getMedallaDaily(){
        if("male".equals(preferences.getString("gender", "famele"))){
            if(preferences.getInt("shareSentiteComodo", 0) > 4){
                if(preferences.getInt("shareGifCard", 0) > 0){
                    if(hombres < 1){
                        Long value = hombres + 1;
                        myFirebaseRef.child("hombres").setValue(value);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("Daily", false);
                        editor.commit();

                        int influencer = R.drawable.wamu;
                        String mensaje = "Desbloqueaste esta insignia. Con ella tienes una remera gratis para hombre de la marca Daily en cualquier sucursar de Vaqueria. ¡Que la disfrutes!";

                        DialogMedalla dialogo = new DialogMedalla(context, mensaje, "Daily", influencer);
                        dialogo.show(fragmentManager, "tagAlerta");
                    }
                }
            }
        }
    }
}
