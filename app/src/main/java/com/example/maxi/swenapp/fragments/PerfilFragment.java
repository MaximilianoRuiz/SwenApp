package com.example.maxi.swenapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxi.swenapp.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class PerfilFragment extends Fragment {

    Profile profile = Profile.getCurrentProfile();

    ImageView ivProfile;
    TextView tvName, tvPoints, tvInsignias;
    ImageView newbi, explorer, influence, daily, wambu, vaqueria;

    SharedPreferences preferences;

    private static PerfilFragment instance;

    public PerfilFragment() {
    }

    public static PerfilFragment getInstance(){
        if (instance == null){
            instance = new PerfilFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPoints = (TextView) view.findViewById(R.id.tvPoint);
        tvInsignias = (TextView) view.findViewById(R.id.tvInsignias);
        ivProfile = (ImageView) view.findViewById(R.id.ivProfile);

        newbi = (ImageView) view.findViewById(R.id.newbi);
        explorer = (ImageView) view.findViewById(R.id.explorer);
        influence = (ImageView) view.findViewById(R.id.influencer);
        daily = (ImageView) view.findViewById(R.id.daily);
        wambu = (ImageView) view.findViewById(R.id.wambu);
        vaqueria = (ImageView) view.findViewById(R.id.vaqueria);

        preferences = getActivity().getSharedPreferences("MyShared",Context.MODE_PRIVATE);

        tvName.setText(profile.getName());
        setValues();

        Picasso.with(getContext()).load(profile.getProfilePictureUri(70, 70)).transform(new CircleTransform()).into(ivProfile);

        if(preferences.getBoolean("first", true)){
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            try {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("gender", object.getString("gender").toString());
                                editor.putBoolean("first", false);
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "gender");
            request.setParameters(parameters);
            request.executeAsync();
        }


        return view;
    }

    public void setValues(){
        tvPoints.setText(Integer.toString(preferences.getInt("points", 0)));
        tvInsignias.setText(Integer.toString(preferences.getInt("insignias", 0)));

        if(!preferences.getBoolean("Newbie", true)){
            newbi.setBackgroundResource(R.drawable.newbie);
        }
        if(!preferences.getBoolean("Explorer", true)){
            explorer.setBackgroundResource(R.drawable.explorer);
        }
        if(!preferences.getBoolean("Influencer", true)){
            influence.setBackgroundResource(R.drawable.influencer);
        }
    }

    private Bitmap obtainCircleImage(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.with(getContext()).load(uri).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(bitmap == null){
            try {
                InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);

        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

        return circleBitmap;
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
