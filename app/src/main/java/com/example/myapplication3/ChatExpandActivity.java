package com.example.myapplication3;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication3.views.CardViewPost;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.myapplication3.R.layout.activity_chat_expand;
import static com.example.myapplication3.R.layout.activity_homepage;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class ChatExpandActivity extends AppCompatActivity {
    FirebaseFirestore db;
    GoogleSignInAccount account;
    LinearLayout mainLayout;
    Button back;
    String postId;
    String author;
    String textBody;
    FirebaseAuth fbAuth;
    ListView commentList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.textBody = bundle.getString("postBody");
        this.author = bundle.getString("poster");
        this.postId = bundle.getString("post_id");
        setContentView(activity_chat_expand);
        commentList = (ListView) findViewById(R.id.comments);
        fbAuth = FirebaseAuth.getInstance();

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ArrayList<PostFragment> pvList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("comment").whereEqualTo("post",  this.postId)
                .orderBy("time", ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            PostFragment main = PostFragment.newInstance(textBody, author, postId, true);
                            ft.replace(R.id.ll, main);
                            pvList.add(main);
                            for (QueryDocumentSnapshot comment : Objects.requireNonNull(task.getResult())) {
                                String postBody = Objects.requireNonNull(comment.get("text")).toString();
                                String author = Objects.requireNonNull(comment.get("poster")).toString();
                                PostFragment f = PostFragment.newInstance(postBody, author, comment.getId(), true);
                                pvList.add(f);
                                System.out.println("Test");
                                ft.replace(R.id.comments, f);
                            }
                            System.out.println("Tes2");
                            PostFragmentAdapter adapter = new PostFragmentAdapter(getApplicationContext(), pvList);
                            commentList.setAdapter(adapter);
                            ft.commit();

                        }
                    }
                });
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
