package com.schoolvote.schoolvote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteAlertDialogActivity extends AppCompatActivity {

    AlertDialog.Builder diabuild;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_alert_dialog);
        diabuild = new AlertDialog.Builder(this);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        final Map<String, String>[] Lists = new Map[]{new HashMap<>()};
        DocumentReference documentReference = db.collection("votes").document(currentUser.getJoiningVoteTitle());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Lists[0] = (Map) documentSnapshot.get("Lists");
                    Map<String, Long> answer = (Map<String, Long>) documentSnapshot.get("answer");
                    setup(Lists[0].size(), Lists[0], currentUser.getJoiningVoteTitle(), answer);
                }
            }
        });
    }

    public void setup(int Length, Map<String, String> Lists, final String title, final Map<String, Long> answer) {
        final List<String> Items = new ArrayList<>(Lists.values());
        diabuild.setTitle(title);
        final CharSequence[] items = Items.toArray(new String[Length]);
        diabuild.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final DocumentReference votes = db.collection("votes").document(title);
                answer.put(currentUser.getEmail(), (long) i);
                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(votes);

                        // Note: this could be done without a transaction
                        //       by updating the population using FieldValue.increment()
                        transaction.update(votes, "answer", answer);

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("VoteAlertDialogActivity", "Voted successfully!");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("VoteAlertDialogActivity", "Voted with a failure.", e);
                    }
                });
            }
        });
        diabuild.show();
    }
}
