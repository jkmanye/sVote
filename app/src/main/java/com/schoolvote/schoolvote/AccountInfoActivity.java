package com.schoolvote.schoolvote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AccountInfoActivity extends AppCompatActivity {

    User user;

    @Override
    @SuppressLint("SetTextI18n")

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        final TextView email_ai = findViewById(R.id.email_ai);
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        User currentUser = (User) getIntent().getSerializableExtra("currentUser");
        email_ai.setText(currentUser.getEmail());
        final TextView grade_ai = findViewById(R.id.grade_ai);
        grade_ai.setText(Long.toString(currentUser.getGrade_update()));
        final TextView clroom_ai = findViewById(R.id.clroom_ai);
        clroom_ai.setText(Long.toString(currentUser.getClroom_update()));
        final TextView number_ai = findViewById(R.id.number_ai);
        number_ai.setText(Long.toString(currentUser.getNumber_update()));
    }

    public void goback(View view) {

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
    }
}
