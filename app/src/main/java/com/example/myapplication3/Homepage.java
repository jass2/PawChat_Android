package com.example.myapplication3;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication3.views.CardViewPost;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.myapplication3.R.layout.activity_homepage;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class Homepage extends AppCompatActivity {
    private static final String TAG ="E";
    FirebaseFirestore db;
    GoogleSignInAccount account;
    LinearLayout sv;
    CardView cv;
    ArrayList<CardViewPost> posts;
    String uid;
    TextView[] postViews;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(activity_homepage);
       db = FirebaseFirestore.getInstance();
       sv = findViewById(R.id.scroll);
       final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        db.collection("posts").orderBy("time", DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                String postBody = Objects.requireNonNull(document.get("text")).toString();
                                String author = Objects.requireNonNull(document.get("poster_id")).toString();
                                View postCard = getLayoutInflater().inflate(R.layout.sample_card_view, sv, false);
                                TextView f = postCard.findViewById(R.id.textBody);
                                f.setText(postBody);
                                TextView e = postCard.findViewById(R.id.poster);
                                e.setText(author);
                                sv.addView(postCard);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
