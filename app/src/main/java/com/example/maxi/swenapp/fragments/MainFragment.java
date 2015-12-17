package com.example.maxi.swenapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.activities.TabsActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private LoginButton mLoginButton;
    private TextView textView;
    private Button button;

    AccessTokenTracker mTracker;
    ProfileTracker mProfileTracker;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            displayWelcomeMessage(profile);
            try {
                showPageListActivity(getActivity());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                try {
                    showPageListActivity(getActivity());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                displayWelcomeMessage(newProfile);
            }
        };
        mTracker.startTracking();
        mProfileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        textView = (TextView) rootView.findViewById(R.id.tvTextView);
        button = (Button) rootView.findViewById(R.id.button);
        mLoginButton = (LoginButton) rootView.findViewById(R.id.face);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TabsActivity.class));
            }
        });
        button.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout relativeLayout = (LinearLayout) view.findViewById(R.id.main);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        List<String> permisions = new ArrayList<>();
        permisions.add("user_friends");
        permisions.add("user_posts");

        mLoginButton.setReadPermissions(permisions);
        mLoginButton.setFragment(this);
        mLoginButton.registerCallback(mCallbackManager, mCallBack);
        if ("Salir".equals(mLoginButton.getText())) {
            button.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    private void displayWelcomeMessage(Profile profile) {
        if (profile != null)
            textView.setText(profile.getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
        if (profile != null && "Salir".equals(mLoginButton.getText())) {
            try {
                showPageListActivity(getActivity());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private void showPageListActivity(Context context) throws Throwable {
        startActivity(new Intent(context, TabsActivity.class));
        getActivity().finish();
    }
}
