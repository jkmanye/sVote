package com.schoolvote.schoolvote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class VotePreviewActivity extends AppCompatActivity {

    User currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView votetitle_vp;
    TextView info_vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_preview);
        votetitle_vp = findViewById(R.id.votetitle_vp);
        info_vp = findViewById(R.id.info_vp);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        updateTitle(currentUser.getJoiningVoteTitle());
    }

    public void updateTitle(String title) {
        votetitle_vp.setText(title);
        DocumentReference documentReference = db.collection("votes").document(title);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                final String subtitle;
                subtitle = document.getString("info");
                updateSubtitle(subtitle);
            }
        });
    }

    public void updateSubtitle(String subtitle) {
        info_vp.setText(subtitle);
    }

    public void goback(View view) {
        final Intent menu = new Intent(this, MainMenuActivity.class);
        startActivityForResult(menu, 1001);
    }

    public void finaljoin(View view) {
        final Intent intent = new Intent(this, VoteAlertDialogActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivityForResult(intent, 1001);
    }
}
