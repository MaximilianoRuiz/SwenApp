package com.example.maxi.swenapp.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.VOs.LocalPostComment;
import com.example.maxi.swenapp.VOs.LocalPostLiked;
import com.example.maxi.swenapp.VOs.PostVO;
import com.example.maxi.swenapp.data.DataBaseCommentsHandler;
import com.example.maxi.swenapp.data.DataBaseLikeHandler;
import com.example.maxi.swenapp.dialogs.DialogComment;
import com.example.maxi.swenapp.fragments.PerfilFragment;
import com.example.maxi.swenapp.utils.Desafios;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostListAdapter extends ArrayAdapter<PostVO> {

    Context context;
    private DataBaseLikeHandler dataBaseLikeHandler;
    private DataBaseCommentsHandler dataBaseCommentHandle;
    public List<PostVO> postVOs;
    private SharedPreferences prefs;
    public ViewHolder viewHolder;
    public FragmentActivity activity;

    SharedPreferences preferences;

    private Map<String, LocalPostLiked> stringLocalPostLikedMap;
    Map<String, List<LocalPostComment>> valueComments;
    Map<String, List<LocalPostComment>> commentsListMaps;

    public PostListAdapter(Context c, List<PostVO> postVOs, FragmentActivity fragmentActivity) {
        super(c, R.layout.post_list_adapter_row, postVOs);
        this.context = c;
        this.postVOs = postVOs;
        this.activity = fragmentActivity;
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        dataBaseLikeHandler = new DataBaseLikeHandler(c);
        dataBaseCommentHandle = new DataBaseCommentsHandler(c);
        dataBaseCommentHandle.open();
        reloadListComments();
        preferences = fragmentActivity.getSharedPreferences("MyShared", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.post_list_adapter_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.createdTime = (TextView) convertView.findViewById(R.id.createdTime);
            viewHolder.message = (TextView) convertView.findViewById(R.id.message);

            viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);
            viewHolder.fullPicture = (ImageView) convertView.findViewById(R.id.fullPicture);

            viewHolder.share = (ImageButton) convertView.findViewById(R.id.btnShare);
            viewHolder.unlike = (ImageButton) convertView.findViewById(R.id.btnUnLike);
            viewHolder.like = (ImageButton) convertView.findViewById(R.id.btnLike);
            viewHolder.comment = (ImageButton) convertView.findViewById(R.id.btnComment);

            viewHolder.listView = (ListView) convertView.findViewById(R.id.listView2);

            dataBaseLikeHandler.open();

            stringLocalPostLikedMap = dataBaseLikeHandler.returnValue();

            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = activity.getFragmentManager();

                    FacebookSdk.sdkInitialize(activity);
                    ShareDialog shareDialog = new ShareDialog(activity);

                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(postVOs.get((Integer)view.getTag()).getName())
                                .setContentDescription("Compartido por SwenApp. Buscanos en Google Play")
                                .setContentUrl(Uri.parse(postVOs.get((Integer)view.getTag()).getLink()))
                                .build();

                        shareDialog.show(linkContent);

                        int points = preferences.getInt("points", 0);
                        points = points + 10;

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("points", points);
                        editor.commit();


                        int share = preferences.getInt("share", 0);
                        share = share + 1;

                        SharedPreferences.Editor editorShare = preferences.edit();
                        editorShare.putInt("share", share);
                        editorShare.commit();

                        Desafios desafios = new Desafios(context, preferences, fragmentManager);

                        if((postVOs.get((Integer)view.getTag()).getMessage().contains("#SentiteComodo"))){
                            int shareSentiteComodo = preferences.getInt("shareSentiteComoda", 0);
                            shareSentiteComodo = shareSentiteComodo + 1;

                            editorShare.putInt("shareSentiteComoda", shareSentiteComodo);
                        }

                        if((postVOs.get((Integer)view.getTag()).getMessage().contains("#Gifcard"))){
                            int shareGifCard = preferences.getInt("shareGifCard", 0);
                            shareGifCard = shareGifCard + 1;

                            editorShare.putInt("shareGifCard", shareGifCard);
                        }

                        PerfilFragment.getInstance().setValues();

                        desafios.getMedallaPorAcciones();
                    }
                }
            });

            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    DialogComment dialogo = new DialogComment(context, postVOs.get((Integer)view.getTag()).getId());
                    dialogo.show(fragmentManager, "tagAlerta");
                    reloadListComments();

                    int points = preferences.getInt("points", 0);
                    points = points + 5;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("points", points);
                    editor.commit();

                    int comment = preferences.getInt("comment", 0);
                    comment = comment + 1;

                    SharedPreferences.Editor editorComment = preferences.edit();
                    editorComment.putInt("comment", comment);
                    editorComment.commit();

                    Desafios desafios = new Desafios(context, preferences, fragmentManager);

                    desafios.getMedallaPorAcciones();

                    PerfilFragment.getInstance().setValues();
                }
            });

            viewHolder.unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = postVOs.get((Integer)view.getTag()).getId();
                    FragmentManager fragmentManager = activity.getFragmentManager();

                    viewHolder.unlike.setVisibility(View.GONE);
                    viewHolder.like.setVisibility(View.VISIBLE);

                    Toast.makeText(context, "Me gusta +1", Toast.LENGTH_LONG).show();

                    if(!stringLocalPostLikedMap.containsKey(id)){
                        dataBaseLikeHandler.insertData(id, true);
                    } else {
                        LocalPostLiked localPostLiked = stringLocalPostLikedMap.get(id);
                        dataBaseLikeHandler.upDate(localPostLiked.getId(), localPostLiked.getPostID(), true);
                    }

                    int points = preferences.getInt("points", 0);
                    points = points + 1;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("points", points);
                    editor.commit();

                    int like = preferences.getInt("like", 0);
                    like = like + 1;

                    SharedPreferences.Editor editorLike = preferences.edit();
                    editorLike.putInt("like", like);
                    editorLike.commit();

                    stringLocalPostLikedMap = dataBaseLikeHandler.returnValue();

                    Desafios desafios = new Desafios(context, preferences, fragmentManager);

                    desafios.getMedallaPorAcciones();

                    PerfilFragment.getInstance().setValues();
                }
            });

            viewHolder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = postVOs.get((Integer)view.getTag()).getId();

                    viewHolder.unlike.setVisibility(View.VISIBLE);
                    viewHolder.like.setVisibility(View.GONE);

                    Toast.makeText(context, "Me gusta -1", Toast.LENGTH_LONG).show();

                    LocalPostLiked localPostLiked = stringLocalPostLikedMap.get(id);
                    try{
                        dataBaseLikeHandler.upDate(localPostLiked.getId(), localPostLiked.getPostID(), false);
                    } catch (Exception e){
                        //todo;
                    }

                    stringLocalPostLikedMap = dataBaseLikeHandler.returnValue();
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostVO postVO = postVOs.get(position);
        if(postVO != null) {
            viewHolder.share.setTag(position);
            viewHolder.comment.setTag(position);
            viewHolder.like.setTag(position);
            viewHolder.unlike.setTag(position);
            viewHolder.id = postVO.getId();
            viewHolder.name.setText(postVO.getName());
            viewHolder.createdTime.setText(postVO.getCreatedTime());
            viewHolder.message.setText(postVO.getMessage());
            Picasso.with(context).load(postVO.getPicture()).into(viewHolder.picture);
            Picasso.with(context).load(postVO.getFullPicture()).into(viewHolder.fullPicture);

            if(stringLocalPostLikedMap.containsKey(postVO.getId())){
               if(stringLocalPostLikedMap.get(postVO.getId()).isLiked()){
                   viewHolder.unlike.setVisibility(View.GONE);
                   viewHolder.like.setVisibility(View.VISIBLE);
               }else {
                   viewHolder.unlike.setVisibility(View.VISIBLE);
                   viewHolder.like.setVisibility(View.GONE);
               }
            } else {
                viewHolder.unlike.setVisibility(View.VISIBLE);
                viewHolder.like.setVisibility(View.GONE);
            }

            if(commentsListMaps.containsKey(postVO.getId())){
                List<LocalPostComment> localPostComments = commentsListMaps.get(postVO.getId());
                List<String> list = new ArrayList<>();
                for(LocalPostComment localPostComment : localPostComments){
                    list.add(localPostComment.getComment());
                }
                viewHolder.listView.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, list));
            } else {
                viewHolder.listView.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, new ArrayList()));
            }
        }

        return convertView;
    }

    private void reloadListComments(){
        commentsListMaps = dataBaseCommentHandle.returnValue();
    }

    static class ViewHolder {
        String id;
        TextView name, createdTime, message;
        ImageView picture, fullPicture;
        ImageButton share,unlike, like, comment;
        ListView listView;
    }
}
