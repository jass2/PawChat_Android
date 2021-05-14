package com.example.myapplication3;

import android.content.Context;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class PostFragmentAdapter extends ArrayAdapter<PostFragment> {

    private Context mContext;
    private List<PostFragment> postList = new ArrayList<>();

    public PostFragmentAdapter(@NonNull Context context, ArrayList<PostFragment> list) {
        super(context, 0 , list);
        mContext = context;
        postList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.post_fragment,parent,false);

        PostFragment currentPost = postList.get(position);

        TextView textBody = (TextView) listItem.findViewById(R.id.textBody);
        textBody.setText(currentPost.getTextBody());

        TextView author = (TextView) listItem.findViewById(R.id.poster);
        author.setText(currentPost.getAuthor());

        return listItem;
    }
}