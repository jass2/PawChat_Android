package com.example.myapplication3;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "PostFragment";

    private View mViewModel;

    private Boolean comment;
    private TextView pb;
    private TextView a;
    private String textBody;
    private String author;
    private String post_id;

    public PostFragment newInstance() {
        return new PostFragment();
    }

    public static PostFragment newInstance(String body, String author, String DocId) {
        PostFragment f = new PostFragment();
        Bundle bdl = new Bundle(2);
        bdl.putString("postBody", body);
        bdl.putString("poster", author);
        bdl.putString("post_id", DocId);
        bdl.putBoolean("comment", false);
        f.setArguments(bdl);
        return f;
    }

    public static PostFragment newInstance(String body, String author, String DocId, Boolean comment) {
        PostFragment f = new PostFragment();
        Bundle bdl = new Bundle(2);
        bdl.putString("postBody", body);
        bdl.putString("poster", author);
        bdl.putString("post_id", DocId);
        bdl.putBoolean("comment", comment);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment, container, false);
        pb = (TextView) view.findViewById(R.id.textBody);
        a = (TextView) view.findViewById(R.id.poster);
        this.textBody = getArguments().getString("postBody");
        this.author = getArguments().getString("poster");
        this.post_id = getArguments().getString("post_id");
        this.comment = getArguments().getBoolean("comment");
        System.out.println(this.post_id);
        pb.setText(this.textBody);
        a.setText(this.author);
        mViewModel = view;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View getView() {
        return mViewModel;
    }

    public String getTextBody() {
        return this.textBody;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setTag (String tag) {

    }

    @Override
    public void onClick(View v) {
        if (!comment) {
            Intent viewPost = new Intent(getContext(), ChatExpandActivity.class);
            Bundle b = new Bundle();
            b.putBoolean("comment", true);
            b.putString("postBody", this.textBody);
            b.putString("poster", this.author);
            System.out.println("HEre");
            System.out.println(this.post_id);
            b.putString("post_id", this.post_id);
            viewPost.putExtras(b);
            startActivity(viewPost);
        }
    }

}