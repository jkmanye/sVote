package com.schoolvote.schoolvote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class pubMethods {

    String email;
    public long grade_update;
    public long clroom_update;
    public long number_update;
    public boolean isAdmin;
    public String joiningVoteTitle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private void update() {
        Log.d("updateUI", "Successed adding update datas.");
    }

    public boolean isNumeric(String s) {
        if(s != "" && s != null) return s.matches("-?\\d+(\\.\\d+)?");
        else return false;
    }

//    public void updateUI(FirebaseUser user) {
//        // Method that updates UI of sVote activities
//        if(user != null) {
//            email = user.getEmail();
//            final String TAG = "updateUI";
//            final DocumentReference docRef = db.collection("users").document(email);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            grade_update = (long)document.get("grade");
//                            clroom_update = (long)document.get("clroom");
//                            number_update = (long)document.get("number");
//                            isAdmin = (boolean)document.get("isAdmin");
//                        } else {
//                            Log.d(TAG, "No such document!");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });
//            Log.d("updateUI", "Successed adding update datas.");
//        }
//    }

    public void reauthenticate(FirebaseUser user, int grade, int clroom, int number) {
        final String reauemail;
        final Map<String, Object> reaudata = new HashMap<>();
        if(user != null) {
            reauemail = user.getEmail();
            reaudata.put("grade", grade);
            reaudata.put("clroom", clroom);
            reaudata.put("number", number);
            db.collection("users").document(reauemail).set(reaudata)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("reauthenticate", "Successfully Reauthenticated.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("reauthenticate", "Reauthenticating failed : " + e);
                        }
                    });
        } else {
            Log.d("reauthenticate", "Failed to reauthenticate because current user is null.");
        }
    }

    public void setJoiningVoteTitle(String title) {
        joiningVoteTitle = title;
    }
}
