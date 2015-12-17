package com.example.maxi.swenapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.VOs.FanPageVO;
import com.example.maxi.swenapp.VOs.PostVO;
import com.example.maxi.swenapp.adapters.PostListAdapter;
import com.example.maxi.swenapp.data.DataBaseHandler;
import com.example.maxi.swenapp.utils.DatesUtility;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PagesListFragment extends Fragment {

    List<PostVO> postVOs;

    ListView listView;
    ProgressBar progressBar;

    private static PagesListFragment instance;

    public PagesListFragment() {
    }

    public static PagesListFragment getInstance(){
        if (instance == null){
            instance = new PagesListFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        if(requestList().size() == 0){
            progressBar.setVisibility(View.GONE);
            listView.setAdapter(new PostListAdapter(getActivity(), postVOs, getActivity()));
        }

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postVOs = new ArrayList<>();
        if(requestList().size() > 0){
            new Al().execute(requestList());
        } else {
            try{
                progressBar.setVisibility(View.GONE);
                listView.setAdapter(new PostListAdapter(getActivity(), postVOs, getActivity()));
            }catch (Exception e){
            }
        }
    }

    private void populatePostsList(JSONObject object) throws JSONException {

        String name = object.getString("name");
        String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
        JSONArray jsonArray = object.getJSONObject("posts").getJSONArray("data");
        for(int i = 0; i < jsonArray.length(); i ++){
            if(jsonArray.getJSONObject(i).length() == 5) {
                PostVO postVO = new PostVO();
                postVO.setId(jsonArray.getJSONObject(i).getString("id"));
                postVO.setName(name);
                postVO.setPicture(picture);
                postVO.setCreatedTime(DatesUtility.dateFormater(jsonArray.getJSONObject(i).getString("created_time")));
                postVO.setMessage(jsonArray.getJSONObject(i).getString("message"));
                postVO.setFullPicture(jsonArray.getJSONObject(i).getString("full_picture"));
                postVO.setLink(jsonArray.getJSONObject(i).getString("link"));
                postVOs.add(postVO);
            }
        }
    }

    private List<GraphRequest> requestList(){
        List<GraphRequest> graphRequests = new ArrayList<>();
        List<String> pages = obtainListOfFanPages();

        for (int i = 0; i < pages.size(); i++) {
            GraphRequest request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    pages.get(i),
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                populatePostsList(response.getJSONObject());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,picture,posts{id,created_time,message,story,full_picture,link}");
            request.setParameters(parameters);
            graphRequests.add(request);
        }

        return graphRequests;
    }

    private List<String> obtainListOfFanPages(){
        List<String> fanPageList = new ArrayList<>();
        try {
            DataBaseHandler dataBaseHandler = new DataBaseHandler(getActivity());
            dataBaseHandler.open();
            for (FanPageVO fanPageVO : dataBaseHandler.returnValue()){
                if(fanPageVO.isChecked()){
                    fanPageList.add(fanPageVO.getUrl());
                }
            }
        } catch (Exception e) {
            Log.e("PageFragment", "error");
        }
        return fanPageList;
    }

class Al extends AsyncTask<List<GraphRequest>, Void, GraphResponse>{

    List<GraphRequest> requestList;


    @Override
    protected GraphResponse doInBackground(List<GraphRequest>... lists) {
        requestList = lists[0];
        for (int i = 0; i< requestList.size()-1; i++){
            requestList.get(i).executeAndWait();
        }
        return requestList.get(requestList.size()-1).executeAndWait();
    }

    @Override
    protected void onPostExecute(GraphResponse graphResponse) {
        super.onPostExecute(graphResponse);
        progressBar.setVisibility(View.GONE);
        sorterList();
        listView.setAdapter(new PostListAdapter(getActivity(), postVOs, getActivity()));
        Toast.makeText(getActivity(),"size " + postVOs.size(), Toast.LENGTH_SHORT).show();
        postVOs = new ArrayList<>();
    }

    private void sorterList() {
        Collections.sort(postVOs, new Comparator<PostVO>() {
            @Override
            public int compare(PostVO postVO, PostVO postVO2) {
                Date date1, date2;
                try {
                    date1 = DatesUtility.changeToDate(postVO.getCreatedTime());
                    date2 = DatesUtility.changeToDate(postVO2.getCreatedTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
                return date2.compareTo(date1);
            }
        });
    }
}
}