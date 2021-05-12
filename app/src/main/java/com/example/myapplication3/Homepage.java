package com.example.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.myapplication3.R.layout.activity_homepage;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class Homepage extends AppCompatActivity {
    private static final String TAG ="E";
    FirebaseFirestore db;
    GoogleSignInAccount account;
    LinearLayout sv;
    CardView cv;
    ArrayList<TextView> posts;
    String uid;
    TextView[] postViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(activity_homepage);
       db = FirebaseFirestore.getInstance();
       sv = findViewById(R.id.scroll);

        db.collection("posts").orderBy("time", DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<TextView> textViews = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                CardView cv = new CardView(getApplicationContext());
                                TextView temp = new TextView(getApplicationContext());
                                temp.setText(document.get("text").toString());
                                System.out.println(document.get("text").toString());
                                sv.addView(temp);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}
