package com.example.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication3.views.CardViewPost;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.myapplication3.R.layout.activity_homepage;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class HomepageActivity extends AppCompatActivity {
    private static final String TAG ="E";
    FirebaseFirestore db;
    GoogleSignInAccount account;
    CardView cv;
    String uid;
    TextView[] postViews;
    private ViewPager mViewPager;
    ListView click;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(activity_homepage);
       db = FirebaseFirestore.getInstance();
       final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       click = (ListView)findViewById(R.id.lv);
       LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
       ArrayList<PostFragment> posts = new ArrayList<>();
       db.collection("posts").orderBy("time", DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                String postBody = Objects.requireNonNull(document.get("text")).toString();
                                String author = Objects.requireNonNull(document.get("poster_id")).toString();
                                PostFragment f = PostFragment.newInstance(postBody, author, document.getId());
                                posts.add(f);
                                ft.add(R.id.ll, f);
                            }
                            ft.commit();
                            PostFragmentAdapter adapter = new PostFragmentAdapter(getApplicationContext(), posts);
                            click.setAdapter(adapter);
                            click.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    PostFragment item = (PostFragment) parent.getItemAtPosition(position);
                                    item.onClick(view);
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
//
//    private void setViewPager() {
//    }

//    public void displayComments(View view) {
//        Intent chatExpand = new Intent(getApplicationContext(), ChatExpandActivity.class);
//        // Clicked postion
//        int position =getAdapterPosition();
//        PlaceSaved place=items.get(position);
//        Bundle passData = new Bundle();
//        startActivity(chatExpand);
//    }
}
