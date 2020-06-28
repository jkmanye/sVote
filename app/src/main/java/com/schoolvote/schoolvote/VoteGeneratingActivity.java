package com.schoolvote.schoolvote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VoteGeneratingActivity extends AppCompatActivity {

    FirebaseFirestore db;

    public Map<String, Object> vote = new HashMap<>();
    public Map<String, String> list = new HashMap<>();

    EditText title_vg;
    EditText subtitle_vg;
    EditText list_vg;
    EditText grade_vg;
    EditText clroom_vg;

    boolean closed = false;
    long counter;
    String put;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_generating);
        user = (User) getIntent().getSerializableExtra("currentUser");
        db = FirebaseFirestore.getInstance();
        title_vg = findViewById(R.id.title_vg);
        subtitle_vg = findViewById(R.id.subtitle_vg);
        list_vg = findViewById(R.id.push_vg);
        grade_vg = findViewById(R.id.grade_vg);
        clroom_vg = findViewById(R.id.clroom_vg);
        counter = 0L;
    }

    public void addlist(View view) {
        put = list_vg.getText().toString();
        list.put(String.valueOf(counter), put);
        counter += 1;
        list_vg.setText("");
    }

    public void onClick(View view) {
        final AlertDialog.Builder diabuild = new AlertDialog.Builder(this);
        final Map<String, Long> forsomeone = new HashMap<>();
        final Map<String, Long> answer = new HashMap<>();
        try {
            forsomeone.put("grade", Long.parseLong(grade_vg.getText().toString()));
            forsomeone.put("clroom", Long.parseLong(clroom_vg.getText().toString()));
            vote.put("title", title_vg.getText().toString());
            vote.put("info", subtitle_vg.getText().toString());
            vote.put("isClosed", closed);
            vote.put("opener", user.getEmail());
            vote.put("for", forsomeone);
            vote.put("counter", counter);
            vote.put("answer", answer);
            final Intent menu = new Intent(this, MainMenuActivity.class);
            final String TAG = "addVote";
            vote.put("Lists", list);
            if (user.isAdmin()) {
                db.collection("votes").document(title_vg.getText().toString()).set(vote).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Added vote with title " + title_vg.getText().toString());
                        diabuild.setTitle("");
                        diabuild.setMessage("투표가 등록되었습니다.");
                        diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        diabuild.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding vote", e);
                    }
                });
            } else if (!user.isAdmin()) {
                diabuild.setTitle("");
                diabuild.setMessage("관리자만 들어올 수 있어요!");
                diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                diabuild.show();
            }
        } catch (Exception e) {
            diabuild.setTitle("");
            diabuild.setMessage("유효하지 않은 값이 기입되었습니다.");
            diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            diabuild.show();
        }
    }

    public void menu(View view) {
        finish();
    }

}