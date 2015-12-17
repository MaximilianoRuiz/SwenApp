package com.example.maxi.swenapp.fragments;


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
import com.facebook.Profile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class PerfilFragment extends Fragment {

    Profile profile = Profile.getCurrentProfile();

    ImageView ivProfile;
    TextView tvName, tvPoints;

    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPoints = (TextView) view.findViewById(R.id.tvPoint);
        ivProfile = (ImageView) view.findViewById(R.id.ivProfile);

        tvName.setText(profile.getName());
        tvPoints.setText("20");

        Picasso.with(getContext()).load(profile.getProfilePictureUri(70, 70)).transform(new CircleTransform()).into(ivProfile);

        return view;
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
