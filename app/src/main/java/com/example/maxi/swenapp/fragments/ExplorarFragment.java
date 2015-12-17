package com.example.maxi.swenapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.VOs.FanPageVO;
import com.example.maxi.swenapp.adapters.FanPageListAdapter;
import com.example.maxi.swenapp.data.DataBaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ExplorarFragment extends Fragment {

    private List<FanPageVO> fanPageVOs;
    private ListView lvPageSelectorList;
    private Button btnUpdatePosts;
    private DataBaseHandler dataBaseHandler;

    public ExplorarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        fanPageVOs = new ArrayList<>();

        dataBaseHandler = new DataBaseHandler(getActivity());
        dataBaseHandler.open();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(preferences.getBoolean("first", true)){
            dataBaseHandler.insertData("Vaqueria", "Indumentaria", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfp1/v/t1.0-1/p50x50/12241769_1067743546571977_8869025085178695116_n.jpg?oh=4b5ade5e0aba6af68e50bea3c1a802d2&oe=56F80046&__gda__=1457098460_29ff602caa767d8b971f9bfa831c44e4"
                    , false, "https://www.facebook.com/vaqueria");
            dataBaseHandler.insertData("Extra Linda", "Indumentaria", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-frc3/v/t1.0-1/p50x50/1379671_625336117536617_365024181_n.jpg?oh=87d67f748fca2ac5c04d4aca4fc2f73c&oe=56DB40F4&__gda__=1457274389_e9b07397effd89c58a4efd2dd4eed976"
                    , false, "https://www.facebook.com/ExtraLinda");
            dataBaseHandler.insertData("Salvadora", "Indumentaria", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xlp1/v/t1.0-1/p50x50/1897799_299683886855525_6627418993137445718_n.jpg?oh=26f6657127887a60414d78747ea8a81b&oe=56D8FEE5&__gda__=1457339372_ee0e69cf8eae776e3a5b28e71de2f58c"
                    , false, "https://www.facebook.com/Salvadora.Moda");

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first", false);
            editor.commit();
        }

        fanPageVOs = dataBaseHandler.returnValue();

        lvPageSelectorList = (ListView) view.findViewById(R.id.lvPageSelectorList);
        btnUpdatePosts = (Button) view.findViewById(R.id.btnUpdatePosts);

        btnUpdatePosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PagesListFragment pagesListFragment = PagesListFragment.getInstance();
                pagesListFragment.onCreate(new Bundle());
            }
        });

        lvPageSelectorList.setAdapter(new FanPageListAdapter(getActivity(), fanPageVOs));

        return view;
    }


}
