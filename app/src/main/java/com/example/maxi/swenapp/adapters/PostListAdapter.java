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
import android.widget.TextView;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.VOs.PostVO;
import com.example.maxi.swenapp.dialogs.DialogComment;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostListAdapter extends ArrayAdapter<PostVO> {

    Context context;
    //private LocalPostDataBaseHandler dataBaseHandler;
    public List<PostVO> postVOs;
//    Map<String,List<LocalPostComment>> localPostComments;
//    Map<String,List<LocalPostLiked>> localPostLikeds;
    private SharedPreferences prefs;
    public ViewHolder viewHolder;
    public FragmentActivity activity;

    public PostListAdapter(Context c, List<PostVO> postVOs, FragmentActivity fragmentActivity) {
        super(c, R.layout.post_list_adapter_row, postVOs);
//        LocalPostDataBaseHandler baseHandler = new LocalPostDataBaseHandler(context);
        this.context = c;
        this.postVOs = postVOs;
        this.activity = fragmentActivity;
//        this.localPostComments = baseHandler.returnValueComments();
//        this.localPostLikeds = baseHandler.returnValueLikeds();
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            dataBaseHandler = new LocalPostDataBaseHandler(context);
//            dataBaseHandler.open();

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
                }
            });

            viewHolder.unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = postVOs.get((Integer)view.getTag()).getId();

//                    if(localPostLikeds.containsKey(id)){
//                        List<LocalPostLiked> postLiked = localPostLikeds.get(id);
////                        dataBaseHandler.upDateLiked(postLiked.get(0).getId(), id, true);
//                    } else {
////                        dataBaseHandler.insertLiked(id, true);
//                    }

                    viewHolder.unlike.setVisibility(View.GONE);
                    viewHolder.like.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(id, true);
                }
            });

            viewHolder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = postVOs.get((Integer)view.getTag()).getId();
//                    List<LocalPostLiked> postLiked = localPostLikeds.get(id);

//                    dataBaseHandler.upDateLiked(postLiked.get(0).getId(), id, false);
//                    viewHolder.like.setVisibility(View.GONE);
//                    viewHolder.unlike.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(id, false);
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

            if(prefs.getBoolean(postVO.getId(), false)){
                viewHolder.unlike.setVisibility(View.GONE);
                viewHolder.like.setVisibility(View.VISIBLE);
            } else {
                viewHolder.unlike.setVisibility(View.VISIBLE);
                viewHolder.like.setVisibility(View.GONE);
            }

//            if(localPostLikeds.containsKey(postVO.getId())) {
//                List<LocalPostLiked> postLiked = localPostLikeds.get(postVO.getId());
//
//                if(postLiked.get(0).isLiked()){
//                    viewHolder.unlike.setVisibility(View.GONE);
//                    viewHolder.like.setVisibility(View.VISIBLE);
//                } else{
//                    viewHolder.like.setVisibility(View.GONE);
//                    viewHolder.unlike.setVisibility(View.VISIBLE);
//                }
//                viewHolder.like.setVisibility(View.GONE);
//                viewHolder.unlike.setVisibility(View.VISIBLE);
//            }
        }

        return convertView;
    }

    static class ViewHolder {
        String id;
        TextView name, createdTime, message;
        ImageView picture, fullPicture;
        ImageButton share,unlike, like, comment;
    }
}
