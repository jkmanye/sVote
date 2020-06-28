package com.schoolvote.schoolvote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class VoteScheduleActivity extends AppCompatActivity {

    TextView vote_vs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;
    String title;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_schedule);
        vote_vs = findViewById(R.id.vote_vs);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        email = currentUser.getEmail();
        Map<String, Long> forsomeone = new HashMap<>();
        if (currentUser != null) {
            forsomeone.put("grade", currentUser.getGrade_update());
            forsomeone.put("clroom", currentUser.getClroom_update());
            loadVote(forsomeone);
        } else {
            Log.d("loadVote", "Logged in user does not exist.");
        }
    }

    private void loadVote(Map<String, Long> forsomeone) {
        if (forsomeone != null) {
            db.collection("votes")
                    .whereEqualTo("for", forsomeone)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            title = (String) document.get("title");
                            Log.d("loadVote", document.getId() + " => " + document.getData());
                            break;
                        }
                        vote_vs.setText(title);
                    } else {
                        vote_vs.setText("");
                    }
                }
            });
        } else {
            vote_vs.setText("");
        }
    }

    public void join(View view) {
        currentUser.setJoiningVoteTitle(vote_vs.getText().toString());
        final Intent intent = new Intent(this, VotePreviewActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivityForResult(intent, 1001);
    }


}