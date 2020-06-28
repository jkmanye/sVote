package com.schoolvote.schoolvote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.GregorianCalendar;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    GregorianCalendar gc = new GregorianCalendar();
    int loginDate = gc.get(gc.DAY_OF_MONTH);
    RegisterActivity ra = new RegisterActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText email_lg = (EditText) findViewById(R.id.email_lg);
        EditText pw_lg = (EditText) findViewById(R.id.pw_lg);
        Button button_lg = (Button) findViewById(R.id.button_lg);
        Button register_lg = (Button) findViewById(R.id.register_lg);

        String email;
        String password;

        Intent register = new Intent(this, RegisterActivity.class);
        Intent login = new Intent(this, LoginActivity.class);
        Intent menu = new Intent(this, MainMenuActivity.class);

        final String TAG = "loginWithEmail";

        mAuth = FirebaseAuth.getInstance();
    }

    public void onClick(View view) {
        final AlertDialog.Builder diabuild = new AlertDialog.Builder(this);
        final pubMethods pm = new pubMethods();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText email_lg = findViewById(R.id.email_lg);
        EditText pw_lg = findViewById(R.id.pw_lg);

        String email = email_lg.getText().toString();
        String password = pw_lg.getText().toString();

        final Intent login = new Intent(this, LoginActivity.class);
        final Intent menu = new Intent(this, MainMenuActivity.class);

        final String TAG = "loginWithEmail";
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user.isEmailVerified()) {
//                                    pm.updateUI(user);
                                    final String email = user.getEmail();
                                    final String TAG = "updateUI";
                                    final DocumentReference docRef = db.collection("users").document(email);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    final User currentUser = new User();
                                                    currentUser.setEmail(email);
                                                    currentUser.setGrade_update((long) document.get("grade"));
                                                    currentUser.setClroom_update((long) document.get("clroom"));
                                                    currentUser.setNumber_update((long) document.get("number"));
                                                    currentUser.setAdmin((boolean) document.get("isAdmin"));

                                                    Log.d(TAG, "signInWithEmail:success");
                                                    diabuild.setTitle("로그인 성공");
                                                    diabuild.setMessage("로그인에 성공하였습니다.");
                                                    diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            menu.putExtra("currentUser", currentUser);
                                                            startActivityForResult(menu, 1001);
                                                        }
                                                    });
                                                    diabuild.show();

                                                } else {
                                                    Log.d(TAG, "No such document!");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                    Log.d("updateUI", "Successed adding update datas.");
                                } else {
                                    if (loginDate - ra.d <= 1) {
                                        diabuild.setTitle("로그인 실패");
                                        diabuild.setMessage("인증되지 않은 이메일입니다. 인증을 완료한 후 다시 시도하세요.");
                                        diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivityForResult(login, 1001);
                                            }
                                        });
                                        diabuild.show();
                                    } else {
                                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
                                        docRef.delete();
                                        user.delete();
                                        diabuild.setTitle("로그인 실패");
                                        diabuild.setMessage("인증이 제한 시간 안에 되지 않은 계정입니다. 재가입 뒤 인증을 완료한 후 다시 시도하세요.");
                                        diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivityForResult(login, 1001);
                                            }
                                        });
                                        diabuild.show();
                                    }
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                diabuild.setTitle("로그인 실패");
                                diabuild.setMessage("로그인에 실패하였습니다. 없는 계정이거나 오타가 있을 수 있습니다.");
                                diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(login, 1001);
                                    }
                                });
                                diabuild.show();
                            }

                            // ...
                        }
                    });
        } catch (Exception e) {
            diabuild.setTitle("로그인 실패");
            diabuild.setMessage("올바른 이메일이나 비밀번호를 입력해주세요!");
            diabuild.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(login, 1001);
                }
            });
            diabuild.show();
        }
    }

    public void reg(View view) {
        final Intent register = new Intent(this, RegisterActivity.class);
        startActivityForResult(register, 1001);
    }

}