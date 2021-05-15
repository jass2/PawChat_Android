package com.example.myapplication3;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.myapplication3.R.layout.activity_chat_expand;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;


public class ChatExpandActivity extends AppCompatActivity implements NewCommentDialog.NewCommentDialogListener {
    FirebaseFirestore db;
    GoogleSignInAccount account;
    LinearLayout mainLayout;
    Button back;
    Button addComment;
    String postId;
    String author;
    String textBody;
    FirebaseAuth fbAuth;
    ListView commentList;
    ArrayList<PostFragment> pvList;
    PostFragmentAdapter adapter;

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

        pvList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("comment").whereEqualTo("post",  this.postId)
                .orderBy("time", ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            PostFragment main = new PostFragment(textBody, author, postId, true);
                            pvList.add(main);
                            for (QueryDocumentSnapshot comment : Objects.requireNonNull(task.getResult())) {
                                String postBody = Objects.requireNonNull(comment.get("text")).toString();
                                String author = Objects.requireNonNull(comment.get("poster")).toString();
                                PostFragment f = new PostFragment(postBody, author, comment.getId(), true);
                                pvList.add(f);
                            }
                            adapter = new PostFragmentAdapter(getApplicationContext(), pvList);
                            commentList.setAdapter(adapter);
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

        addComment = (Button) findViewById(R.id.newPostBtn);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        NewCommentDialog npd = new NewCommentDialog();
        npd.show(getSupportFragmentManager(), "new comment dialog");
    }

    @Override
    public void applyTexts(String comment) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());

        Map<String, Object> newComment = new HashMap<>();
        String poster = getUserFromEmail(Objects.requireNonNull(Objects.requireNonNull(this.fbAuth.getCurrentUser()).getEmail()));
        newComment.put("post", this.postId);
        newComment.put("poster", poster);
        newComment.put("text", comment);
        newComment.put("time", format);

        db.collection("comment").document().set(newComment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                PostFragment f = new PostFragment(comment, poster, "", true);
                adapter.add(f);
                Context context = getApplicationContext();
                CharSequence text = "Comment Added!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Context context = getApplicationContext();
                        CharSequence text = "Could Not Add Comment";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
    }

    public String getUserFromEmail(String email) {
        String[] parts = email.split("@");
        return parts[0];
    }

}
