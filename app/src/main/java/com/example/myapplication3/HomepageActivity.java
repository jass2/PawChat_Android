package com.example.myapplication3;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.myapplication3.R.layout.activity_homepage;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class HomepageActivity extends AppCompatActivity implements NewPostDialog.NewPostDialogListener {
    private static final String TAG ="HomePage";
    private FirebaseFirestore db;
    private ListView click;
    private PostFragmentAdapter adapter;
    private FirebaseAuth fbAuth;
    private Query postSnap;
    private ArrayList<PostFragment> posts;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(activity_homepage);

       this.click = (ListView)findViewById(R.id.lv);

       this.posts = new ArrayList<>();

       this.fbAuth = FirebaseAuth.getInstance();
       this.db = FirebaseFirestore.getInstance();
       this.postSnap = this.db.collection("posts").orderBy("time", DESCENDING);

       this.getPosts();

        Button newPost = (Button) findViewById(R.id.newPostBtn);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    public void getPosts() {
        this.postSnap.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    posts.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String postBody = Objects.requireNonNull(document.get("text")).toString();
                        String author = Objects.requireNonNull(document.get("poster_id")).toString();
                        PostFragment f = new PostFragment(postBody, author, document.getId(), false);
                        posts.add(f);
                    }
                    adapter = new PostFragmentAdapter(getApplicationContext(), posts);
                    click.setAdapter(adapter);
                    click.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            PostFragment item = (PostFragment) parent.getItemAtPosition(position);
                            if (!item.getComment()) {
                                Intent viewPost = new Intent(getApplicationContext(), ChatExpandActivity.class);
                                Bundle b = new Bundle();
                                b.putBoolean("comment", item.getComment());
                                b.putString("postBody", item.getTextBody());
                                b.putString("poster", item.getAuthor());
                                b.putString("post_id", item.getPost_id());
                                viewPost.putExtras(b);
                                startActivity(viewPost);
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void openDialog() {
        NewPostDialog npd = new NewPostDialog();
        npd.show(getSupportFragmentManager(), "new post dialog");
    }

    @Override
    public void applyTexts(String postText) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());

        Map<String, Object> newPost = new HashMap<>();
        String poster = getUserFromEmail(Objects.requireNonNull(Objects.requireNonNull(this.fbAuth.getCurrentUser()).getEmail()));
        newPost.put("poster_id", poster);
        newPost.put("text", postText);
        newPost.put("comments_allowed", true);
        newPost.put("time", format);

        db.collection("posts").document().set(newPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getPosts();
                        Context context = getApplicationContext();
                        CharSequence text = "Post Created!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Context context = getApplicationContext();
                        CharSequence text = "Could Not Create Post";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();                    }
                });
    }

    public String getUserFromEmail(String email) {
        String[] parts = email.split("@");
        return parts[0];
    }

}
