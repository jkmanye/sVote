package com.schoolvote.schoolvote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    GregorianCalendar gc = new GregorianCalendar();
    public Date date = new Date();
    public int d = gc.get( gc.DAY_OF_MONTH );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    public void onClick(View view) {
        final pubMethods pm = new pubMethods();
        final AlertDialog.Builder diabuild = new AlertDialog.Builder(this);
        final Intent login = new Intent(this, LoginActivity.class);
        final Intent register = new Intent(this, RegisterActivity.class);
        EditText email_reg = findViewById(R.id.email_reg);
        EditText pw_reg = findViewById(R.id.pw_reg);
        EditText grade_reg = findViewById(R.id.grade_reg);
        EditText clroom_reg = findViewById(R.id.clroom_reg);
        EditText number_reg = findViewById(R.id.number_reg);
        final Map<String, Object> userdata = new HashMap<>();
        final String email = email_reg.getText().toString();
        final String password = pw_reg.getText().toString();
        final int grade;
        final int clroom;
        final int number;
        final boolean isAdmin = false;
        final int openedVote = 0;
        if (pm.isNumeric(grade_reg.getText().toString()) && pm.isNumeric(clroom_reg.getText().toString()) && pm.isNumeric(grade_reg.getText().toString())) {
            grade = Integer.parseInt(grade_reg.getText().toString());
            clroom = Integer.parseInt(clroom_reg.getText().toString());
            number = Integer.parseInt(number_reg.getText().toString());
            userdata.put("grade", grade);
            userdata.put("clroom", clroom);
            userdata.put("number", number);
            userdata.put("isAdmin", isAdmin);
            userdata.put("openedVote", openedVote);
        } else {
            diabuild.setTitle("회원가입 실패");
            diabuild.setMessage("학년, 반, 번호에는 반드시 숫자만 입력해주세요!");
            diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(register, 1001);
                }
            });
            diabuild.show();
        }
        final String TAG = "createUser";
        final String em = "가입에 실패하였습니다. 이미 같은 이메일로 다른 계정이 있거나 약한 비밀번호를 설정할 경우 종종 일어나는 에러입니다. 비밀번호는 6~20자까지 가능합니다.";
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분");
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                db.collection("users").document(email).set(userdata)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Added userdata document with a title " + email);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding userdata", e);
                                            }
                                        });
                                currentUser.sendEmailVerification();
                                diabuild.setTitle("회원가입 성공");
                                diabuild.setMessage("회원가입에 성공하였습니다. 입력하신 이메일로 인증 메일을 보냈으니 " +  sdf.format(date) + " 로부터 하루 뒤까지 인증 후 로그인해 주시기 바랍니다.");
                                diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(login, 1001);
                                    }
                                });
                                diabuild.show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                diabuild.setTitle("회원가입 실패");
                                diabuild.setMessage(em);
                                diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(register, 1001);
                                    }
                                });
                                diabuild.show();
                            }

                            // ...
                        }
                    });
        } catch(Exception rege) {
            diabuild.setTitle("회원가입 실패");
            diabuild.setMessage("옯바른 값을 입력해주세요!!!");
            diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(register, 1001);
                }
            });
            diabuild.show();
        }
    }

    public void goBack(View view) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login, 1001);
    }
}