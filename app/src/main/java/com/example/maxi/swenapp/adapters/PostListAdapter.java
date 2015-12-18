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

                    FacebookSdk.sdkInitialize(activity);
                    ShareDialog shareDialog = new ShareDialog(activity);

                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(postVOs.get((Integer)view.getTag()).getName())
                                .setContentDescription("Compartido por SwenApp. Buscanos en Google Play")
                                .setContentUrl(Uri.parse(postVOs.get((Integer)view.getTag()).getLink()))
                                .build();

                        shareDialog.show(linkContent);
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
                }
            });

            viewHolder.unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = postVOs.get((Integer)view.getTag()).getId();

                    viewHolder.unlike.setVisibility(View.GONE);
                    viewHolder.like.setVisibility(View.VISIBLE);

                    Toast.makeText(context, "Me gusta +1", Toast.LENGTH_LONG).show();

                    if(!stringLocalPostLikedMap.containsKey(id)){
                        dataBaseLikeHandler.insertData(id, true);
                    } else {
                        LocalPostLiked localPostLiked = stringLocalPostLikedMap.get(id);
                        dataBaseLikeHandler.upDate(localPostLiked.getId(), localPostLiked.getPostID(), true);
                    }
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
                    dataBaseLikeHandler.upDate(localPostLiked.getId(), localPostLiked.getPostID(), false);
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
