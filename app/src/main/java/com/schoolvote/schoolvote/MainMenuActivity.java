package com.schoolvote.schoolvote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    User currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
    }

    public void vm(View view) {
        final Intent vm = new Intent(this, VoteManageActivity.class);
        if (currentUser != null) {
            vm.putExtra("currentUser", currentUser);
        }
        startActivityForResult(vm, 1001);
    }

    public void vs(View view) {
        final Intent vs = new Intent(this, VoteScheduleActivity.class);
        if (currentUser != null) {
            vs.putExtra("currentUser", currentUser);
        }
        startActivityForResult(vs, 1001);
    }

    public void ai(View view) {
        final Intent ai = new Intent(this, AccountInfoActivity.class);
        if (currentUser != null) {
            ai.putExtra("currentUser", currentUser);
        }
        startActivityForResult(ai, 1001);
    }

    public void vg(View view) {
        final Intent vg = new Intent(this, VoteGeneratingActivity.class);
        if (currentUser != null) {
            vg.putExtra("currentUser", currentUser);
        }
        startActivityForResult(vg, 1001);
    }
}
