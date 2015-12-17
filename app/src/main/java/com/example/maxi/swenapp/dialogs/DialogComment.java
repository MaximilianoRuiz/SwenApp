package com.example.maxi.swenapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.data.LocalPostDataBaseHandler;


public class DialogComment extends DialogFragment{

    EditText etComment;
    private Context context;
    private LocalPostDataBaseHandler dataBaseHandler;
    private String postId;

    public DialogComment(Context context, String postId) {
        this.context = context;
        this.postId = postId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dataBaseHandler = new LocalPostDataBaseHandler(context);
        dataBaseHandler.open();

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_comment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        etComment = (EditText) view.findViewById(R.id.etComment);

        builder.setView(view);
        builder.setTitle("Comentario");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Comentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataBaseHandler.insertComment(postId, etComment.getText().toString());
                Toast.makeText(getActivity(), "Comentar: " + etComment.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }


}
